package com.daou.project.daoumall.model.payment

import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.payment.connectionTable.OrderPaper
import com.daou.project.daoumall.model.point.Point
import org.hibernate.criterion.Order
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class PaymentManagement(
    val mallName: String,
    val totalPrice: Long,
    val usedPointAmount: Long,
    @Column(unique = true)
    val orderSerialCode: String,
    @Enumerated(EnumType.STRING)
    val paymentMethod: PAYMENTMEHTOD,

    val paymentSerialCode: String,
    val discountAmount: Long,
    @Enumerated(EnumType.STRING)
    val couponCategory: CATEGORY,
    val couponSerialCode: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_management_id")
    val id: Long? = 0
    val createDate: LocalDateTime = LocalDateTime.now()

    @Enumerated(EnumType.STRING)
    var status: STATUS = STATUS.PROGRESS
    val payAmount: Long = (totalPrice - discountAmount) - usedPointAmount

    var modifiedDate: LocalDate = LocalDate.now()

    @ManyToOne(
        targetEntity = Customer::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null

    @OneToOne(
        targetEntity = Point::class,
        fetch = FetchType.EAGER,
    )
    @JoinColumn(name = "point_id")
    var point: Point? = null

    @OneToMany(
        targetEntity = OrderPaper::class,
        fetch = FetchType.LAZY,
        mappedBy = "paymentManagement"
    )
    var orderPapers: MutableSet<OrderPaper> = mutableSetOf()

    override fun toString(): String = "PaymentManagement (orderSerialCode= $orderSerialCode, totalPrice=$totalPrice, payAmount=$payAmount" +
            ", paymentMethod=$paymentMethod, usedPointAmount=$usedPointAmount, discountAmount=$discountAmount" +
            ", paymentSerialCode=$paymentSerialCode, couponCategory=$couponCategory, couponSerialCode=$couponSerialCode" +
            ", status=$status, payAmount=$payAmount, lastModifiedDate=$modifiedDate, orderPapers=$orderPapers, customer=${customer!!.toAbstractInfoString()})"
}

enum class STATUS(info: String) {
    PROGRESS("진행 중..."), COMPLETE("구매 확정"), CANCEL("구매 취소")
}

enum class PAYMENTMEHTOD {
    CARD, BANKACCOUNT
}
