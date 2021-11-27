package xyz.n8ify.call2.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import xyz.n8ify.call2.enums.ServiceGroup
import xyz.n8ify.call2.repository.ServiceRepository
import xyz.n8ify.call2.model.StatusInfo
import xyz.n8ify.call2.model.rest.request.HealthCheckRequest
import xyz.n8ify.call2.model.rest.request.RegisterServiceRequest
import xyz.n8ify.call2.model.rest.response.BaseResponse
import xyz.n8ify.call2.repository.entity.ServiceEntity
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
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

    fun findAll() = try {
        BaseResponse<List<ServiceEntity>>(true, repository.findAll())
    } catch (e: Exception) {
        logger.error("Find all failed", e)
        BaseResponse<List<ServiceEntity>>(false, null)
    }

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

    fun checkServiceStatus(request: HealthCheckRequest): BaseResponse<StatusInfo> {
        return try {
            val start = System.currentTimeMillis()
            val response = rest.exchange(request.url, HttpMethod.GET, null, String::class.java)
            val end = System.currentTimeMillis() - start
            val formattedTimeUsage = DecimalFormat("#.00").format(end)

            val ok = response.statusCode == HttpStatus.OK
            val healthy = when {
                end < 2000 -> StatusInfo.Healthy
                end < 3000 -> StatusInfo.Fine
                else -> StatusInfo.Unhealthy
            }
            val status = "$healthy : $formattedTimeUsage ms"
            BaseResponse<StatusInfo>(true, StatusInfo(
                ok = ok,
                healthy = healthy,
                status = status,
                responseMs = end
            ))
        } catch (e: Exception) {
            logger.error("Health checking for ${request.url} failed", e)
            BaseResponse<StatusInfo>(false, null)
        }
    }

    fun export() : ResponseEntity<ByteArray> {
        val content = jacksonObjectMapper().writeValueAsString(repository.findAll()).toByteArray(charset("UTF-8"))
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=Call2Export_${SimpleDateFormat("yyyyMMdd").format(Date())}.json")
            .contentLength(content.size.toLong())
            .contentType(MediaType.APPLICATION_JSON)
            .body(content)
    }

//    fun import(request: HttpServletRequest, response: HttpServletResponse) : ResponseEntity<ByteArray> {
//
//    }

}