package com.daou.project.daoumall.service.coupon

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.coupondomain.AppliedCouponPaymentDTO
import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.dto.coupondomain.use.CanUseCouponDTO
import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.daou.project.daoumall.model.coupon.RateCoupon
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class CouponUseService(
    private val couponFindService: CouponFindService
) {
    data class AppliedRateCouponInfo(
        val discountPrice: Long,
        val upperBound: Long,
        val expiredDt: LocalDate,
        val serialCode: String,
        val rate: Long
    )

    data class AppliedAmountCouponInfo(
        val discountPrice: Long,
        val limitPrice: Long,
        val expiredDt: LocalDate,
        val serialCode: String
    )


    private fun canUsedAmountCouponsToAppliedAmountCouponInfos(
        canUsedAmountCoupons: Set<CanUseCouponDTO>,
        originalPrice: Long
    ): Set<AppliedAmountCouponInfo> {
        Log.info("사용자가 사용할 수 있는 쿠폰만을 추려냅니다.")

        var appliedAmountCouponsInfos = mutableSetOf<AppliedAmountCouponInfo>()
        val filteredConstraintAmountCoupons = canUsedAmountCoupons.filter {
            it.constraint <= originalPrice
        }

        for (canUsedAmountCoupon in filteredConstraintAmountCoupons) {
            appliedAmountCouponsInfos.add(
                AppliedAmountCouponInfo(
                    discountPrice = canUsedAmountCoupon.discountInfo,
                    limitPrice = canUsedAmountCoupon.constraint,
                    expiredDt = canUsedAmountCoupon.expiredDate,
                    serialCode = canUsedAmountCoupon.serialCode
                )
            )
        }

        return appliedAmountCouponsInfos
    }

    private fun canUsedRateCouponsToAppliedRateCouponInfos(
        canUsedRateCoupons: Set<CanUseCouponDTO>,
        originalPrice: Long
    ): Set<AppliedRateCouponInfo> {

        var appliedRateCouponInfos = mutableSetOf<AppliedRateCouponInfo>()

        for (canUseRateCoupon in canUsedRateCoupons) {
            var discountPrice = (originalPrice * canUseRateCoupon.discountInfo) / 100
            if (discountPrice >= canUseRateCoupon.constraint) discountPrice = canUseRateCoupon.constraint

            appliedRateCouponInfos.add(
                AppliedRateCouponInfo(
                    discountPrice = discountPrice,
                    expiredDt = canUseRateCoupon.expiredDate,
                    upperBound = canUseRateCoupon.constraint,
                    serialCode = canUseRateCoupon.serialCode,
                    rate = canUseRateCoupon.discountInfo
                )
            )
        }

        return appliedRateCouponInfos
    }

    @Transactional(readOnly = true)
    fun applyAutoCoupon(customerLoginId: String, mallName: String, originalPrice: Long): AppliedCouponPaymentDTO {
        val amountCoupon = couponFindService.findMostEfficientAmountCoupon(customerLoginId, mallName, originalPrice)
        val rateCoupon = couponFindService.findMostEfficientRateCoupon(customerLoginId, mallName, originalPrice)

        Log.info("가장 효율적인 금액 쿠폰 정보 : $amountCoupon")
        Log.info("가장 효율적인 비율 쿠폰 정보 : $rateCoupon")
        if(amountCoupon.discountAmount == 0L && rateCoupon.discountPrice == 0L) {
            return AppliedCouponPaymentDTO(
                originalPrice = originalPrice,
                couponSerialCode = "NONE",
                couponCATEGORY = CATEGORY.NONE,
            )
        }

        if(amountCoupon.discountAmount == rateCoupon.discountPrice) {
            if(amountCoupon.expiredDate == rateCoupon.expiredDt) {
                return amountCouponToPaymentDTO(
                    originalPrice = originalPrice,
                    amountCoupon = amountCoupon
                )
            } else if(amountCoupon.expiredDate > rateCoupon.expiredDt) {
                var appliedRateCoupon = AppliedCouponPaymentDTO(originalPrice = originalPrice,
                    couponSerialCode = rateCoupon.serialCode, couponCATEGORY = CATEGORY.RATE)
                appliedRateCoupon.discountPrice = rateCoupon.discountPrice
                return appliedRateCoupon
            }
        }

        if(amountCoupon.discountAmount > rateCoupon.discountPrice) {
           return amountCouponToPaymentDTO(
                originalPrice = originalPrice,
                amountCoupon = amountCoupon
            )
        } else {
            var appliedRateCoupon = AppliedCouponPaymentDTO(originalPrice = originalPrice,
            couponSerialCode = rateCoupon.serialCode, couponCATEGORY = CATEGORY.RATE)
            appliedRateCoupon.discountPrice = rateCoupon.discountPrice
            return appliedRateCoupon
        }
    }

    private fun getEfficientRateCoupon(appliedRateCouponInfos: Set<AppliedRateCouponInfo>): AppliedRateCouponInfo {
        return if (appliedRateCouponInfos.isNotEmpty()) {
            appliedRateCouponInfos.sortedWith(
                compareBy<AppliedRateCouponInfo>() { -it.discountPrice }
                    .thenBy { it.upperBound }
                    .thenBy { it.expiredDt })[0]
        } else {
            AppliedRateCouponInfo(
                discountPrice = 0,
                upperBound = 0,
                expiredDt = LocalDate.of(2080, 3, 15),
                serialCode = "can't use",
                rate = 0
            )
        }
    }

    private fun getEfficientAmountCoupon(appliedAmountCouponInfos: Set<AppliedAmountCouponInfo>): AppliedAmountCouponInfo {
        return if (appliedAmountCouponInfos.isNotEmpty()) {
            appliedAmountCouponInfos.sortedWith(
                compareBy<AppliedAmountCouponInfo>() { -it.discountPrice }
                    .thenBy { it.expiredDt }
                    .thenBy { it.limitPrice }
                    )[0]
        } else {
            AppliedAmountCouponInfo(
                discountPrice = 0,
                limitPrice = 0,
                expiredDt = LocalDate.of(1999, 9, 15),
                serialCode = "can't use"
            )
        }
    }

    private fun rateCouponToPaymentDTO(originalPrice: Long, rateCoupon: RateCoupon): AppliedCouponPaymentDTO {
        var appliedCouponDto = AppliedCouponPaymentDTO(
            originalPrice = originalPrice,
            couponSerialCode = rateCoupon.serialCode,
            couponCATEGORY = CATEGORY.RATE
        )
        appliedCouponDto.discountPrice = (originalPrice * rateCoupon.rate) / 100
        if(rateCoupon.upperBoundPrice <= originalPrice) {
            appliedCouponDto.discountPrice = rateCoupon.upperBoundPrice
        }

        return appliedCouponDto
    }

    private fun amountCouponToPaymentDTO(
        originalPrice: Long,
        amountCoupon: AmountCoupon
    ): AppliedCouponPaymentDTO {
        var appliedCouponDto = AppliedCouponPaymentDTO(
            originalPrice = originalPrice,
            couponSerialCode = amountCoupon.serialCode,
            couponCATEGORY = CATEGORY.AMOUNT
        )
        appliedCouponDto.discountPrice = amountCoupon.discountAmount

        return appliedCouponDto
    }
}