package xyz.n8ify.call2.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Value("#{'\${security.permitAll}'.split(',')}")
    lateinit var permitAllEndpoints : List<String>

    @Bean
    fun passwordEncoder() : BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var userDetailService: UserDetailsService

    @Autowired
    fun configureGlobal(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder())
    }

    override fun configure(http: HttpSecurity?) {
        http?.run {
            csrf().disable()
                .authorizeRequests()
                .antMatchers(*permitAllEndpoints.toTypedArray()).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .usernameParameter("username")
                .passwordParameter("password")
                .failureHandler { _, response, _ -> response.outputStream.use { it.print("not allowed!") } }
//                .successHandler { _, response, _ -> response.outputStream.use { it.print("allowed!") } }
            cors().disable()
        }
    }
}