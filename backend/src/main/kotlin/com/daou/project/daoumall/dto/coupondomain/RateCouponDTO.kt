package com.daou.project.daoumall.dto.coupondomain

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.model.coupon.RateCoupon
import java.time.LocalDate


data class RateCouponDTO(
    val mallName: String,
    val serialCode: String,
    val couponInfo: String,
    val rate: Long,
    val upperBoundPrice: Long,
    val expiredDate: LocalDate
) {
    data class RateCouponRESDTO(
        val mallName: String,
        val couponInfo: String,
        val expiredDate: LocalDate,
        val serialCode: String
    )

    companion object {
        fun fromEntity(rateCoupon: RateCoupon): RateCouponRESDTO {
            return RateCouponRESDTO(
                mallName = rateCoupon.mallName,
                couponInfo = rateCoupon.couponInfo,
                expiredDate = rateCoupon.expiredDate,
                serialCode = rateCoupon.serialCode
            )
        }
    }

    fun toEntity(): RateCoupon {
        if(expiredDate < LocalDate.now()) {
            throw BaseException(CustomResponseCode.EXPIRED_COUPON)
        }

        if(rate >= 100 || rate <= 0) throw BaseException(customResponseCode = CustomResponseCode.CANNOT_CREATE_RATECOUPON)

        return RateCoupon(
            mallName = mallName,
            serialCode = serialCode,
            couponInfo = couponInfo,
            rate = rate,
            upperBoundPrice = upperBoundPrice,
            expiredDate = expiredDate
        )
    }
}