package com.daou.project.daoumall.dto.coupondomain.use

import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.daou.project.daoumall.model.coupon.RateCoupon
import com.daou.project.daoumall.model.coupon.connectionTable.USED
import java.time.LocalDate
import java.time.LocalDateTime

data class CanUseCouponDTO(
    val discountInfo: Long,
    val expiredDate: LocalDate,
    val createDate: LocalDateTime,
    val constraint: Long,
    val mallName: String,
    val serialCode: String,
    val couponCategory: CATEGORY
) {

    companion object {
        fun fromAmountCouponEntity(amountCoupon: AmountCoupon): CanUseCouponDTO {
            return CanUseCouponDTO(
                discountInfo = amountCoupon.discountAmount,
                expiredDate = amountCoupon.expiredDate,
                createDate = amountCoupon.createDate,
                constraint = amountCoupon.limitPrice,
                mallName = amountCoupon.mallName,
                serialCode = amountCoupon.serialCode,
                couponCategory = CATEGORY.AMOUNT
            )
        }

        fun fromRateCouponEntity(rateCoupon: RateCoupon): CanUseCouponDTO {
            return CanUseCouponDTO(
                discountInfo = rateCoupon.rate,
                expiredDate = rateCoupon.expiredDate,
                createDate = rateCoupon.createDate,
                constraint = rateCoupon.upperBoundPrice,
                mallName = rateCoupon.mallName,
                serialCode = rateCoupon.serialCode,
                couponCategory = CATEGORY.RATE
            )
        }
    }
}

enum class CATEGORY() {
    AMOUNT, RATE, NONE
}
