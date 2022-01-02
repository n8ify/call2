package xyz.n8ify.call2

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.metrics.export.prometheus.EnablePrometheusMetrics
import org.springframework.stereotype.Component
import xyz.n8ify.call2.enums.Role
import xyz.n8ify.call2.repository.entity.RoleEntity
import xyz.n8ify.call2.repository.entity.UserEntity
import xyz.n8ify.call2.service.UserService

@SpringBootApplication
//@EnablePrometheusMetrics
class Call2Application

@Component
class StartUp : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(StartUp::class.java)

    @Autowired
    lateinit var userService: UserService

    @Value("\${app.super.user.name}")
    lateinit var defaultUsername: String

    @Value("\${app.super.user.password}")
    lateinit var defaultPassword: String


    override fun run(vararg args: String?) {
        userService.findByUsername(defaultUsername)?: run {
            userService.run {
                logger.info("Default user \"${defaultUsername}\" was not found, Do create for first time...")

                val roleAdmin = RoleEntity(0, Role.Administrator.name)
                val roleViewer = RoleEntity(1, Role.Viewer.name)
                userService.saveRole(roleAdmin)
                userService.saveRole(roleViewer)

                userService.saveUser( defaultUsername, defaultPassword, mutableListOf(Role.Administrator))
                logger.info("Default user \"${defaultUsername}\" was created...")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<Call2Application>(*args)
}
