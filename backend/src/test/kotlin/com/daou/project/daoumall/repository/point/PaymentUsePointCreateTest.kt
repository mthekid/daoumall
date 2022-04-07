package com.daou.project.daoumall.repository.point

import com.daou.project.daoumall.model.payment.PaymentUsePoint
import com.daou.project.daoumall.model.point.Point
import com.daou.project.daoumall.repository.paymentmanagement.point.PaymentUsePointRepository
import com.daou.project.daoumall.repository.paymentmanagement.point.PointRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@DisplayName("포인트 사용 테스트 [REPO]")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class PaymentUsePointCreateTest @Autowired constructor(
    val paymentUsePointRepo: PaymentUsePointRepository,
    val pointRepo: PointRepository
) {

    companion object {
        // 테스트용 주문 번호
        val t_serialCode = "t-P-O1"

        // 테스트용 포인트 1
        val t_intialAmount: Long = 1_000
        lateinit var t_point: Point

        // 테스트용 포인트 2
        val t2_intialAmount: Long = 2_000
        lateinit var t2_point: Point

        // 테스트용 포인트 3
        val t3_intialAmount: Long = 2_000
        lateinit var t3_point: Point

        var t_points: MutableList<Point> = mutableListOf()
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_point = Point(initialAmount = t_intialAmount)
            t2_point = Point(initialAmount = t2_intialAmount)
            t3_point = Point(initialAmount = t3_intialAmount)
        }
    }

    @Test
    internal fun `포인트 적립 후 사용 테스트(잔여 포이트존재)`() {
        var usePointAmount: Long = 1500

        var savedPoint1 = pointRepo.save(t_point)
        var savedPoint2 = pointRepo.save(t2_point)
        var savedPoint3 = pointRepo.save(t3_point)

        t_points.add(savedPoint1)
        t_points.add(savedPoint2)
        t_points.add(savedPoint3)

        var usePointAmounts: MutableList<PaymentUsePoint> = mutableListOf()
        for(savedPoint in t_points) {
            if(usePointAmount >= savedPoint.remainAmount) {
                val paymentUsePoint = PaymentUsePoint(orderSerialCode = t_serialCode, usedPoint = savedPoint.remainAmount)
                usePointAmount -= savedPoint.remainAmount
                usePointAmounts.add(paymentUsePoint)
                savedPoint.remainAmount = 0
            } else {
                savedPoint.remainAmount -= usePointAmount
                val paymentUsePoint = PaymentUsePoint(orderSerialCode = t_serialCode, usedPoint = usePointAmount)
                usePointAmounts.add(paymentUsePoint)
                break;
            }
        }

        val foundPoint1 = pointRepo.findById(1).get()
        val foundPoint2 = pointRepo.findById(2).get()
        val foundPoint3 = pointRepo.findById(3).get()
        assertThat(foundPoint1.remainAmount).isEqualTo(0)
        assertThat(foundPoint2.remainAmount).isEqualTo(1500)
        assertThat(foundPoint3.remainAmount).isEqualTo(2000)

        println("contable 정보 $usePointAmounts")
    }
}