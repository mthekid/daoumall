package com.daou.project.daoumall.dto.malldomain.payment

import com.daou.project.daoumall.model.mall.product.MallPaymentManagement
import com.daou.project.daoumall.model.payment.STATUS
import java.time.LocalDateTime

class MallPaymentManagementDTO(
    val mallName: String,
    val totalPrice: Long,
    val discountAmount: Long,
    val usedPointAmount: Long,
    val couponSerialCode: String,
    val orderSerialCode: String,
    val loginId: String
) {
    var payAmount: Long = 0
    var createDate: LocalDateTime? = null
    var orderStatus : STATUS = STATUS.PROGRESS

    companion object {
        fun fromEntity(mallPaymentManagement: MallPaymentManagement): MallPaymentManagementDTO {
            return MallPaymentManagementDTO(
                mallName = mallPaymentManagement.mall!!.mallName,
                totalPrice = mallPaymentManagement.totalPrice,
                discountAmount = mallPaymentManagement.discountAmount,
                usedPointAmount = mallPaymentManagement.usedPointAmount,
                couponSerialCode = mallPaymentManagement.couponSerialCode,
                orderSerialCode = mallPaymentManagement.orderSerialCode,
                loginId = mallPaymentManagement.loginId
            )
        }
    }

    fun toEntity(): MallPaymentManagement {
        return MallPaymentManagement(
            loginId = loginId,
            totalPrice = totalPrice,
            discountAmount = discountAmount,
            usedPointAmount = usedPointAmount,
            couponSerialCode = couponSerialCode,
            orderSerialCode = orderSerialCode
        )
    }
}