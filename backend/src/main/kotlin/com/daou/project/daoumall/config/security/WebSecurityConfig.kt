package com.daou.project.daoumall.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

// 참고 : https://doozi316.github.io/spring%20security/2021/02/17/Spring5/

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @org.springframework.context.annotation.Lazy
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
            .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()

//        http.addFilter(JWTAuthenticationFilter(authenticationManager(), jwtUtil = jwtUtil))
//        http.addFilter(JWTAuthorizationFilter(authenticationManager(), jwtUtil = jwtUtil, userDetailService = userDetailsService))
    }
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder  {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser("foo").password(bCryptPasswordEncoder().encode("foo")).roles("USER")
            .and()
            .withUser("admin").password(bCryptPasswordEncoder().encode("admin"))
            .roles("USER").roles("ADMIN")
    }

//    override fun configure(auth: AuthenticationManagerBuilder) {
//        auth
//            .userDetailsService(userDetailsService)
//            .passwordEncoder(BCryptPasswordEncoder())
//    }
}