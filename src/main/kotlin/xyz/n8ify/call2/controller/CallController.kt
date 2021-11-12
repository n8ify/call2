package xyz.n8ify.call2.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import xyz.n8ify.call2.enums.ServiceGroup
import xyz.n8ify.call2.model.StatusInfo
import xyz.n8ify.call2.model.rest.request.HealthCheckRequest
import xyz.n8ify.call2.model.rest.request.RegisterServiceRequest
import xyz.n8ify.call2.model.rest.response.BaseResponse
import xyz.n8ify.call2.repository.entity.ServiceEntity
import xyz.n8ify.call2.service.CallService

@RestController
class CallController {

    @Autowired
    lateinit var callService: CallService

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterServiceRequest) : BaseResponse<ServiceEntity> {
        return callService.register(request)
    }

    @PostMapping("/unregister/{id}")
    fun unregister(@PathVariable id: String) : BaseResponse<Unit> {
        return callService.unregister(id)
    }

    @GetMapping("/services")
    fun services() : BaseResponse<List<ServiceEntity>> {
        return callService.findAll()
    }

    @GetMapping("/groups")
    fun serviceGroups() : BaseResponse<List<ServiceGroup.Data>> {
        return BaseResponse(true, ServiceGroup.values().map { ServiceGroup.Data(it.id, it.title) })
    }

    @GetMapping("/check")
    fun check(@RequestBody request: HealthCheckRequest) : BaseResponse<StatusInfo> {
        return callService.checkServiceStatus(request)
    }

}