package com.daou.project.daoumall.dto

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.GRADE

/*
고객과 관련된 정보를 처리한다.
반환 결과로는 사용자의 이름, 사용자의 등급, 사용자 로그인 아이디 정보이다.
 */
data class CustomerDTO(
    val email: String,
    val loginId: String,
    val userName: String,
    val password: String,
    val socialSecurityInfo: String,
) {
    data class CustomerDTOResult(
        val loginId: String,
        val email: String,
        val userName: String,
        val grade: GRADE,
    )

    data class AuthenticateCustomerResDTO(
        val email: String,
        val userName: String
    ) {
        var token: String = ""
    }

    fun toEntity(): Customer {
        return Customer(
            email = email,
            loginId = loginId,
            userName = userName,
            password = password,
            socialSecurityInfo = socialSecurityInfo
        )
    }

    companion object {
        fun fromEntity(customer: Customer): CustomerDTOResult {
            return CustomerDTOResult(
                email = customer.email,
                loginId = customer.loginId,
                userName = customer.userName,
                grade = customer.grade
            )
        }

        fun fromCustomerEntity(customer: Customer): AuthenticateCustomerResDTO {
            return AuthenticateCustomerResDTO(
                email = customer.email,
                userName = customer.userName
            )
        }
    }
}