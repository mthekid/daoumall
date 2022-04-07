package com.daou.project.daoumall.repository.paymentMethod

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.payment.method.Card
import com.daou.project.daoumall.repository.customer.CustomerRepository
import com.daou.project.daoumall.repository.paymentmanagement.method.CardRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@DisplayName("고객의 카드 엔티티 사용 [REPO]")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class CardRepoTest @Autowired constructor(
    val customerRepo: CustomerRepository,
    val cardRepo: CardRepository
){

    companion object {
        // 테스트용 고객 생성
        val t_email = "test@daou.co.krr"
        val t_loginId = "테스트 로그인 ID"
        val t_name = "테스트 이름"
        val t_passwd = "qwer1234"
        val t_socialSecurityInfo = "t_ssi_1234"
        lateinit var t_customer: Customer

        // 테스트용 은행 계자 생성
        val t_company = "현준"
        val t_secretSerialInfo = "qwer1234qwer1234"
        val t_moneyAmount: Long = 3_000_000
        lateinit var t_card: Card

        // 불량 은행 계좌 생성
        val t2_company = "불량"
        val t2_secretSerialInfo = "wrong1234"
        val t2_moneyAmount: Long = -3_000_000
        lateinit var t_wrong_card: Card

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_customer = Customer(email = t_email, loginId = t_loginId, userName = t_name, password = t_passwd, socialSecurityInfo = t_socialSecurityInfo)
            t_card = Card(company = t_company, secretSerialInfo = t_secretSerialInfo, moneyAmount = t_moneyAmount)
        }
    }

    @Test
    internal fun `결제용 카드 정보 생성`() {
        val savedCard = cardRepo.save(t_card)
        val foundCard = cardRepo.findBySecretSerialInfo(t_secretSerialInfo)

        assertThat(savedCard).isEqualTo(foundCard)
    }

    @Test
    internal fun `고객-결제용 카드 연결`() {
        val savedCard = cardRepo.save(t_card)
        val savedCustomer = customerRepo.save(t_customer)

        savedCustomer.cards.add(savedCard)
        assertThat(savedCustomer.cards.contains(savedCard)).isEqualTo(true)

        val foundCustomer = customerRepo.findByLoginId(t_loginId)
        assertThat(foundCustomer!!.cards.contains(savedCard)).isEqualTo(true)
    }

    @Test
    internal fun `마이너스 금액으로 인자가 들어오면 예외 발생`() {
        assertThrows<RuntimeException> {
            t_wrong_card = Card(company = t2_company, secretSerialInfo = t2_secretSerialInfo, moneyAmount = t2_moneyAmount)
        }
    }
}