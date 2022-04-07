package com.daou.project.daoumall.model.point

import com.daou.project.daoumall.model.payment.PaymentManagement
import com.daou.project.daoumall.model.payment.PaymentUsePoint
import com.daou.project.daoumall.model.point.connectionTable.CustomerPoint
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "points")
class Point(
    val initialAmount: Long,
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    val id: Long? = 0

    var expiredDateTime: LocalDateTime = LocalDateTime.now().plusYears(10)
    val createDate: LocalDate = LocalDate.now()
    var remainAmount: Long = initialAmount

    @OneToMany(
        targetEntity = CustomerPoint::class,
        mappedBy = "point")
    var customerPoint: MutableSet<CustomerPoint> = mutableSetOf()

    @OneToOne(
        targetEntity = PaymentManagement::class,
        mappedBy = "point")
    var paymentManagement: PaymentManagement? = null

    @OneToMany(
        targetEntity = PaymentUsePoint::class,
        mappedBy = "point",
        cascade = [CascadeType.ALL]
    )
    var paymentUsePoint: MutableSet<PaymentUsePoint> = mutableSetOf()

    override fun toString(): String = "Point (initialAmount=$initialAmount, remainAmount=$remainAmount" +
            ", createDate=$createDate, expiredDateTime=$expiredDateTime"
}