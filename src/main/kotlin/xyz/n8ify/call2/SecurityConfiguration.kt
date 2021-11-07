package xyz.n8ify.call2

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.run {
            csrf().disable()
                .authorizeRequests()/*.antMatchers("/register", "/unregister").authenticated()*/
                .anyRequest().permitAll()
        }
    }
}