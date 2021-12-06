package xyz.n8ify.call2.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
//
//    @Bean
//    fun passwordEncoder() : BCryptPasswordEncoder = BCryptPasswordEncoder()
//
//    @Autowired
//    fun configureGlobal(authenticationManagerBuilder: AuthenticationManagerBuilder) {
//        authenticationManagerBuilder.inMemoryAuthentication()
//            .withUser(System.getenv("CALL2_USERNAME"))
//            .password(System.getenv("CALL2_PASSWORD"))
//            .roles("ADMIN")
//    }

    override fun configure(http: HttpSecurity?) {
        http?.run {
            csrf().disable()
                .authorizeRequests()
                .antMatchers("/login*", "/common/**", "/groupedServices", "/check", "/assets/**").permitAll()
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