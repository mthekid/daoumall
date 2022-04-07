package com.daou.project.daoumall.repository.paymentmanagement.point

import com.daou.project.daoumall.model.payment.PaymentUsePoint
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentUsePointRepository: JpaRepository<PaymentUsePoint, Long> {
    fun findAllByOrderSerialCode(orderSerialCode: String): List<PaymentUsePoint>
}