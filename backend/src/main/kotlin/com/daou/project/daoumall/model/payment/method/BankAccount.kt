package com.daou.project.daoumall.model.payment.method

import com.daou.project.daoumall.model.Customer
import org.hibernate.annotations.GeneratorType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class BankAccount(
    val company: String,
    @Column(unique = true)
    val secretSerialInfo: String,
    moneyAmount: Long
) {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val bankAccountId: Long = 0
    var remainMoney: Long = if(moneyAmount > 0) moneyAmount else throw RuntimeException("-금액이 담긴 불량 은행 계좌를 만들수 없습니다.")

    @ManyToOne(
        targetEntity = Customer::class,
        fetch = FetchType.LAZY
    )
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
}