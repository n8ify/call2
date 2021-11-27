package xyz.n8ify.call2.model.rest.request

import xyz.n8ify.call2.repository.entity.ServiceEntity
import java.util.*

data class UpdateServiceStatusRequest(
    val id: String,
    val isEnable: Boolean
)