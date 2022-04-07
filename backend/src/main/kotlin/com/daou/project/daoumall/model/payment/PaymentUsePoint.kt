package com.daou.project.daoumall.model.payment

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.point.Point
import java.time.LocalDate
import javax.persistence.*

@Entity
class PaymentUsePoint(
    @Column(nullable = false)
    val orderSerialCode: String,
    var usedPoint: Long
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_use_point_id")
    val id: Long? = 0

    @ManyToOne(
        targetEntity = Point::class
    )
    @JoinColumn(name = "point_id", nullable = false)
    var point: Point? = null

    val createdDate : LocalDate = LocalDate.now()

    override fun toString(): String ="PaymentUsePoint (orderSerialCode=$orderSerialCode, usedPoint=$usedPoint)"
}