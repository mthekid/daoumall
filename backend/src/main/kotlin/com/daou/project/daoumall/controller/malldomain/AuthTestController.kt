package com.daou.project.daoumall.controller.malldomain

import com.daou.project.daoumall.config.log.Log
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthTestController {

    @GetMapping("/hello")
    fun hello(): String{
        Log.info("Hello")
        return "hello"
    }

    @GetMapping("/login")
    fun login() :ResponseEntity<Any?>{
        Log.info("GET 요청을 성공적으로 받았습니다.")
        return ResponseEntity.ok().body(" /login GET 요청 성공!")
    }
}