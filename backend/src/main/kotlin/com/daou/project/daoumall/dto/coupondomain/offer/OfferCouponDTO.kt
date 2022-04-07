package com.daou.project.daoumall.dto.coupondomain.offer

import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.daou.project.daoumall.model.coupon.RateCoupon
import java.time.LocalDate

data class OfferCouponDTO(
    val customerLoginId: String,
    val couponSerialCode: String,
    val mallName: String,
) {
    data class OfferCouponRESDTO(
        val mallName : String,
        val customerLoginId: String,
        val couponCategory: CATEGORY,
        val expiredDate: LocalDate,
        val couponInfo: String
    )

    companion object {
        fun fromAmountEntity(customer: Customer, amountCoupon: AmountCoupon): OfferCouponRESDTO {
            return OfferCouponRESDTO(
                customerLoginId = customer.loginId,
                couponCategory = CATEGORY.AMOUNT,
                expiredDate = amountCoupon.expiredDate,
                couponInfo = amountCoupon.couponInfo,
                mallName = amountCoupon.mallName
            )
        }

        fun fromRateEntity(customer: Customer, rateCoupon: RateCoupon): OfferCouponRESDTO {
            return OfferCouponRESDTO(
                customerLoginId = customer.loginId,
                couponCategory = CATEGORY.RATE,
                expiredDate = rateCoupon.expiredDate,
                couponInfo = rateCoupon.couponInfo,
                mallName = rateCoupon.mallName
            )
        }
    }
}