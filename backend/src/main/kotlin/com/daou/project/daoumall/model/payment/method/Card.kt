package com.daou.project.daoumall.model.payment.method

import com.daou.project.daoumall.model.Customer
import javax.persistence.*

@Entity
class Card(
    val company: String,
    @Column(unique = true)
    val secretSerialInfo: String, // 카드 정보
    moneyAmount: Long,
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val cardId: Long = 0

    var remainMoney: Long = if (moneyAmount > 0) moneyAmount else throw RuntimeException("금액은 마이너스가 될 수 없습니다.")

    @ManyToOne(
        targetEntity = Customer::class,
        fetch = FetchType.LAZY )
    var customer: Customer? = null

    fun pay(totalPrice: Long) {
        if(totalPrice > remainMoney) {
            throw OverRemainMoneyException("결제 금액이 남은 금액보다 더 많습니다.")
        }
        remainMoney -= totalPrice
    }

    fun payback(totalPrice: Long) {
        remainMoney += totalPrice
    }

    override fun toString(): String = "Card( remainMoney=$remainMoney, secretSerialInfo=$secretSerialInfo)"
}