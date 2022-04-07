package com.daou.project.daoumall.model.coupon.connectionTable

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "customer_has_discount_coupon")
class CustomerAmountCoupon(
    val mallName: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_discount_coupon_id")
    val id: Long = 0

    // Customer 쪽
    @ManyToOne(
        targetEntity = Customer::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    var customer: Customer? = null

    @ManyToOne(
        targetEntity = AmountCoupon::class,
        fetch = FetchType.EAGER
    )
    @JoinColumn(name = "amount_coupon_id")
    var amountCoupon: AmountCoupon? = null

    @Enumerated(EnumType.STRING)
    @JsonProperty("used")
    var isUsed: USED = USED.NO

    var usedDate: LocalDateTime? = null

    override fun toString(): String = "CustomerAmountCoupon (amountCoupon=$amountCoupon, isUsed=$isUsed, usedDate=$usedDate)"
}

enum class USED {
    YES, NO
}
