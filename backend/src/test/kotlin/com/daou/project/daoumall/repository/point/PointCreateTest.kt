package com.daou.project.daoumall.repository.point

import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.payment.PAYMENTMEHTOD
import com.daou.project.daoumall.model.payment.PaymentManagement
import com.daou.project.daoumall.model.point.Point
import com.daou.project.daoumall.model.point.connectionTable.CustomerPoint
import com.daou.project.daoumall.repository.customer.CustomerRepository
import com.daou.project.daoumall.repository.paymentmanagement.OrderPaperRepoTest
import com.daou.project.daoumall.repository.paymentmanagement.PaymentManagementRepository
import com.daou.project.daoumall.repository.paymentmanagement.connectiontable.CustomerPointRepository
import com.daou.project.daoumall.repository.paymentmanagement.point.PointRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@DisplayName("포인트 적립 테스트 [REPO]")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class PointCreateTest @Autowired constructor(
    val customerRepo: CustomerRepository,
    val pointRepo: PointRepository,
    val customerPointRepo: CustomerPointRepository,
    val paymentManagementRepo: PaymentManagementRepository
) {
    companion object {
        // 테스트 사용자 정보
        val t_email = "test@daou.co.krr"
        val t_loginId = "test_point_loginId"
        val t_name = "문현준"
        val t_passwd = "qwer1234"
        val t_ssi = "t_p_qwer1234"
        lateinit var t_customer: Customer

        // 테스트 포인트 정보 1
        val t1_Amount: Long = 5000
        lateinit var t_point: Point

        // 테스트 포인트 정보 2
        val t2_Amount: Long = 10000
        lateinit var t2_point: Point

        // 연결 테이블
        lateinit var t_customerPoint: CustomerPoint

        // 테스트 결제 내역
        val t_mallName: String = "testMall"
        val t_totalPrice: Long = 70000
        val t_discountAmount: Long = 15000
        val t_usedPointAmount: Long = 20000
        val t_orderSerialCode = "T-T_01"
        val t_couponCategory = CATEGORY.NONE
        val t_couponSerialCode = "T-T-COUPON1"
        val t_paymentMethod = PAYMENTMEHTOD.CARD
        val t_paymentSerialCode = "T-T-CARD1"
        lateinit var t_paymentManagement: PaymentManagement

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_customer = Customer(
                email = t_email,
                loginId = t_loginId,
                userName = t_name,
                password = t_passwd,
                socialSecurityInfo = t_ssi)

            t_point = Point(initialAmount = t1_Amount)
            t2_point = Point(initialAmount = t2_Amount)
            t_customerPoint = CustomerPoint()

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
    internal fun `사용자-contable-포인트 연결 테스트`() {
        val savedPoint = pointRepo.save(t_point)
        val savedCustomer = customerRepo.save(t_customer)

        t_customerPoint.point = savedPoint
        t_customerPoint.customer = savedCustomer
        val savedCustomerPoint = customerPointRepo.save(t_customerPoint)

        assertThat(savedPoint).isEqualTo(savedCustomerPoint.point)
        assertThat(savedCustomer).isEqualTo(savedCustomerPoint.customer)

        savedCustomer.customerPoints.add(savedCustomerPoint)

        val foundCustomer = customerRepo.findByLoginId(t_loginId)
        assertThat(foundCustomer!!.customerPoints.contains(savedCustomerPoint)).isEqualTo(true)
    }

    @Test
    internal fun `포인트-결제내역 연결 테스트`() {
        val savedPoint = pointRepo.save(t_point)
        val savedPaymentManagement = paymentManagementRepo.save(t_paymentManagement)

        savedPaymentManagement.point = savedPoint

        val foundPaymentManagement = paymentManagementRepo.findByOrderSerialCode(t_orderSerialCode)
        assertThat(foundPaymentManagement!!.point).isEqualTo(savedPoint)
    }
}