package com.daou.project.daoumall.controller.malldomain

import com.daou.project.daoumall.config.log.Log
//import com.daou.project.daoumall.config.security.JWTUtil
import com.daou.project.daoumall.dto.CustomerDTO
import com.daou.project.daoumall.dto.customer.CustomerLoginDTO
import com.daou.project.daoumall.service.customer.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/daou/mall/customer")
@CrossOrigin
class CustomerController (
    val customerService: CustomerService)
{
    @GetMapping()
    private fun getCustomerInfo(@RequestParam customerLoginId: String): ResponseEntity<Any?> {
        val customerResDto = customerService.getCustomerInfo(customerLoginId)

        return ResponseEntity.ok().body(customerResDto)
    }

    @PostMapping("/create")
    private fun createCustomer(@RequestBody customerDTO: CustomerDTO): ResponseEntity<Any?> {
        val customerResDto = customerService.createCustomer(customerDTO.toEntity())

        return ResponseEntity.ok().body(customerResDto)
    }

    @PostMapping("/signup")
    private fun registerUser(@RequestParam customerDto: CustomerDTO): ResponseEntity<Any?> {
        val resDto = customerService.createCustomer(customerDto.toEntity())

        return ResponseEntity.ok().body(resDto)
    }

    @PostMapping("/signin")
    private fun authenticate(@RequestBody loginDto: CustomerLoginDTO) : ResponseEntity<Any?> {
        var successLoginStatusDto = customerService.login(loginDto)

        return ResponseEntity.ok().body(successLoginStatusDto)
    }

}