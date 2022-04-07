package com.daou.project.daoumall.model.coupon

import com.daou.project.daoumall.model.coupon.connectionTable.CustomerRateCoupon
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class RateCoupon(
    val couponInfo: String,
    val rate: Long,
    val expiredDate: LocalDate,
    val upperBoundPrice: Long = 0,
    val mallName: String,
    @Column(unique = true)
    val serialCode: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_coupon_id")
    val id: Long = 0

    val createDate: LocalDateTime = LocalDateTime.now()

    @OneToMany(
        targetEntity = CustomerRateCoupon::class,
        mappedBy = "rateCoupon",
        cascade = [CascadeType.ALL])
    @JsonIgnore
    var customerRateCoupons: MutableSet<CustomerRateCoupon> = mutableSetOf()

    override fun toString(): String = "RateCoupon (couponInfo=$couponInfo, rate=$rate, mallName=$mallName, upperBoundPrice=$upperBoundPrice" +
            ", createDate=$createDate, expiredDate=$expiredDate"
}
