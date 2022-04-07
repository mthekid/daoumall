package com.daou.project.daoumall.model.coupon.connectionTable

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.coupon.RateCoupon
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "customer_has_rate_coupon")
class CustomerRateCoupon(
    val mallName: String
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_rate_coupon_id")
    val id: Long = 0

    @ManyToOne(
        targetEntity = Customer::class,
        fetch = FetchType.LAZY )
    var customer: Customer? = null

    @ManyToOne(
        targetEntity = RateCoupon::class,
        fetch = FetchType.EAGER )
    var rateCoupon: RateCoupon? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "is_used")
    @JsonProperty("used")
    var isUsed: USED = USED.NO

    var usedDate: LocalDateTime? = null

    override fun toString(): String = "CustomerRateCoupon con table info (rateCoupon=$rateCoupon, isUsed=$isUsed, usedDate=$usedDate)"
}