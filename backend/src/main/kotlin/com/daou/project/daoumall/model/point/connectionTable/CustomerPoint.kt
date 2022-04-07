package com.daou.project.daoumall.model.point.connectionTable

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.point.Point
import javax.persistence.*

@Entity
@Table(name = "customer_has_point")
class CustomerPoint() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_has_point_id")
    val id: Long = 0

    @ManyToOne(
        targetEntity = Customer::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "customer_id", nullable = false)
    var customer: Customer? = null

    @ManyToOne(
        targetEntity = Point::class,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    @JoinColumn(name = "point_id", nullable = false)
    var point: Point? = null

    override fun toString(): String = "CustomerPoint( point=$point)"
}