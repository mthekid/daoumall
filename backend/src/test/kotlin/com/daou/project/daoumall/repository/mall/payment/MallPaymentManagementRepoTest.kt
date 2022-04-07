package com.daou.project.daoumall.repository.mall.payment

import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.mall.product.MallPaymentManagement
import com.daou.project.daoumall.repository.mall.MallPaymentManagementRepository
import com.daou.project.daoumall.repository.mall.MallRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@DisplayName("판매점 결제 등록을 진행합니다. [REPO]")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class MallPaymentManagementRepoTest @Autowired constructor(
    val mallRepo: MallRepository,
    val mallPaymentManagementRepo: MallPaymentManagementRepository
) {

    companion object {

        // 테스트 판매점
        val t_mallName = "testMall"
        val t_adminName = "testAdmin"
        lateinit var t_mall: Mall

        // 테스트 결제 내역 1 [판매점 전용]
        val t_customerEmail : String = "test_email"
        val t_totalPrice : Long = 700000
        val t_discountAmount : Long = 5000
        val t_usedPointAmount: Long = 7000
        val t_couponSerialCode : String = "t_coupon_serialCode"
        val t_orderSerialCode: String = "t_orderSerialCode"
        lateinit var t_mallPaymentManagement: MallPaymentManagement

        // 테스트 결제 내역 2 [판매점 전용]
        val t2_customerEmail : String = "test_email2"
        val t2_totalPrice : Long = 60000
        val t2_discountAmount : Long = 10000
        val t2_usedPointAmount: Long = 6000
        val t2_couponSerialCode : String = "t2_coupon_serialCode"
        val t2_orderSerialCode: String = "t2_orderSerialCode"
        lateinit var t2_mallPaymentManagement: MallPaymentManagement

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_mall = Mall(mallName = t_mallName, adminName = t_adminName)
            t_mallPaymentManagement = MallPaymentManagement(
                loginId = t_customerEmail,
                totalPrice = t_totalPrice,
                discountAmount = t_discountAmount,
                usedPointAmount = t_usedPointAmount,
                couponSerialCode = t_couponSerialCode,
                orderSerialCode = t_orderSerialCode
            )
            t2_mallPaymentManagement = MallPaymentManagement(
                loginId = t2_customerEmail,
                totalPrice = t2_totalPrice,
                discountAmount = t2_discountAmount,
                usedPointAmount = t2_usedPointAmount,
                couponSerialCode = t2_couponSerialCode,
                orderSerialCode = t2_orderSerialCode
            )
        }
    }
    @Test
    internal fun `판매점 생성 - 결제 내역 등록 테스트`() {
        val savedMall = mallRepo.save(t_mall)
        val savedMallPaymentManagement = mallPaymentManagementRepo.save(t_mallPaymentManagement)

        savedMallPaymentManagement.mall = savedMall

        assertThat(savedMallPaymentManagement.mall).isEqualTo(savedMall)

        val savedMallPaymentManagement2 = mallPaymentManagementRepo.save(t2_mallPaymentManagement)
        savedMallPaymentManagement2.mall = savedMall

        val foundMallPayment1 = mallPaymentManagementRepo.findByOrderSerialCode(t_orderSerialCode)
        val foundMallPayment2 = mallPaymentManagementRepo.findByOrderSerialCode(t2_orderSerialCode)

        assertThat(foundMallPayment1.mall).isEqualTo(foundMallPayment2.mall)
    }

    @Test
    internal fun `판매점 생성 - 결제내역 추가 테스트`() {
        val savedMall = mallRepo.save(t_mall)

        val savedMallPaymentManagement = mallPaymentManagementRepo.save(t_mallPaymentManagement)
        savedMall.mallPaymentManagements.add(savedMallPaymentManagement)

        val foundMall = mallRepo.findByMallName(t_mallName)
        savedMallPaymentManagement.mall = foundMall

        assertThat(savedMallPaymentManagement.mall).isEqualTo(savedMall)
    }
}