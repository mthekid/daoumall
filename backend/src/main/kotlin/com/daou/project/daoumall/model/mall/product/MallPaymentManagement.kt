package com.daou.project.daoumall.model.mall.product

import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.payment.STATUS
import java.time.LocalDateTime
import javax.persistence.*


@Entity
class MallPaymentManagement(
    val loginId : String,
    val totalPrice : Long,
    val discountAmount : Long,
    val usedPointAmount: Long,
    val couponSerialCode : String,

    @Column(unique = true)
    val orderSerialCode: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mall_payment_management_id")
    var id: Long? = 0

    val createDate : LocalDateTime = LocalDateTime.now()

    var modifiedDate : LocalDateTime = LocalDateTime.now()

    val payAmount : Long = (totalPrice - discountAmount - usedPointAmount)

    var orderStatus : STATUS = STATUS.PROGRESS

    @ManyToOne(targetEntity = Mall::class)
    @JoinColumn(name = "mall_id")
    var mall : Mall? = null

    override fun toString(): String = "id=$id, loginId=$loginId"
}