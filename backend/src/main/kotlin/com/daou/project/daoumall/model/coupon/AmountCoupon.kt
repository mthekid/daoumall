package com.daou.project.daoumall.model.coupon

import com.daou.project.daoumall.model.coupon.connectionTable.CustomerAmountCoupon
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class AmountCoupon(
    @Column(name = "coupon_info")
    val couponInfo: String,

    @Column(name = "discount_amount")
    val discountAmount: Long,

    val expiredDate: LocalDate,

    @Column(name = "limit_price", nullable = false)
    val limitPrice: Long = 0,

    @Column(name = "mall_name", nullable = false)
    val mallName: String,

    @Column(name = "serial_code", nullable = false, unique = true)
    val serialCode: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amount_coupon_id")
    val id: Long = 0

    val createDate: LocalDateTime = LocalDateTime.now()

    @OneToMany(
        targetEntity = CustomerAmountCoupon::class,
        mappedBy = "amountCoupon",
        cascade = [CascadeType.ALL])
    @JsonIgnore
    var customerAmountCoupon: MutableSet<CustomerAmountCoupon> = mutableSetOf()

    override fun toString(): String = "AmountCoupon (couponInfo=$couponInfo, discountAmount=$discountAmount, mallName=$mallName, limitPrice=$limitPrice" +
            ", createDate=$createDate, expiredDate=$expiredDate"
}
