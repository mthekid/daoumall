package com.daou.project.daoumall.repository.paymentmanagement.method

import com.daou.project.daoumall.model.payment.method.Card
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository: JpaRepository<Card, Long> {
    fun findBySecretSerialInfo(SecretSerialInfo: String): Card?
}