package xyz.n8ify.call2.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import xyz.n8ify.call2.repository.ServiceRepository
import xyz.n8ify.call2.model.StatusInfo
import xyz.n8ify.call2.model.rest.request.HealthCheckRequest
import xyz.n8ify.call2.model.rest.request.RegisterServiceRequest
import xyz.n8ify.call2.model.rest.response.BaseResponse
import xyz.n8ify.call2.repository.entity.ServiceEntity
import java.text.DecimalFormat
import javax.persistence.EntityManager

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
            logger.error("Register Success!", result)
            BaseResponse(true, result)
        } catch (e : Exception) {
            logger.error("Register error", e)
            BaseResponse(false, null)
        }
    }

    fun unregister(id: String): BaseResponse<Unit> {
        return try {
            repository.deleteById(id)
            logger.error("Unregister Success!", id)
            BaseResponse(true, null)
        } catch (e: Exception) {
            logger.error("Unregister error", e)
            BaseResponse(false, null)
        }
    }

    fun findAll(): List<ServiceEntity> = repository.findAll()

    fun checkServiceStatus(request: HealthCheckRequest): StatusInfo {
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
        return StatusInfo(
            ok = ok,
            healthy = healthy,
            status = status,
            responseMs = end
        )
    }

}