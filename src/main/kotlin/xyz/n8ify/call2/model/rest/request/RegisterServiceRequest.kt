package xyz.n8ify.call2.model.rest.request

import xyz.n8ify.call2.repository.entity.ServiceEntity
import java.util.*

data class RegisterServiceRequest(
    val id: String = UUID.randomUUID().toString(),
    val groupId: String,
    val title: String,
    val description: String? = null,
    val url: String,
    val thumbnailUrl: String? = null
) {
    fun toServiceEntity(): ServiceEntity = ServiceEntity(
        id,
        groupId,
        title,
        description,
        url,
        thumbnailUrl
    )
}