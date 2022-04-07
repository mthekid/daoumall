package com.daou.project.daoumall.config.security

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.service.customer.CustomerService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

/**
 * Jwt 생성 및 검증을 위한 유틸리티 클래스입니다.
 * 참고 블로그 : https://velog.io/@dhk22/TIL-Day-71-Kotlin-03-Spring-Security-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0-Jwt%EB%B0%A9%EC%8B%9D-%EC%9D%B8%EC%A6%9D
 */
@Component
class JWTUtil {
    val expiration: Long = 60000L
    val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256
    var secretKey: SecretKey = Keys.secretKeyFor(signatureAlgorithm)

    fun createToken(username: String): String {
        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setSubject(username)
            .setExpiration(Date(expiration))
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        val claims = getClaimsToken(token)
        if(claims != null) {
            val username = claims.subject
            val expirationDate = claims.expiration
            val now = Date(System.currentTimeMillis())
            if(username != null && expirationDate != null && now.before(expirationDate)) {
                return true
            }
        }
        return false
    }

    fun getClaimsToken(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            throw RuntimeException("getClaimsToken 에러! : 전달된 토큰과 서버의 토큰이 일치하지 않습니다.")
        }
    }

    fun getUserName(token: String): String? {
        val claims = getClaimsToken(token)
        return claims?.subject
    }

//    fun getAuthentication(token: String): Authentication {
//        val customerDetail: Customer = customerService.getCustomerInfoByEmail(getCustomerPK(token))
//        return UsernamePasswordAuthenticationToken(customerDetail, "", customerDetail.role)
//    }

//    토큰에서 회원 정보 추출하기
//    fun getCustomerPK(token: String): String {
//        return Jwts.parserBuilder()
//            .setSigningKey(secretKey)
//            .build()
//            .parseClaimsJws(token)
//            .body.subject
//    }

}