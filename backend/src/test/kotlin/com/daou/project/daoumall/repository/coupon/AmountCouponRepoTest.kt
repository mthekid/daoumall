package com.daou.project.daoumall.repository.coupon

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.daou.project.daoumall.model.coupon.connectionTable.CustomerAmountCoupon
import com.daou.project.daoumall.repository.coupon.connectionTable.CustomerAmountCouponRepository
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
@DisplayName("금액 쿠폰 생성 테스트[REPO]")
internal class AmountCouponRepoTest @Autowired constructor(
    val customerRepo: CustomerRepository,
    val amountCouponRepo: AmountCouponRepository,
    val customerAmountCouponRepo: CustomerAmountCouponRepository
) {
    companion object {
        //test용 쿠폰 이름들
        val t_couponInfo = "봄 특별 할인"

        // test 용 customer 데이터 생성
        val t_email = "test@daou.co.krr"
        val t_loginId = "test login id"
        val t_name = "테스트 계정"
        val t_passwd = "qwer1234"
        val t_ssi = "1234qwer" // 보안 번호
        val t_customer = Customer(email = t_email, loginId = t_loginId, userName = t_name, password = t_passwd, socialSecurityInfo = t_ssi)

        // test 용 mall 이름
        var t_mallName = "testMall"

        // 1. 만료 기한을 넘긴 amount coupon
        val t1_amount: Long = 5000
        val t1_expiredDate = LocalDate.of(2021, 3, 17)
        val t1_limit: Long = 15000
        val t1_serialCode = "C-C-A1"
        val t1_amountCoupon = AmountCoupon(
            couponInfo = t_couponInfo,
            discountAmount = t1_amount,
            expiredDate = t1_expiredDate,
            limitPrice = t1_limit,
            mallName = t_mallName,
            serialCode = t1_serialCode
        )

        //2. 평범한 금액 쿠폰
        val t2_amount: Long = 10000
        val t2_expiredDate = LocalDate.of(2022, 10, 17)
        val t2_limit: Long = 22000
        val t2_serialCode = "C-C-A2"
        var t2_amountCoupon = AmountCoupon(
            couponInfo = t_couponInfo,
            discountAmount = t2_amount,
            expiredDate = t2_expiredDate,
            limitPrice = t2_limit,
            mallName = t_mallName,
            serialCode = t2_serialCode
        )
    }

    @Test
    internal fun `금액 쿠폰 2개 생성`() {
        val savedAmountCoupon1 = amountCouponRepo.save(t1_amountCoupon)
        val savedAmountCoupon2 = amountCouponRepo.save(t2_amountCoupon)

        val foundAmountCoupon1 = amountCouponRepo.findBySerialCode(t1_serialCode)
        val foundAmountCoupon2 = amountCouponRepo.findBySerialCode(t2_serialCode)

        assertThat(savedAmountCoupon1).isEqualTo(foundAmountCoupon1)
        assertThat(foundAmountCoupon2).isEqualTo(savedAmountCoupon2)
    }

    @Test
    internal fun `사용자 - 금액 쿠폰 등록 확인`() {
        val savedCustomer = customerRepo.save(t_customer)
        val savedAmountCoupon = amountCouponRepo.save(t1_amountCoupon)
        val contable = savedCustomer.customerAmountCoupons

        val customerAmounCouponCon = CustomerAmountCoupon(mallName = t_mallName)
        customerAmounCouponCon.amountCoupon = savedAmountCoupon
        customerAmounCouponCon.customer = savedCustomer

        contable.add(customerAmounCouponCon)
        assertThat(savedCustomer.customerAmountCoupons.contains(customerAmounCouponCon)).isEqualTo(true)

        val savedAmountCoupon2 = amountCouponRepo.save(t2_amountCoupon)
        val foundCustomer = customerRepo.findByLoginId(t_loginId)

        val customerAmountCouponCon2 = CustomerAmountCoupon(mallName = t_mallName)
        customerAmountCouponCon2.amountCoupon = savedAmountCoupon2
        customerAmountCouponCon2.customer = savedCustomer

        foundCustomer?.customerAmountCoupons!!.add(customerAmountCouponCon2)

        assertThat(foundCustomer.customerAmountCoupons.contains(customerAmountCouponCon2)).isEqualTo(true)
    }

}