package com.daou.project.daoumall.dto.paymentdomain

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.dto.coupondomain.use.UsedCouponDTO
import com.daou.project.daoumall.dto.malldomain.payment.MallPaymentManagementDTO
import com.daou.project.daoumall.dto.paymentdomain.method.BankAccountDTO
import com.daou.project.daoumall.model.payment.PAYMENTMEHTOD
import com.daou.project.daoumall.model.payment.PaymentManagement
import com.daou.project.daoumall.model.payment.STATUS
import java.time.LocalDateTime

data class PaymentManagementDTO(
    val mallName: String,
    val loginId: String,
    val totalPrice: Long,
    val usedPointAmount: Long,
    val orderSerialCode: String,
    val couponCategory: CATEGORY,
    val couponSerialCode: String,
    val paymentMethod: PAYMENTMEHTOD,
    val paymentSerialCode: String,
) {
    var orderStatus: STATUS = STATUS.PROGRESS
    var discountAmount: Long = 0
    var payPrice: Long = 0
    var createDate: LocalDateTime? = null

    data class PaymentManagementRESDTO(
        val mallName: String,
        val totalPrice: Long,
        val couponCategory: CATEGORY,
        val discountAmount: Long,
        val usedPointAmount: Long,
        val paymentMethod: PAYMENTMEHTOD
    ) {
        lateinit var customerLoginId: String
        lateinit var payInfo: BankAccountDTO.AfterPayRESDTO
        lateinit var couponInfo: UsedCouponDTO
        lateinit var buyProductsInfoDTOs: Set<OrderPaperDTO.BuyProductsInfoDTO>
        lateinit var mallPaymentManagement: MallPaymentManagementDTO
    }

    companion object {
        fun fromEntityAndPayInfoAndCouponInfoAndOrderPaperInfo(
            paymentManagement: PaymentManagement,
            payInfo: BankAccountDTO.AfterPayRESDTO,
            couponInfo: UsedCouponDTO,
            buyProductsInfoDTOs: Set<OrderPaperDTO.BuyProductsInfoDTO>
        ): PaymentManagementRESDTO {
            var res = PaymentManagementRESDTO(
                mallName = paymentManagement.mallName,
                totalPrice = paymentManagement.totalPrice,
                couponCategory = paymentManagement.couponCategory,
                discountAmount = paymentManagement.discountAmount,
                usedPointAmount = paymentManagement.usedPointAmount,
                paymentMethod = paymentManagement.paymentMethod,
            )
            res.payInfo = payInfo
            res.couponInfo = couponInfo
            res.buyProductsInfoDTOs = buyProductsInfoDTOs
            return res
        }

        fun fromPayManagementEntity(paymentManagement: PaymentManagement): PaymentManagementDTO {
            var res = PaymentManagementDTO(
                mallName = paymentManagement.mallName,
                totalPrice = paymentManagement.totalPrice,
                couponCategory = paymentManagement.couponCategory,
                usedPointAmount = paymentManagement.usedPointAmount,
                paymentMethod = paymentManagement.paymentMethod,
                orderSerialCode = paymentManagement.orderSerialCode,
                couponSerialCode = paymentManagement.couponSerialCode,
                paymentSerialCode = paymentManagement.paymentSerialCode,
                loginId = "Can't response",
            )
            res.orderStatus = paymentManagement.status
            res.discountAmount = paymentManagement.discountAmount
            res.payPrice = paymentManagement.totalPrice - ( paymentManagement.discountAmount + paymentManagement.usedPointAmount)
            res.createDate = paymentManagement.createDate
            return res
        }
    }

    fun toEntity(): PaymentManagement {
        if (totalPrice < discountAmount + usedPointAmount) {
            throw BaseException(CustomResponseCode.CANNOT_CREATE_MINUS_PAYMENT)
        }
        return PaymentManagement(
            mallName = mallName,
            totalPrice = totalPrice,
            discountAmount = discountAmount,
            usedPointAmount = usedPointAmount,
            orderSerialCode = orderSerialCode,
            couponCategory = couponCategory,
            couponSerialCode = couponSerialCode,
            paymentMethod = paymentMethod,
            paymentSerialCode = paymentSerialCode
        )
    }

    fun toMallPaymentManagementDto() : MallPaymentManagementDTO {
        return MallPaymentManagementDTO(
            mallName = mallName,
            totalPrice = totalPrice,
            discountAmount = discountAmount,
            usedPointAmount = usedPointAmount,
            orderSerialCode = orderSerialCode,
            couponSerialCode = couponSerialCode,
            loginId = loginId
        )
    }
}