package xyz.n8ify.call2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class CallController {

    @Autowired
    lateinit var callService: CallService

    @PostMapping("/register")
    fun register(@RequestBody serviceInfo: ServiceInfo) : Boolean = callService.register(serviceInfo)

    @PostMapping("/unregister/{id}")
    fun unregister(@PathVariable id: String) : Boolean = callService.unregister(id)

    @GetMapping("/services")
    fun services() : List<ServiceInfo> = callService.findAll()

    @GetMapping("/check")
    fun check(@RequestBody serviceInfo: ServiceInfo) : StatusInfo = callService.checkServiceStatus(serviceInfo)

}