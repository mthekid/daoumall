package com.daou.project.daoumall.repository

import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.repository.customer.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("고객 생성 테스트[REPO]")
internal class CustomerRepoTest @Autowired constructor(
    val customerRepo: CustomerRepository
) {

    companion object {
        val t_loginId = "mhj5730"
        val t_email = "test@daou.co.kr"
        val t_name = "문현준"
        val t_passwd = "qwer1234"
        val t_SSI  = "qwer1234"
        lateinit var t_customer: Customer

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_customer = Customer(email = t_email, loginId = t_loginId, userName = t_name, password = t_passwd, socialSecurityInfo = t_SSI)
        }
    }

    @Test
    fun `사용자 생성하기`() {
        val savedCustomer = customerRepo.save(t_customer)
        val foundCustomer = customerRepo.findBySocialSecurityInfo(t_SSI)
        assertThat(savedCustomer).isEqualTo(foundCustomer)
    }
}