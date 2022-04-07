package com.daou.project.daoumall.dto.paymentdomain.method

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.payment.method.BankAccount
import com.daou.project.daoumall.model.payment.method.Card

data class CardDTO(
    val customerLoginId: String,
    val cardCompany: String,
    val remainMoney: Long,
    val cardSecretSerialInfo: String
) {

    data class CardRESDTO(
        val customerLoginId: String,
        val cardCompany: String,
        val comment: String
    )

    companion object {
        fun fromCustomerEntityAndCardEntity(customer: Customer, card: Card): CardRESDTO {
            return CardRESDTO(
                customerLoginId = customer.loginId,
                cardCompany = card.company,
                comment = "${customer.userName}에 카드를 성공적으로 연결했습니다."
            )
        }

        fun afterPay(customer: Customer, card: Card, totalPrice: Long): BankAccountDTO.AfterPayRESDTO {
            return BankAccountDTO.AfterPayRESDTO(
                company = card.company,
                totalPrice = totalPrice,
                remainPrice = card.remainMoney,
                comment = "${customer.userName}의 ${card.company}의 회사에서 ${totalPrice}금액 결제가 완료되었습니다. 남은 금액 : ${card.remainMoney}"
            )
        }
    }

    fun toCardEntity(): Card {
        if(remainMoney < 0) throw BaseException(CustomResponseCode.CANNOT_CREATE_MINUS_CARD)
        return Card(
            company = cardCompany,
            secretSerialInfo = cardSecretSerialInfo,
            moneyAmount = remainMoney
        )
    }
}