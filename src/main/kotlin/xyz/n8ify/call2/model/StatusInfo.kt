package xyz.n8ify.call2.model

data class StatusInfo(val ok: Boolean, val healthy: Int, val status: String, val responseMs: Long) {
    companion object {
        const val Healthy = 1
        const val Fine = 2
        const val Unhealthy = 3
    }
}
