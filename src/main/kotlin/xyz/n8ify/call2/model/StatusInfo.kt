package xyz.n8ify.call2.model

data class StatusInfo(val ok: Boolean, val healthy: String, val status: String, val responseMs: Long) {
    companion object {
        const val Healthy = "Healthy"
        const val Fine = "Fine"
        const val Unhealthy = "Unhealthy"
    }
}
