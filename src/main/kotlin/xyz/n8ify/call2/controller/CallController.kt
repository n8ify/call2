package xyz.n8ify.call2.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import xyz.n8ify.call2.model.StatusInfo
import xyz.n8ify.call2.model.rest.request.HealthCheckRequest
import xyz.n8ify.call2.model.rest.request.RegisterServiceRequest
import xyz.n8ify.call2.repository.entity.ServiceEntity
import xyz.n8ify.call2.service.CallService

@RestController
class CallController {

    @Autowired
    lateinit var callService: CallService

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterServiceRequest) : Boolean = callService.register(request)

    @PostMapping("/unregister/{id}")
    fun unregister(@PathVariable id: String) : Boolean = callService.unregister(id)

    @GetMapping("/services")
    fun services() : List<ServiceEntity> = callService.findAll()

    @GetMapping("/check")
    fun check(@RequestBody request: HealthCheckRequest) : StatusInfo = callService.checkServiceStatus(request)

}