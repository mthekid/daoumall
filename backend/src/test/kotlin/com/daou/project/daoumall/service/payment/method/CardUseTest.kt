package com.daou.project.daoumall.service.payment.method

import com.daou.project.daoumall.model.payment.method.Card
import com.daou.project.daoumall.model.payment.method.OverRemainMoneyException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@DisplayName("카드 사용 테스트 [Entity]")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class CardUseTest {

    val t_card = Card(company = "t_test", secretSerialInfo = "t_qwer1234", moneyAmount = 3_000_000)

    @Test
    internal fun `결제 처리 테스트`() {
        var remainMoney = t_card.remainMoney
        val totalPrice: Long = remainMoney
        t_card.pay(remainMoney)

        assertThat(t_card.remainMoney).isEqualTo(0)
    }

    @Test
    internal fun `결제 금액이 남은 금액보다 높은 경우 예외 발생`() {
        var remainMoney = t_card.remainMoney
        val totalPrice: Long = remainMoney + 5

        assertThrows<OverRemainMoneyException> {
            t_card.pay(totalPrice)
        }
    }

    @Test
    internal fun `결제 취소로 결제 금액이 되돌리기`() {
        var remainMoney = t_card.remainMoney
        val totalPrice: Long = 2_000_000
        t_card.payback(totalPrice)

        assertThat(t_card.remainMoney).isEqualTo(remainMoney + totalPrice)
    }
}