package xyz.n8ify.call2.model.rest.request

import xyz.n8ify.call2.repository.entity.ServiceEntity
import java.util.*

data class UpdateServiceRequest(
    val id: String,
    val groupId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val healthCheckUrl: String? = null,
)