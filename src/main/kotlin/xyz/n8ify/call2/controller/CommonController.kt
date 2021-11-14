package xyz.n8ify.call2.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/common/")
class CommonController {

    @Value("\${app.version}")
    lateinit var appVersion: String

    @GetMapping("/version")
    fun version() : String {
        return appVersion
    }

}