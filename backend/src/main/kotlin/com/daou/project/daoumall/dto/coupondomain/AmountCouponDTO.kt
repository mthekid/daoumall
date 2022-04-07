package com.daou.project.daoumall.dto.coupondomain

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.model.coupon.AmountCoupon
import java.time.LocalDate

data class AmountCouponDTO(
    val mallName: String,
    val serialCode: String,
    val couponInfo: String,
    val discountAmount: Long,
    val limitPrice: Long,
    val expiredDate: LocalDate
) {
    data class AmountCouponRESDTO(
        val mallName: String,
        val couponInfo: String,
        val expiredDate: LocalDate,
        val serialCode: String,
    ) {
    }

    companion object {
        fun fromEntity(amountCoupon: AmountCoupon): AmountCouponRESDTO {
            return AmountCouponRESDTO(
                mallName = amountCoupon.mallName,
                couponInfo = amountCoupon.couponInfo,
                expiredDate = amountCoupon.expiredDate,
                serialCode = amountCoupon.serialCode
            )
        }
    }

    fun toEntity(): AmountCoupon {
        if(expiredDate < LocalDate.now())throw BaseException(CustomResponseCode.EXPIRED_COUPON)

        if(discountAmount > limitPrice) throw BaseException(CustomResponseCode.CANNOT_CREATE_AMOUNTCOUPON_UNDERDISCOUT)

        return AmountCoupon(
            mallName = mallName,
            couponInfo = couponInfo,
            serialCode = serialCode,
            discountAmount = discountAmount,
            limitPrice = limitPrice,
            expiredDate = expiredDate
        )
    }
}