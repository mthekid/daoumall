package com.daou.project.daoumall.dto.paymentdomain.method

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.payment.method.BankAccount
import com.daou.project.daoumall.model.payment.method.Card

data class BankAccountDTO(
    val customerLoginId: String,
    val bankCompany: String,
    val remainMoney: Long,
    val bankAccountSecretSerialInfo: String
) {
    data class BankAccountRESDTO(
        val customerLoginId: String,
        val bankCompany: String,
        val comment: String
    )

    data class AfterPayRESDTO(
        val company: String,
        val totalPrice: Long,
        val remainPrice: Long,
        val comment: String
    )

    companion object {
        fun fromCustomerEntityAndBankAccountEntity(customer: Customer, bankAccount: BankAccount): BankAccountRESDTO  {
            return BankAccountDTO.BankAccountRESDTO(
                customerLoginId = customer.loginId,
                bankCompany = bankAccount.company,
                comment = "${customer.userName}에 은행 계좌를 성공적으로 연결했습니다."
            )
        }

        fun afterPay(customer: Customer, bankAccount: BankAccount, totalPrice: Long): AfterPayRESDTO  {
            return BankAccountDTO.AfterPayRESDTO(
                company = bankAccount.company,
                totalPrice = totalPrice,
                remainPrice = bankAccount.remainMoney,
                comment = "${customer.userName}의 ${bankAccount.company}의 회사에서 ${totalPrice}금액 결제가 완료되었습니다. 남은 금액 : ${bankAccount.remainMoney}"
            )
        }
    }

    fun toBankAccountEntity(): BankAccount {
        if(remainMoney < 0) throw BaseException(CustomResponseCode.CANNOT_CREATE_MINUS_BANKACCOUNT)
        return BankAccount(
            company = bankCompany,
            moneyAmount = remainMoney,
            secretSerialInfo = bankAccountSecretSerialInfo
        )
    }
}