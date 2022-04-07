package com.daou.project.daoumall.config.security

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Component
class CorsFilter {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        var corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOrigin("*")
        corsConfiguration.maxAge = 8000L
        corsConfiguration.addAllowedMethod(HttpMethod.GET)
        corsConfiguration.addAllowedMethod(HttpMethod.POST)
        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS)
        corsConfiguration.addAllowedHeader("Content-Type")


        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)

        return source
    }
}