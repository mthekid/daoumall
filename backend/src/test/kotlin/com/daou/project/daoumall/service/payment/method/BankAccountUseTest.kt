package com.daou.project.daoumall.service.payment.method

import com.daou.project.daoumall.model.payment.method.BankAccount
import com.daou.project.daoumall.model.payment.method.OverRemainMoneyException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@DisplayName("은행 계좌 사용 테스트 [Entity]")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class BankAccountUseTest {

    val t_bankAccount = BankAccount(company = "t_test", secretSerialInfo = "t_qwer1234", moneyAmount = 3_000_000)

    @Test
    internal fun `결제 처리 테스트`() {
        val totalPrice = t_bankAccount.remainMoney
        t_bankAccount.pay(totalPrice)

        assertThat(t_bankAccount.remainMoney).isEqualTo(0)
    }

    @Test
    internal fun `결제 금액이 남은 금액보다 높은 경우 예외 발생`() {

        assertThrows<OverRemainMoneyException> {
            t_bankAccount.pay(3_500_000)
        }
    }

    @Test
    internal fun `결제 취소로 돌아온 금액 반영하기`() {
        var remainMoney = t_bankAccount.remainMoney
        val totalPrice: Long = 2_000_000

        t_bankAccount.payback(totalPrice = totalPrice)
        assertThat(t_bankAccount.remainMoney).isEqualTo(totalPrice + remainMoney)
    }
}