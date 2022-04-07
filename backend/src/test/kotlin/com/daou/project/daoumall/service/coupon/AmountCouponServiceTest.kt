package com.daou.project.daoumall.service.coupon

import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.daou.project.daoumall.repository.coupon.RateCouponRepoTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("금액 쿠폰 사용 테스트 [Entity]")
internal class AmountCouponServiceTest {
    companion object {
        // 테스트 판매점 이름
        val t_mallName = "daouMall"

        // 금액 쿠폰 생성 A
        val tA_couponInfo = "쿠폰 A"
        val tA_discountAmount: Long = 5000
        val tA_expiredDate = LocalDate.of(2022, 3, 15)
        val tA_limitPrice: Long = 15000
        val tA_serialCode = "T-D-A1"
        lateinit var tA_amountCoupon: AmountCoupon

        // 금액 쿠폰 생성 B [ C와 같은 만료 기간 금액은 더 작음 ]
        val tB_couponInfo = "쿠폰 B"
        val tB_discountAmount: Long = 4000
        val tB_expiredDate = LocalDate.of(2022, 10, 22)
        val tB_limitPrice: Long = 10000
        val tB_serialCode = "T-D-A2"
        lateinit var tB_amountCoupon: AmountCoupon

        // 금액 쿠폰 생성 C
        val tC_couponInfo = "쿠폰 C"
        val tC_discountAmount: Long = 8000
        val tC_expiredDate = LocalDate.of(2022, 10, 22)
        val tC_limitPrice: Long = 15000
        val tC_serialCode = "T-D-A3"
        lateinit var tC_amountCoupon: AmountCoupon

        // 금액 쿠폰 생성 D [ C와 같은 할인 금액이지만 만료기간이 더 짧다 ]
        val tD_couponInfo = "쿠폰 D"
        val tD_discountAmount: Long = 8000
        val tD_expiredDate = LocalDate.of(2022, 5, 22)
        val tD_limitPrice: Long = 10000
        val tD_serialCode = "T-D-A4"
        lateinit var tD_amountCoupon: AmountCoupon

        // 금액 쿠폰 생성 E [ D와 같은 할인 금액, 같은 만료기간 하지만 limitPrice가 더 낮다 ]
        val tE_couponInfo = "쿠폰 E"
        val tE_discountAmount: Long = 8000
        val tE_expiredDate = LocalDate.of(2022, 5, 22)
        val tE_limitPrice: Long = 9000
        val tE_serialCode = "T-D-A5"
        lateinit var tE_amountCoupon: AmountCoupon

        lateinit var amountCoupons : MutableList<AmountCoupon>
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            amountCoupons = mutableListOf()
            tA_amountCoupon = AmountCoupon(
                couponInfo = tA_couponInfo,
                discountAmount = tA_discountAmount,
                expiredDate = tA_expiredDate,
                limitPrice = tA_limitPrice,
                mallName = t_mallName,
                serialCode = tA_serialCode
            )
            amountCoupons.add(tA_amountCoupon)

            tB_amountCoupon = AmountCoupon(
                couponInfo = tB_couponInfo,
                discountAmount = tB_discountAmount,
                expiredDate = tB_expiredDate,
                limitPrice = tB_limitPrice,
                mallName = t_mallName,
                serialCode = tB_serialCode
            )
            amountCoupons.add(tB_amountCoupon)

            tC_amountCoupon = AmountCoupon(
                couponInfo = tC_couponInfo,
                discountAmount = tC_discountAmount,
                expiredDate = tC_expiredDate,
                limitPrice = tC_limitPrice,
                mallName = t_mallName,
                serialCode = tC_serialCode
            )
            amountCoupons.add(tC_amountCoupon)

            tD_amountCoupon = AmountCoupon(
                couponInfo = tD_couponInfo,
                discountAmount = tD_discountAmount,
                expiredDate = tD_expiredDate,
                limitPrice = tD_limitPrice,
                mallName = t_mallName,
                serialCode = tD_serialCode
            )
            amountCoupons.add(tD_amountCoupon)

            tE_amountCoupon = AmountCoupon(
                couponInfo = tE_couponInfo,
                discountAmount = tE_discountAmount,
                expiredDate = tE_expiredDate,
                limitPrice = tE_limitPrice,
                mallName = t_mallName,
                serialCode = tE_serialCode
            )
            amountCoupons.add(tE_amountCoupon)
        }
    }
    @Test
    internal fun `한정된 금액을 필터링한 쿠폰 결과 확인하기`() {

        val limitPriceFilteredAmountCoupons = amountCoupons.filter {
            it.limitPrice <= 9000
        }

        assertThat(limitPriceFilteredAmountCoupons[0]).isEqualTo(tE_amountCoupon)
    }

    @Test
    internal fun `할인 금액 - 만료기한 - limitPrice로 정렬한 쿠폰 가져오기`() {
        val sortedByAmountPriceAndexpiredDateAndLimitPriceAmountCoupons = amountCoupons.sortedWith(
            compareBy<AmountCoupon>() { -it.discountAmount }
                .thenBy { it.expiredDate }
                .thenBy { it.limitPrice }
        )
        assertThat(sortedByAmountPriceAndexpiredDateAndLimitPriceAmountCoupons[0]).isEqualTo(tE_amountCoupon)
    }

    @Test
    internal fun `특정 금액 이상에서 적용할 수 있는 쿠폰 중 가장 효율 적인 쿠폰 가져오기`() {
        val limitPriceFilteredAmountCoupons = amountCoupons.filter {
            it.limitPrice >= 15000
        }

        val bestEfficiencyCoupon = limitPriceFilteredAmountCoupons.sortedWith(
            compareBy<AmountCoupon> {-it.discountAmount}
                .thenBy { it.expiredDate }
                .thenBy { it.limitPrice }
        ).get(0)

        assertThat(bestEfficiencyCoupon).isEqualTo(tC_amountCoupon)
    }

}