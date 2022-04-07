package com.daou.project.daoumall.controller.paymentdomain

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.paymentdomain.method.BankAccountDTO
import com.daou.project.daoumall.dto.paymentdomain.method.CardDTO
import com.daou.project.daoumall.model.payment.method.BankAccount
import com.daou.project.daoumall.model.payment.method.Card
import com.daou.project.daoumall.service.payment.method.BankAccountService
import com.daou.project.daoumall.service.payment.method.CardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/daou/mall/payment/method")
class PaymentMethodController(
    val cardService: CardService,
    val bankAccountService: BankAccountService
) {

    @PostMapping("/card")
    private fun createCard(@RequestBody cardDto : CardDTO): ResponseEntity<Any?> {
        val cardRESDto = cardService.createCard(cardDto.customerLoginId, cardDto.toCardEntity())

        return ResponseEntity.ok().body(cardRESDto)
    }

    @PostMapping("/bank-account")
    private fun createBankAccount(@RequestBody bankAccountDto : BankAccountDTO): ResponseEntity<Any?> {
        val bankAccountRESDto = bankAccountService.createBankAccount(bankAccountDto.customerLoginId, bankAccountDto.toBankAccountEntity())

        return ResponseEntity.ok().body(bankAccountRESDto)
    }
}