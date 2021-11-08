package xyz.n8ify.call2

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import xyz.n8ify.call2.service.CallService

@SpringBootTest
class Call2ApplicationTests {

    @Autowired
    lateinit var service: CallService

    val testFileBrowserServiceInfo = ServiceInfo(
        title = "Test File Browser",
        description = "Test Remote File Browser",
        url = "https://file.n8ify.xyz",
        thumbnailUrl = "/logos/filebrowser.png"
    )

    @Test
    fun contextLoads() {

    }

    @Test
    fun register() {
        assert(service.register(testFileBrowserServiceInfo))
        service.findAll().forEach(::println)
//        assert(service.unregister(testFileBrowserServiceInfo.id))
    }

    @Test
    fun healthCheck() {
        println(testFileBrowserServiceInfo)
        println(service.checkServiceStatus(testFileBrowserServiceInfo))
    }

}
