package xyz.n8ify.call2.model.rest.request

import xyz.n8ify.call2.repository.entity.ServiceEntity
import java.util.*

data class RegisterServiceRequest(
    val id: String = UUID.randomUUID().toString(),
    val groupId: String,
    val title: String,
    val description: String? = null,
    val url: String,
    val healthCheckUrl: String? = null,
    val thumbnail: String? = null,
    val isLink: Boolean = true,
    val isEnable: Boolean = true
) {
    fun toServiceEntity(): ServiceEntity = ServiceEntity(
        id,
        groupId,
        title,
        description,
        url,
        healthCheckUrl,
        thumbnail,
        isLink,
        isEnable
    )
}