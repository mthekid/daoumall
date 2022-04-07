package com.daou.project.daoumall.config.security

import com.daou.project.daoumall.config.log.Log
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter : BasicAuthenticationFilter {

    private var jwtUtil: JWTUtil
    private var userDetailService: UserDetailsService

    constructor(authenticationManager: AuthenticationManager, jwtUtil: JWTUtil, userDetailService: UserDetailsService) :
            super(authenticationManager) {
        this.jwtUtil = jwtUtil
        this.userDetailService = userDetailService
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")

        Log.info("authorizationHeader를 확인합니다. header 정보 : $authorizationHeader")
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            Log.info("auth 정보를 확인합니다.")
            val auth = getAuthentication(authorizationHeader)
            Log.info("auth 정보 $auth")
            SecurityContextHolder.getContext().authentication = auth
        }
        Log.info("스프링 시큐리티 체이닝을 진행합니다.")

        chain.doFilter(request, response)
    }

    private fun getAuthentication(authorizationHeader: String?): UsernamePasswordAuthenticationToken {
        Log.info("getAuthentication으로 들어온 헤더 정보 $authorizationHeader")
        val token = authorizationHeader?.substring(7) ?: ""

        Log.info("토큰 정보 : $token")
        if(jwtUtil.validateToken(token)) {
            Log.info("토큰이 검증된 토큰입니다.")
            val username = jwtUtil.getUserName(token)
            val user = userDetailService.loadUserByUsername(username)

            Log.info("사용자 이름(이메일) : $username , 사용자 정보 : $user")
            Log.info("사용자의 이름과 비밀번호를 검증하고 권한을 검증합니다.")
            return UsernamePasswordAuthenticationToken(user, null, user.authorities)
        }
        throw UsernameNotFoundException("<getAuthentication>로직이 실패했습니다! Auth invalid!")
    }

}