package xyz.n8ify.call2.model

data class StatusInfo(val ok: Boolean, val healthy: Int, val status: String, val responseMs: Long) {
    enum class Status (val id: Int, val desc: String) {
        Healthy(1, "Healthy"),
        Fine(2, "Fine"),
        Unhealthy(3, "Unhealthy"),
        Unreachable(4, "Unreachable")
    }
}
