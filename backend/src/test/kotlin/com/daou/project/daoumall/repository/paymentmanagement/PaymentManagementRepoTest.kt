package com.daou.project.daoumall.repository.paymentmanagement

import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.model.payment.PAYMENTMEHTOD
import com.daou.project.daoumall.model.payment.PaymentManagement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@DisplayName("결제 내역 추가 테스트 진행 [REPO]")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class PaymentManagementRepoTest @Autowired constructor(
    val paymentManagementRepo: PaymentManagementRepository
){
    companion object {
        val t_mallName: String = "testMall"
        val t_totalPrice: Long = 70000
        val t_discountAmount: Long = 10000
        val t_usedPointAmount: Long = 5000
        val t_orderSerialCode = "t-P-O-1"
        val t_couponCategory = CATEGORY.AMOUNT
        val t_couponSerialCode = "T-T-A1"
        val t_paymentMethod = PAYMENTMEHTOD.CARD
        val t_paymentSerialCode = "T-P-C1"
        lateinit var t_paymentManagement: PaymentManagement

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_paymentManagement = PaymentManagement(
                mallName = t_mallName,
                totalPrice = t_totalPrice,
                discountAmount = t_discountAmount,
                usedPointAmount = t_usedPointAmount,
                orderSerialCode = t_orderSerialCode,
                couponCategory = t_couponCategory,
                couponSerialCode = t_couponSerialCode,
                paymentMethod = t_paymentMethod,
                paymentSerialCode = t_paymentSerialCode
            )
        }
    }

    @Test
    internal fun `결제 내역 생성하고 확인하기`() {
        val savedPaymentManagement = paymentManagementRepo.save(t_paymentManagement)
        val foundPaymentManagement = paymentManagementRepo.findByOrderSerialCode(t_orderSerialCode)

        assertThat(savedPaymentManagement).isEqualTo(foundPaymentManagement)
    }

    @Test
    internal fun `결제내역 고유 번호로 결제 내역 찾기`() {
        val savedPaymentManagement = paymentManagementRepo.save(t_paymentManagement)
        val foundPaymentManagement = paymentManagementRepo.findByOrderSerialCode(t_orderSerialCode)

        assertThat(savedPaymentManagement).isEqualTo(foundPaymentManagement)
    }
}