package com.daou.project.daoumall.service.payment.method

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.paymentdomain.method.CardDTO
import com.daou.project.daoumall.model.payment.method.Card
import com.daou.project.daoumall.repository.customer.CustomerRepository
import com.daou.project.daoumall.repository.paymentmanagement.method.CardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CardService (
    val customerRepo: CustomerRepository,
    val cardRepo: CardRepository
    ) {
    @Transactional
    fun createCard(loginId: String, card: Card): CardDTO.CardRESDTO {
        Log.info("고객이 사용할 카드를 생성(연결)합니다.[CardService <createCard> start]")

        var foundCustomer = customerRepo.findByLoginId(loginId) ?: throw BaseException(CustomResponseCode.NOTFOUND_CUSTOMER)

        Log.info("찾아낸 고객의 정보 ${foundCustomer.toAbstractInfoString()}")
        var savedCard = cardRepo.save(card)
        savedCard.customer = foundCustomer

        Log.info("연결한 카드 정보 $savedCard")
        Log.info("연결한 고객의 정보 ${foundCustomer.toConnectCardString()}")

        Log.info("고객이 사용할 카드를 생성(연결)했습니다.[CardService <createCard> end]")
        return CardDTO.fromCustomerEntityAndCardEntity(foundCustomer, savedCard)
    }
}