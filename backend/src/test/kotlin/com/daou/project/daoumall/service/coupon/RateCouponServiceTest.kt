package com.daou.project.daoumall.service.coupon

import com.daou.project.daoumall.dto.coupondomain.AppliedCouponPaymentDTO
import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.model.coupon.RateCoupon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("비율 쿠폰 사용 테스트 [Entity]")
internal class RateCouponServiceTest {
    companion object {
        // 테스트 판매점 이름
        val t_mallName = "daouMall"

        // 금액 쿠폰 생성 A
        val tA_couponInfo = "쿠폰 A"
        val tA_rate: Long = 5
        val tA_expiredDate = LocalDate.of(2022, 3, 15)
        val tA_upperBoundPrice: Long = 15_000
        val tA_serialCode = "T-D-A1"
        lateinit var tA_rateCoupon: RateCoupon

        // 금액 쿠폰 생성 B [ C와 같은 만료 기간 금액은 더 작음 ]
        val tB_couponInfo = "쿠폰 B"
        val tB_rate: Long = 10
        val tB_expiredDate = LocalDate.of(2022, 10, 22)
        val tB_upperBoundPrice: Long = 20_000
        val tB_serialCode = "T-D-A2"
        lateinit var tB_RateCoupon: RateCoupon

        // 금액 쿠폰 생성 C
        val tC_couponInfo = "쿠폰 C"
        val tC_rate: Long = 10
        val tC_expiredDate = LocalDate.of(2022, 10, 22)
        val tC_upperBoundPrice: Long = 30_000
        val tC_serialCode = "T-D-A3"
        lateinit var tC_RateCoupon: RateCoupon

        // 금액 쿠폰 생성 D [ C와 같은 할인 금액이지만 만료기간이 더 짧다 ]
        val tD_couponInfo = "쿠폰 D"
        val tD_rate: Long = 5
        val tD_expiredDate = LocalDate.of(2022, 5, 22)
        val tD_upperBoundPrice: Long = 15_000
        val tD_serialCode = "T-D-A4"
        lateinit var tD_RateCoupon: RateCoupon

        // 금액 쿠폰 생성 E [ D와 같은 할인 금액, 같은 만료기간 하지만 upperBoundPrice가 더 낮다 ]
        val tE_couponInfo = "쿠폰 E"
        val tE_rate: Long = 15
        val tE_expiredDate = LocalDate.of(2022, 5, 22)
        val tE_upperBoundPrice: Long = 20_000
        val tE_serialCode = "T-D-A5"
        lateinit var tE_RateCoupon: RateCoupon

        // 테스트 상품 금액
        val t_price: Long = 300_000

        lateinit var rateCoupons: MutableList<RateCoupon>

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            rateCoupons = mutableListOf()
            tA_rateCoupon = RateCoupon(
                couponInfo = tA_couponInfo,
                rate = tA_rate,
                expiredDate = tA_expiredDate,
                upperBoundPrice = tA_upperBoundPrice,
                mallName = t_mallName,
                serialCode = tA_serialCode
            )
            rateCoupons.add(tA_rateCoupon)

            tB_RateCoupon = RateCoupon(
                couponInfo = tB_couponInfo,
                rate = tB_rate,
                expiredDate = tB_expiredDate,
                upperBoundPrice = tB_upperBoundPrice,
                mallName = t_mallName,
                serialCode = tB_serialCode
            )
            rateCoupons.add(tB_RateCoupon)

            tC_RateCoupon = RateCoupon(
                couponInfo = tC_couponInfo,
                rate = tC_rate,
                expiredDate = tC_expiredDate,
                upperBoundPrice = tC_upperBoundPrice,
                mallName = t_mallName,
                serialCode = tC_serialCode
            )
            rateCoupons.add(tC_RateCoupon)

            tD_RateCoupon = RateCoupon(
                couponInfo = tD_couponInfo,
                rate = tD_rate,
                expiredDate = tD_expiredDate,
                upperBoundPrice = tD_upperBoundPrice,
                mallName = t_mallName,
                serialCode = tD_serialCode
            )
            rateCoupons.add(tD_RateCoupon)

            tE_RateCoupon = RateCoupon(
                couponInfo = tE_couponInfo,
                rate = tE_rate,
                expiredDate = tE_expiredDate,
                upperBoundPrice = tE_upperBoundPrice,
                mallName = t_mallName,
                serialCode = tE_serialCode
            )
            rateCoupons.add(tE_RateCoupon)
        }
    }

    @Test
    internal fun `금액이 주어진 경우 가장 큰 할인이 적용된 쿠폰 확인하기`() {
        var applyRateCouponInfos: MutableList<AppliedCouponPaymentDTO> = mutableListOf()

        for (rateCoupon in rateCoupons) {
            val applyRateCouponInfo =  AppliedCouponPaymentDTO(
                originalPrice = t_price,
                couponSerialCode = rateCoupon.serialCode,
                couponCATEGORY = CATEGORY.RATE
            )
            val limitUpperBoundPrice = rateCoupon.upperBoundPrice
            var discountPrice = (t_price * rateCoupon.rate) / 100
            if(discountPrice >= limitUpperBoundPrice) discountPrice = limitUpperBoundPrice
            applyRateCouponInfo.discountPrice = discountPrice

            applyRateCouponInfos.add(applyRateCouponInfo)
        }

        val sortedByDiscountAmountInfos = applyRateCouponInfos.sortedWith(
            compareBy<AppliedCouponPaymentDTO>() {-it.discountPrice}
        )

//        sortedByDiscountAmountInfos.forEach{
//            it -> println("할인 금액 ${it.discountPrice}, 적용한 쿠폰 정보 ${it.couponSerialCode}")
//        }

        assertThat(sortedByDiscountAmountInfos[0].couponSerialCode).isEqualTo(tC_RateCoupon.serialCode)
    }

    // 진행 중....
    @Test
    internal fun `할인 금액들이 같은 경우 만료 기간이 짧은 순으로 진행 만료기간이 같은 경우 rate가 적은 순으로 진행`() {
        val t2_price: Long = 5000

        var applyRateCouponInfos: MutableList<AppliedCouponPaymentDTO> = mutableListOf()

        for (rateCoupon in rateCoupons) {
            val applyRateCouponInfo =  AppliedCouponPaymentDTO(
                originalPrice = t2_price,
                couponSerialCode = rateCoupon.serialCode,
                couponCATEGORY = CATEGORY.RATE
            )

            val limitUpperBoundPrice = rateCoupon.upperBoundPrice
            var discountPrice = (t2_price * rateCoupon.rate) / 100
            if(discountPrice >= limitUpperBoundPrice) discountPrice = limitUpperBoundPrice
            applyRateCouponInfo.discountPrice = discountPrice

            applyRateCouponInfos.add(applyRateCouponInfo)
        }

        applyRateCouponInfos.removeAt(4)

        val sortedRateCouponInfos = applyRateCouponInfos.sortedWith(
            compareBy<AppliedCouponPaymentDTO> {-it.discountPrice }
        )
        val maxDisCountPrice = sortedRateCouponInfos[0].discountPrice

//        applyRateCouponInfos.forEach{
//                it -> println("할인 금액 ${it.discountPrice}, 적용한 쿠폰 정보 ${it.couponSerialCode}")
//        }

        val filteredRateCouponInfos = sortedRateCouponInfos.filter {
            it.discountPrice >= maxDisCountPrice
        }

        // 최대 할인 금액으로 필터링 하기.
//        println("최대 할인 금액으로 필터링 한 이후")
//        filteredRateCouponInfos.forEach{
//                it -> println("할인 금액 ${it.discountPrice}, 적용한 쿠폰 정보 ${it.couponSerialCode}")
//        }
    }
}