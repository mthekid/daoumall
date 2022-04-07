package com.daou.project.daoumall.repository.paymentmanagement

import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.mall.product.Product
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProductRegistration
import com.daou.project.daoumall.model.mall.product.essential.EssentialOption
import com.daou.project.daoumall.model.mall.product.essential.ProductRegistration
import com.daou.project.daoumall.model.payment.PAYMENTMEHTOD
import com.daou.project.daoumall.model.payment.PaymentManagement
import com.daou.project.daoumall.model.payment.connectionTable.OrderPaper
import com.daou.project.daoumall.model.payment.connectionTable.OrderWithAdditionalProduct
import com.daou.project.daoumall.repository.CustomerRepoTest
import com.daou.project.daoumall.repository.customer.CustomerRepository
import com.daou.project.daoumall.repository.mall.product.ProductRepository
import com.daou.project.daoumall.repository.mall.product.additional.AdditionalProductRegistrationRepository
import com.daou.project.daoumall.repository.mall.product.additional.AdditionalProductRepository
import com.daou.project.daoumall.repository.mall.product.essential.EssentialOptionRepository
import com.daou.project.daoumall.repository.mall.product.essential.ProductRegistrationRepository
import com.daou.project.daoumall.repository.paymentmanagement.connectiontable.OrderPaperRepository
import com.daou.project.daoumall.repository.paymentmanagement.connectiontable.OrderWithAdditionalProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@DisplayName("주문서 정보 생성을 테스트합니다. [REPO]")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class OrderPaperRepoTest @Autowired constructor(
    val orderPaperRepo: OrderPaperRepository,
    val orderWithAdditionalProductRepo: OrderWithAdditionalProductRepository,
    val productRegistrationRepo: ProductRegistrationRepository,
    val productRepo: ProductRepository,
    val essentialOptionRepo: EssentialOptionRepository,
    val additionalProductRepo: AdditionalProductRepository,
    val additionalProductRegistrationRepo: AdditionalProductRegistrationRepository,
    val customerRepo: CustomerRepository,
    val paymentManagementRepo: PaymentManagementRepository
) {

    companion object {
        // 테스트 상품
        val t_productName = "testProduct"
        val t_serialCode = "C_A1"
        val t_price: Long = 50000
        lateinit var t_product: Product

        // 테스트 추가상품 A
        val tA_productName = "test Additional Product"
        val tA_price: Long = 50000
        val tA_serialCode = "T-P-A1"
        lateinit var tA_additionalProduct: AdditionalProduct

        // 테스트 필수 옵션
        val t_essentialOptionName = "test essential_opt_name"
        val t_essentialOptionMetaInfo = "NONE"
        val t_essentialOptionPrice = 10_000
        val t_essentialSerialCode = "T-P-E1"
        lateinit var t_essentialOption: EssentialOption

        // 테스트 용 결제 내역 정보 생성하기.
        var t_mallName: String = "testMall"
        var t_totalPrice: Long = 0
        var t_discountAmount: Long = 0
        var t_usedPointAmount: Long = 0
        val t_orderSerialCode = "t-P-O-1"
        val t_couponCategory = CATEGORY.NONE
        val t_couponSerialCode = "NONE"
        val t_paymentMethod = PAYMENTMEHTOD.CARD
        val t_paymentSerialCode = "T-P-C1"
        lateinit var t_paymentManagement: PaymentManagement

        // 테스트용 고객
        val t_email = "test@daou.co.krr"
        val t_loginId = "mhj5730"
        val t_name = "문현준"
        val t_passwd = "qwer1234"
        val t_SSI = "qwer1234"
        lateinit var t_customer: Customer

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_product = Product(name = t_productName, serialCode = t_serialCode, price = t_price, metaInfo = "테스트용 상품")
            tA_additionalProduct = AdditionalProduct(
                name = tA_productName,
                price = tA_price,
                metaInfo = "테스트용 추가 상품",
                serialCode = tA_serialCode
            )
            t_essentialOption = EssentialOption(
                optionName = t_essentialOptionName,
                metaInfo = t_essentialOptionMetaInfo,
                price = t_essentialOptionPrice,
                serialCode = t_essentialSerialCode
            )
            t_paymentManagement = PaymentManagement(
                mallName = t_mallName,
                totalPrice = t_totalPrice,
                usedPointAmount = t_usedPointAmount,
                orderSerialCode = t_orderSerialCode,
                paymentMethod = t_paymentMethod,
                paymentSerialCode = t_paymentSerialCode,
                discountAmount = t_discountAmount,
                couponCategory = t_couponCategory,
                couponSerialCode = t_couponSerialCode
            )
            t_customer = Customer(
                email = t_email,
                loginId = CustomerRepoTest.t_loginId,
                userName = CustomerRepoTest.t_name,
                password = CustomerRepoTest.t_passwd,
                socialSecurityInfo = CustomerRepoTest.t_SSI
            )
        }
    }

    @Test
    internal fun `주문서 생성 테스트하기`() {
        // 상품 - 필수 옵션 연결하기.
        val savedProduct = productRepo.save(t_product)
        val savedEssentialOption = essentialOptionRepo.save(t_essentialOption)

        var productRegistration = ProductRegistration()
        productRegistration.product = savedProduct
        productRegistration.essentialOption = savedEssentialOption

        val savedProductRegistration = productRegistrationRepo.save(productRegistration)

        assertThat(savedProductRegistration.product).isEqualTo(savedProduct)
        assertThat(savedProductRegistration.essentialOption).isEqualTo(savedEssentialOption)

        savedProduct!!.productRegistrations.add(savedProductRegistration)

        val savedAdditionalProduct = additionalProductRepo.save(tA_additionalProduct)
        var additionalProductRegistration = AdditionalProductRegistration()
        additionalProductRegistration.additionalProduct = savedAdditionalProduct
        additionalProductRegistration.product = savedProduct

        val savedAdditionalProductRegistration = additionalProductRegistrationRepo.save(additionalProductRegistration)
        savedProduct.additionalProductRegistrations.add(savedAdditionalProductRegistration)

        assertThat(savedAdditionalProductRegistration.product).isEqualTo(savedProduct)
        assertThat(savedAdditionalProductRegistration.additionalProduct).isEqualTo(
            savedAdditionalProduct
        )

        // 주문서 정보 생성하기.
        var orderPaper = OrderPaper(
            count = 1,
            basePrice = savedProduct.price,
            essentialSerialCode = t_essentialOption.serialCode,
            essentialOptionPrice = t_essentialOptionPrice,
            productName = savedProduct.name,
            essentialOptionName = savedEssentialOption.optionName
        )
        orderPaper.product = savedProduct

        val savedOrderPaper = orderPaperRepo.save(orderPaper)
        assertThat(savedOrderPaper.product).isEqualTo(savedProduct)
        assertThat(savedOrderPaper.basePrice).isEqualTo(savedProduct.price)


        var orderWithAdditionalProduct =
            OrderWithAdditionalProduct(additionalProductPrice = savedAdditionalProduct.price, additionalProductName = savedAdditionalProduct.name)
        orderWithAdditionalProduct.orderPaper = savedOrderPaper
        orderWithAdditionalProduct.additionalProduct = savedAdditionalProduct

        val savedOrderWithAdditionalProduct = orderWithAdditionalProductRepo.save(orderWithAdditionalProduct)
        savedOrderPaper.orderWithAdditionalProducts.add(savedOrderWithAdditionalProduct)

        assertThat(savedOrderPaper.orderWithAdditionalProducts.contains(savedOrderWithAdditionalProduct)).isEqualTo(true)

        val savedCustomer = customerRepo.save(t_customer)
        val savedPaymentManagement = paymentManagementRepo.save(t_paymentManagement)
        savedPaymentManagement.orderPapers.add(savedOrderPaper)
        savedPaymentManagement.customer = savedCustomer
    }
}