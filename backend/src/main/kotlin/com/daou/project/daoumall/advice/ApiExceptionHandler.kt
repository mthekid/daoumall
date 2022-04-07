package com.daou.project.daoumall.advice

import com.daou.project.daoumall.advice.exception.BaseException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {
    
    @ExceptionHandler(value = [BaseException::class])
    fun handleBaseException(e : BaseException): ResponseEntity<Any?> {
        return ResponseEntity.status(e.customResponseCode.status)
            .body(e.customResponseCode.message)
    }
}