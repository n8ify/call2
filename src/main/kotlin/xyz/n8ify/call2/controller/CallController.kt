package xyz.n8ify.call2.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.metrics.annotation.Timed
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import xyz.n8ify.call2.enums.ServiceGroup
import xyz.n8ify.call2.model.StatusInfo
import xyz.n8ify.call2.model.rest.request.HealthCheckRequest
import xyz.n8ify.call2.model.rest.request.RegisterServiceRequest
import xyz.n8ify.call2.model.rest.request.UpdateServiceRequest
import xyz.n8ify.call2.model.rest.request.UpdateServiceStatusRequest
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

    @PostMapping("/update")
    fun update(@RequestBody request: UpdateServiceRequest) : BaseResponse<ServiceEntity> {
        return callService.update(request)
    }

    @PostMapping("/unregister/{id}")
    fun unregister(@PathVariable id: String) : BaseResponse<Unit> {
        return callService.unregister(id)
    }

    @PostMapping("/updateStatus")
    fun unregister(@RequestBody request: UpdateServiceStatusRequest) : BaseResponse<Unit> {
        return callService.updateStatus(request.id)
    }

    @GetMapping("/services")
    fun services() : BaseResponse<List<ServiceEntity>> {
        return callService.findAll()
    }

    @GetMapping("/groupedServices")
    fun groupedServices() : BaseResponse<List<Map<String, Any>>> {
        return callService.findAllGrouped()
    }

    @GetMapping("/groups")
    fun serviceGroups() : BaseResponse<List<ServiceGroup.Data>> {
        return BaseResponse(true, ServiceGroup.values().map { ServiceGroup.Data(it.id, it.title) })
    }

    @PostMapping("/check")
    fun check(@RequestBody request: HealthCheckRequest) : BaseResponse<StatusInfo> {
        return callService.checkServiceStatus(request)
    }

    @GetMapping("/export", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun export() : ResponseEntity<ByteArray> = callService.export()

    @PostMapping("/import")
    fun import(@RequestParam("file") request: MultipartFile) : BaseResponse<Unit> {
        return callService.import(request)
    }

}