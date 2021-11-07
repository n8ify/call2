package xyz.n8ify.call2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class CallController {

    @Autowired
    lateinit var callService: CallService

    @PostMapping("/register")
    fun register(@RequestBody serviceInfo: ServiceInfo) : Boolean {
        TODO()
    }

    @GetMapping("/services")
    fun services() : List<ServiceInfo> {
        TODO()
    }

    @GetMapping("/check/{id}")
    fun check(@PathVariable id: String) {

    }

}