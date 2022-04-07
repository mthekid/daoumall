package com.daou.project.daoumall.repository.paymentMethod

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.payment.method.BankAccount
import com.daou.project.daoumall.repository.customer.CustomerRepository
import com.daou.project.daoumall.repository.paymentmanagement.method.BankAccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.Column

@DataJpaTest
@DisplayName("고객의 계좌 엔티티 사용 [REPO]")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class BankAccountRepoTest @Autowired constructor(
    val customerRepo: CustomerRepository,
    val bankAccountRepo: BankAccountRepository
) {

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
        lateinit var t_bankAccount: BankAccount

        // 불량 은행 계자 생성
        val t2_company = "불량 은행"
        val t2_secretSerialInfo = "wrong_qwer1234"
        val t2_moneyAmount: Long = -3_000_000
        lateinit var t_wrong_bankAccount: BankAccount
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_customer = Customer(email = t_email, loginId = t_loginId, userName = t_name, password = t_passwd, socialSecurityInfo = t_socialSecurityInfo)
            t_bankAccount = BankAccount(company = t_company, secretSerialInfo = t_secretSerialInfo, moneyAmount = t_moneyAmount)
        }
    }

    @Test
    internal fun `은행계좌 생성 테스트`() {
        val savedBankAccount = bankAccountRepo.save(t_bankAccount)
        val foundBankAccount = bankAccountRepo.findBySecretSerialInfo(t_secretSerialInfo)

        assertThat(savedBankAccount).isEqualTo(foundBankAccount)
    }

    @Test
    internal fun `고객-은행 계좌 생성 테스트`() {
        val savedCustomer = customerRepo.save(t_customer)
        val savedBankAccount = bankAccountRepo.save(t_bankAccount)

        savedCustomer.bankAccounts.add(savedBankAccount)
        assertThat(savedCustomer.bankAccounts.contains(savedBankAccount)).isEqualTo(true)

        val foundCustomer = customerRepo.findByLoginId(t_loginId)
        assertThat(foundCustomer!!.bankAccounts.contains(savedBankAccount)).isEqualTo(true)
    }

    @Test
    internal fun `불량 은행 계좌 생성시 예외 발생`() {
        assertThrows<RuntimeException> {
            t_wrong_bankAccount = BankAccount(company = t2_company, secretSerialInfo = t2_secretSerialInfo, moneyAmount = t2_moneyAmount)
        }
    }
}