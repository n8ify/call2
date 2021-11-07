package xyz.n8ify.call2

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "service")
data class ServiceInfo(
    @Id val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String? = null,
    val url: String,
    val thumbnailUrl: String? = null)

data class StatusInfo(val ok: Boolean, val healthy: String, val status: String, val responseMs: Long) {
    companion object {
        const val Healthy = "Healthy"
        const val Fine = "Fine"
        const val Unhealthy = "Unhealthy"
    }
}
