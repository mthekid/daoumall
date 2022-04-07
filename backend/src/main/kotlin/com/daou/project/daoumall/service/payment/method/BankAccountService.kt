package com.daou.project.daoumall.service.payment.method

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.paymentdomain.method.BankAccountDTO
import com.daou.project.daoumall.model.payment.method.BankAccount
import com.daou.project.daoumall.repository.customer.CustomerRepository
import com.daou.project.daoumall.repository.paymentmanagement.method.BankAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BankAccountService (
    val customerRepo: CustomerRepository,
    val bankAccountRepo: BankAccountRepository
    ){

    @Transactional
    fun createBankAccount(loginId: String, bankAccount: BankAccount): BankAccountDTO.BankAccountRESDTO {
        Log.info("고객이 사용할 카드를 생성(연결)합니다.[CardService <createCard> start]")

        val foundCustomer = customerRepo.findByLoginId(loginId) ?: throw BaseException(CustomResponseCode.NOTFOUND_CUSTOMER)

        Log.info("찾아낸 고객의 정보 ${foundCustomer.toAbstractInfoString()}")
        val savedBankAccount = bankAccountRepo.save(bankAccount)
        savedBankAccount.customer = foundCustomer

        Log.info("연결한 은행 계좌 정보 $savedBankAccount")
        Log.info("연결한 고객의 정보 ${foundCustomer.toConnectBankAccountString()}")

        Log.info("고객이 사용할 카드를 생성(연결)했습니다.[CardService <createCard> end]")
        return BankAccountDTO.fromCustomerEntityAndBankAccountEntity(foundCustomer, savedBankAccount)
    }
}