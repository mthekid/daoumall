package com.daou.project.daoumall.repository.paymentmanagement

import com.daou.project.daoumall.model.payment.PaymentManagement
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentManagementRepository: JpaRepository<PaymentManagement, Long> {
    fun findByOrderSerialCode(orderSerialCode: String): PaymentManagement?
}