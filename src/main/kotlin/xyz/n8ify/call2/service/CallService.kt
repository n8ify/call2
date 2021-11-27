package xyz.n8ify.call2.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import xyz.n8ify.call2.enums.ServiceGroup
import xyz.n8ify.call2.repository.ServiceRepository
import xyz.n8ify.call2.model.StatusInfo
import xyz.n8ify.call2.model.rest.request.HealthCheckRequest
import xyz.n8ify.call2.model.rest.request.RegisterServiceRequest
import xyz.n8ify.call2.model.rest.request.UpdateServiceRequest
import xyz.n8ify.call2.model.rest.response.BaseResponse
import xyz.n8ify.call2.repository.entity.ServiceEntity
import java.lang.IllegalArgumentException
import java.net.URL
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import javax.net.ssl.HttpsURLConnection
import javax.persistence.EntityManager
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class CallService {

    private val logger = LoggerFactory.getLogger(CallService::class.java)
    private val rest = RestTemplate()

    @Autowired
    lateinit var repository: ServiceRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Transactional(
        isolation = Isolation.SERIALIZABLE,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class]
    )
    fun register(request: RegisterServiceRequest): BaseResponse<ServiceEntity> {
        return try {
            val result = repository.saveAndFlush(request.toServiceEntity())
            logger.info("Register Success!", result)
            BaseResponse(true, result)
        } catch (e: Exception) {
            logger.error("Register error", e)
            BaseResponse(false, null)
        }
    }

    @Transactional(
        isolation = Isolation.SERIALIZABLE,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class]
    )
    fun update(request: UpdateServiceRequest): BaseResponse<ServiceEntity> {
        return try {
            val update = repository.findById(request.id)
                .orElseGet { throw IllegalArgumentException("[${request.id}] is not found ") }
                .run {
                    copy(
                        groupId = request.groupId ?: groupId,
                        title = request.title ?: title,
                        description = request.description ?: description,
                        url = request.url ?: url,
                        healthCheckUrl = request.healthCheckUrl ?: healthCheckUrl,
                    )
                }
            val result = repository.saveAndFlush(update)
            logger.info("Update Success!", result)
            BaseResponse(true, result)
        } catch (e: Exception) {
            logger.error("Update error", e)
            BaseResponse(false, null)
        }
    }

    @Transactional(
        isolation = Isolation.SERIALIZABLE,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class]
    )
    fun unregister(id: String): BaseResponse<Unit> {
        return try {
            repository.deleteById(id)
            logger.info("Unregister Success!", id)
            BaseResponse(true, null)
        } catch (e: Exception) {
            logger.error("Unregister error", e)
            BaseResponse(false, null)
        }
    }

    @Transactional(
        isolation = Isolation.SERIALIZABLE,
        propagation = Propagation.REQUIRED,
        rollbackFor = [Throwable::class]
    )
    fun updateStatus(id: String): BaseResponse<Unit> {
        return try {
            repository.findById(id).get().let {
                repository.save(it.copy(isEnable = it.isEnable.not()))
                logger.info("Update service status Success!", id)
                BaseResponse(true, null)
            }
        } catch (e: Exception) {
            logger.error("Update service status error for $id", e)
            BaseResponse(false, null)
        }
    }

    @Transactional
    fun findAll() = try {
        BaseResponse<List<ServiceEntity>>(true, repository.findAll())
    } catch (e: Exception) {
        logger.error("Find all failed", e)
        BaseResponse<List<ServiceEntity>>(false, null)
    }

    @Transactional
    fun findAllGrouped() = try {
        val result = repository.findByIsEnableTrue()
            .groupBy { it.groupId }
            .map {
                mapOf(
                    "serviceType" to ServiceGroup.values().toList().single { g -> it.key == g.id.toString() }.title,
                    "menus" to it.value
                )
            }
        BaseResponse<List<Map<String, Any>>>(true, result)
    } catch (e: Exception) {
        logger.error("Find all failed", e)
        BaseResponse<List<Map<String, Any>>>(false, null)
    }

    @Transactional
    fun checkServiceStatus(request: HealthCheckRequest): BaseResponse<StatusInfo> {
        val start = System.currentTimeMillis()

        return try {
            (URL(request.url).openConnection() as HttpsURLConnection).run {
                requestMethod = "HEAD"
                connectTimeout = 5000
                readTimeout = 5000
                val ok = responseCode in 200..299
                if (ok) {
                    val end = System.currentTimeMillis() - start
                    val formattedTimeUsage = DecimalFormat("#.00").format(end)

                    val healthy = when {
                        end < 2000 -> StatusInfo.Status.Healthy
                        end < 3000 -> StatusInfo.Status.Fine
                        else -> StatusInfo.Status.Unhealthy
                    }
                    val status = "${healthy.desc} : $formattedTimeUsage ms"
                    BaseResponse<StatusInfo>(
                        true, StatusInfo(
                            ok = ok,
                            healthy = healthy.id,
                            status = status,
                            responseMs = end
                        )
                    )
                } else {
                    BaseResponse<StatusInfo>(
                        true, StatusInfo(
                            ok = ok,
                            healthy = StatusInfo.Status.Unreachable.id,
                            status = "Unreachable ($responseCode)",
                            responseMs = 0
                        )
                    )
                }
            }
        } catch (e: Exception) {
            logger.error("Health checking for ${request.url} failed, $e")
            BaseResponse<StatusInfo>(
                true, StatusInfo(
                    ok = false,
                    healthy = StatusInfo.Status.Unreachable.id,
                    status = "Unreachable",
                    responseMs = 0
                )
            )
        }
    }

    @Transactional
    fun export(): ResponseEntity<ByteArray> {
        val content = jacksonObjectMapper().writeValueAsString(repository.findAll()).toByteArray(charset("UTF-8"))
        return ResponseEntity.ok()
            .header(
                "Content-Disposition",
                "attachment; filename=Call2Export_${SimpleDateFormat("yyyyMMdd").format(Date())}.json"
            )
            .contentLength(content.size.toLong())
            .contentType(MediaType.APPLICATION_JSON)
            .body(content)
    }

//    fun import(request: HttpServletRequest, response: HttpServletResponse) : ResponseEntity<ByteArray> {
//
//    }

}