package xyz.n8ify.call2.enums;

import com.fasterxml.jackson.annotation.JsonFormat


enum class ServiceGroup(val id: Int, val title: String) {

    Utilities(1, "Utilities"),
    Management(2, "Management"),
    Entertain(3, "Entertain"),
    Monitoring(4, "Monitoring"),
    Service(5, "Service");

    data class Data(val id: Int, val title: String)

}