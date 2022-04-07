package com.daou.project.daoumall.dto.coupondomain.use

import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.daou.project.daoumall.model.coupon.RateCoupon
import java.time.LocalDate
import java.time.LocalDateTime

data class UsedCouponDTO(
    val discountInfo: Long,
    val createDate: LocalDateTime?,
    val constraint: Long,
    val serialCode: String,
    val couponCategory: CATEGORY
) {
    var usedDate: LocalDateTime? = null

    data class CanceledCouponDTO(
        val serialCode: String,
        val category: CATEGORY,
    )

    companion object {
        fun fromAmountCouponEntityToCancledCouponDTO(amountCoupon: AmountCoupon): CanceledCouponDTO {
            return CanceledCouponDTO(
                serialCode = amountCoupon.serialCode,
                category = CATEGORY.AMOUNT
            )
        }
        fun fromAmountCouponEntityToCancledCouponDTO(rateCoupon: RateCoupon): CanceledCouponDTO {
            return CanceledCouponDTO(
                serialCode = rateCoupon.serialCode,
                category = CATEGORY.RATE
            )
        }

        fun fromAmountCouponEntity(amountCoupon: AmountCoupon, usedDate: LocalDateTime): UsedCouponDTO {
            var usedCouponDto = UsedCouponDTO(
                discountInfo = amountCoupon.discountAmount,
                createDate = amountCoupon.createDate,
                constraint = amountCoupon.limitPrice,
                serialCode = amountCoupon.serialCode,
                couponCategory = CATEGORY.AMOUNT
            )
            usedCouponDto.usedDate = usedDate
            return usedCouponDto
        }

        fun fromRateCouponEntity(rateCoupon: RateCoupon, usedDate: LocalDateTime): UsedCouponDTO {
            var usedCouponDto = UsedCouponDTO(
                discountInfo = rateCoupon.rate,
                createDate = rateCoupon.createDate,
                constraint = rateCoupon.upperBoundPrice,
                serialCode = rateCoupon.serialCode,
                couponCategory = CATEGORY.RATE
            )
            usedCouponDto.usedDate = usedDate
            return usedCouponDto
        }
    }
}