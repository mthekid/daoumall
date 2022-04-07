package com.daou.project.daoumall.repository.customer

import com.daou.project.daoumall.model.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository: JpaRepository<Customer, Long> {
    fun findBySocialSecurityInfo(socialSecurityInfo: String): Customer?
    fun findByLoginId(loginId: String): Customer?
    fun findByEmail(email: String): Customer?
}