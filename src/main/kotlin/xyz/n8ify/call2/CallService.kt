package xyz.n8ify.call2

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.text.DecimalFormat
import javax.persistence.EntityManager

@Service
class CallService {

    private val logger = LoggerFactory.getLogger(CallService::class.java)
    private val rest = RestTemplate()

    @Autowired
    lateinit var repository: CallServiceRepository

    @Autowired
    lateinit var entityManager: EntityManager

    fun register(serviceInfo: ServiceInfo) : Boolean {
        return try {
            repository.save(serviceInfo)
            logger.error("Register Success!", serviceInfo)
            true
        } catch (e: Exception) {
            logger.error("Register error", e)
            false
        }
    }

    fun unregister(id: String) : Boolean {
        return try {
            repository.deleteById(id)
            logger.error("Unregister Success!", id)
            true
        } catch (e: Exception) {
            logger.error("Unregister error", e)
            false
        }
    }

    fun findAll() : List<ServiceInfo> = repository.findAll()

    fun checkServiceStatus(serviceInfo: ServiceInfo): StatusInfo {
        val start = System.currentTimeMillis()
        val response = rest.exchange(serviceInfo.url, HttpMethod.GET, null, String::class.java)
        val end = System.currentTimeMillis() - start
        val formattedTimeUsage = DecimalFormat("#.00").format(end)

        val ok = response.statusCode == HttpStatus.OK
        val healthy = when  {
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