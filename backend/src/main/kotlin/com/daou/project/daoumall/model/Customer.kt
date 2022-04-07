package com.daou.project.daoumall.model

import com.daou.project.daoumall.model.coupon.connectionTable.CustomerAmountCoupon
import com.daou.project.daoumall.model.coupon.connectionTable.CustomerRateCoupon
import com.daou.project.daoumall.model.payment.PaymentManagement
import com.daou.project.daoumall.model.payment.method.BankAccount
import com.daou.project.daoumall.model.payment.method.Card
import com.daou.project.daoumall.model.point.connectionTable.CustomerPoint
import java.time.LocalDate
import javax.persistence.*

@Entity
class Customer(
    @Column(unique = true)
    val email: String,
    @Column(unique = true)
    val loginId: String,
    userName: String,
    password: String,
    @Column(unique = true)
    val socialSecurityInfo: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = 0
    val createDate: LocalDate = LocalDate.now()
    var userName = userName
    var password = password

    @Enumerated(EnumType.STRING)
    var grade: GRADE = GRADE.SILVER

    @OneToMany(
        targetEntity = CustomerAmountCoupon::class,
        mappedBy = "customer",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY)
    var customerAmountCoupons: MutableSet<CustomerAmountCoupon> = mutableSetOf()

    @OneToMany(
        targetEntity = CustomerRateCoupon::class,
        mappedBy = "customer",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY )
    var customerRateCoupons: MutableSet<CustomerRateCoupon> = mutableSetOf()

    @OneToMany(
        targetEntity = PaymentManagement::class,
        mappedBy = "customer",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY)
    @OrderBy(value = "create_date ASC")
    var paymentManagementInfos: MutableSet<PaymentManagement> = mutableSetOf()

    @OneToMany(
        targetEntity = CustomerPoint::class,
        mappedBy = "customer",
        fetch = FetchType.LAZY
    )
    @OrderBy(value = "customer_has_point_id ASC")
    var customerPoints: MutableSet<CustomerPoint> = mutableSetOf()

    @OneToMany(
        targetEntity = Card::class,
        mappedBy = "customer",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var cards: MutableSet<Card> = mutableSetOf()

    @OneToMany(
        targetEntity = BankAccount::class,
        mappedBy = "customer",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var bankAccounts: MutableSet<BankAccount> = mutableSetOf()

    override fun toString(): String = "Customer(userName=$userName, loginId=$loginId, createDate=$createDate, grade=$grade" +
            ", customerRateCoupons=$customerRateCoupons, customerAmountCoupons=$customerAmountCoupons" +
            ", paymentManagementInfos=$paymentManagementInfos)"

    fun toAbstractInfoString(): String = "Customer(userName=$userName, loginId=$loginId"

    fun toConnectCardString(): String = "Customer(userName=$userName, loginId=$loginId, cards=$cards"

    fun toConnectBankAccountString(): String = "Customer(userName=$userName, loginId=$loginId, bankAccounts=$bankAccounts"

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Customer
        if(id != other.id) return false

        return true
    }
}

enum class GRADE(val ratio: Int) {
    SILVER(5),
    GOLD(10),
    PLATINUM(15),
    DIAMOND(20);

    companion object {
        fun getRatio(grade: GRADE) =
            when (grade) {
                GRADE.SILVER -> 5
                GRADE.GOLD -> 10
                GRADE.PLATINUM -> 15
                GRADE.DIAMOND -> 20
            }
    }
}

enum class ROLE() {
    USER,
    ADMIN,
    SELLER,
    ALL,
}