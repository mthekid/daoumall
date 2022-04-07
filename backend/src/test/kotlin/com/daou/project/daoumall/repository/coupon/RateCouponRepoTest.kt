package com.daou.project.daoumall.repository.coupon

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.coupon.RateCoupon
import com.daou.project.daoumall.model.coupon.connectionTable.CustomerRateCoupon
import com.daou.project.daoumall.repository.coupon.connectionTable.CustomerRateCouponRepository
import com.daou.project.daoumall.repository.customer.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDate

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("할인 쿠폰 생성 테스트[REPO]")
internal class RateCouponRepoTest @Autowired constructor(
    val customerRepo: CustomerRepository,
    val rateCouponRepo: RateCouponRepository,
    val customerRateCouponRepo: CustomerRateCouponRepository
) {

    companion object {

        val t_couponInfo = "봄 특별 할인"

        // test 용 customer 데이터 생성
        val t_email = "test@daou.co.krr"
        val t_loginId = "test login id"
        val t_name = "테스트 계정"
        val t_passwd = "qwer1234"
        val t_ssi = "1234qwer" // 보안 번호
        lateinit var t_customer: Customer

        // test 용 mall 이름
        var t_mallName = "testMall"

        // 1. 만료 기한을 넘긴 rate coupon
        val t1_rate: Long = 10
        val t1_expiredDate = LocalDate.of(2021, 3, 17)
        val t1_upperBoundPrice: Long = 50000
        val t1_serialCode = "C-T-R1"
        lateinit var t1_rateCoupon: RateCoupon

        //2. 할인 금액이 & upperbound 금액을 넘긴 쿠폰
        val t2_rate: Long = 10
        val t2_expiredDate = LocalDate.of(2022, 10, 17)
        val t2_upperBoundPrice: Long = 5000
        val t2_serialCode = "C-T-R2"
        lateinit var t2_rateCoupon: RateCoupon

        //3. 평범한 쿠폰
        val t3_rate: Long = 5
        val t3_expiredDate = LocalDate.of(2022, 10, 17)
        val t3_upperBoundPrice: Long = 50000
        val t3_serialCode = "C-T-R3"
        lateinit var t3_rateCoupon: RateCoupon

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_customer = Customer(email = t_email, loginId = t_loginId, userName = t_name, password = t_passwd, socialSecurityInfo = t_ssi)
            t1_rateCoupon = RateCoupon(
                couponInfo = t_couponInfo,
                rate = t1_rate,
                expiredDate = t1_expiredDate,
                upperBoundPrice = t1_upperBoundPrice,
                mallName = t_mallName,
                serialCode = t1_serialCode
            )
            t2_rateCoupon = RateCoupon(
                couponInfo = t_couponInfo,
                rate = t2_rate,
                expiredDate = t2_expiredDate,
                upperBoundPrice = t2_upperBoundPrice,
                mallName = t_mallName,
                serialCode = t2_serialCode
            )
            t3_rateCoupon = RateCoupon(
                couponInfo = t_couponInfo,
                rate = t3_rate,
                expiredDate = t3_expiredDate,
                upperBoundPrice = t3_upperBoundPrice,
                mallName = t_mallName,
                serialCode = t3_serialCode
            )
        }
    }

    @Test
    internal fun `비율 쿠폰 3개 생성`() {
        val savedRateCoupon1 = rateCouponRepo.save(t1_rateCoupon)
        val savedRateCoupon2 = rateCouponRepo.save(t2_rateCoupon)
        val savedRateCoupon3 = rateCouponRepo.save(t3_rateCoupon)

        val foundRateCoupon1 = rateCouponRepo.findBySerialCode(t1_serialCode)
        val foundRateCoupon2 = rateCouponRepo.findBySerialCode(t2_serialCode)
        val foundRateCoupon3 = rateCouponRepo.findBySerialCode(t3_serialCode)

        // 비율 쿠폰 생성 비교
        assertThat(savedRateCoupon1).isEqualTo(foundRateCoupon1)
        assertThat(savedRateCoupon2).isEqualTo(foundRateCoupon2)
        assertThat(savedRateCoupon3).isEqualTo(foundRateCoupon3)
    }

    @Test
    internal fun `사용자 - 금액 쿠폰 등록 확인`() {
        // 1. 고객 & 쿠폰 & contable 정보 가져오기.
        val savedCustomer = customerRepo.save(t_customer)
        val savedRateCoupon = rateCouponRepo.save(t1_rateCoupon)
        val contable = savedCustomer.customerRateCoupons

        val customerRateCoupon = CustomerRateCoupon(mallName = AmountCouponRepoTest.t_mallName)
        customerRateCoupon.rateCoupon = savedRateCoupon
        customerRateCoupon.customer = savedCustomer

        contable.add(customerRateCoupon)

        val afterAddRateCouponCustomer = customerRepo.save(savedCustomer)

        val savedRateCoupon2 = rateCouponRepo.save(t2_rateCoupon)
        val foundCustomer = customerRepo.findByLoginId(t_loginId)

        val customerRateCoupon2 = CustomerRateCoupon(mallName = AmountCouponRepoTest.t_mallName)
        customerRateCoupon2.rateCoupon = savedRateCoupon2
        customerRateCoupon2.customer = savedCustomer

        foundCustomer?.customerRateCoupons!!.add(customerRateCoupon2)

        val afterAddRateCouponCustomer2 = customerRepo.save(foundCustomer)
    }
}