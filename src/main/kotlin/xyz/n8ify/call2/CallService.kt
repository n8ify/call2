package xyz.n8ify.call2

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Service
import javax.sql.DataSource


@Service
@PropertySource("classpath:persistence.properties")
class CallService {

    @Autowired
    lateinit var env: Environment

    @Bean
    fun datasource(): DataSource {
        return DriverManagerDataSource().apply {
            setDriverClassName(env.getProperty("driverClassName")!!)
            url = env.getProperty("url")!!
            username = env.getProperty("username")!!
            password = env.getProperty("password")!!
        }
    }


}