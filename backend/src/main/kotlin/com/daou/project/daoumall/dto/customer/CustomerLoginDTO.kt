package com.daou.project.daoumall.dto.customer

data class CustomerLoginDTO(
    val loginId: String,
    val password: String
) {
    data class LoginStatusDTO(
        val loginId: String,
        val status : String
    )
}