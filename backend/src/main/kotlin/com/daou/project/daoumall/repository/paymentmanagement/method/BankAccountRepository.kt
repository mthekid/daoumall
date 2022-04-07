package com.daou.project.daoumall.repository.paymentmanagement.method

import com.daou.project.daoumall.model.payment.method.BankAccount
import org.springframework.data.jpa.repository.JpaRepository

interface BankAccountRepository: JpaRepository<BankAccount, Long> {
    fun findBySecretSerialInfo(findBySecretSerialInfo: String): BankAccount?
}