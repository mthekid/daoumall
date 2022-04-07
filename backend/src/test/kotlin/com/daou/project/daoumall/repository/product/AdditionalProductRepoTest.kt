package com.daou.project.daoumall.repository.product

import com.daou.project.daoumall.model.mall.product.Product
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProductRegistration
import com.daou.project.daoumall.repository.mall.product.ProductRepository
import com.daou.project.daoumall.repository.mall.product.additional.AdditionalProductRegistrationRepository
import com.daou.project.daoumall.repository.mall.product.additional.AdditionalProductRepository
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
@DisplayName("추가 상품 생성 및 상품 연결 테스트[REPO]")
internal class AdditionalProductRepoTest @Autowired constructor(
    val productRepo: ProductRepository,
    val additionalProductRepo: AdditionalProductRepository,
    val additionalProductRegistrationRepo: AdditionalProductRegistrationRepository
) {

    companion object {
        // 테스트 상품 
        var t_productName = "testProduct"
        var t_serialCode = "C_A1"
        var t_price: Long = 50000
        var t_product = Product(name = t_productName, serialCode = t_serialCode, price = t_price, metaInfo = "테스트 데이터")

        // 테스트 추가상품 A
        var tA_productName = "testProduct"
        var tA_price: Long = 50000
        var tA_serialCode = "A1"
        var tA_additionalProduct = AdditionalProduct( name = tA_productName, price = tA_price, metaInfo = "테스트용 추가 상품 A", serialCode = tA_serialCode )

        // 테스트 추가상품 B
        var tB_productName = "testProductB"
        var tB_price: Long = 5000
        var tB_serialCode = "A2"
        var tB_addtionalProduct = AdditionalProduct( name = tB_productName, price = tB_price, metaInfo = "테스트용 추가 상품 B", serialCode = tB_serialCode)

        // 테스트 추가상품 C
        var tC_productName = "testProductC"
        var tC_price: Long = 8000
        var tC_serialCode = "A3"
        var tC_additionalProduct = AdditionalProduct( name = tC_productName, price = tC_price, metaInfo = "테스트용 추가 상품 B", serialCode = tC_serialCode)

        // 테스트 추가상품 D
        val tD_productName = "testProductD"
        val tD_price: Long = 5000
        val tD_serialCode = "A4"
        var tD_additionalProductD = AdditionalProduct( name = tD_productName, price = tD_price, metaInfo = "테스트용 추가 상품 B", serialCode = tD_serialCode)
    }

    @Test
    internal fun `2개의 추가상품과 상품 연결하고 eager로 데이터 가져오는 정보 확인하기`() {
        val savedProduct = productRepo.save(t_product)
        val foundProduct = productRepo.findBySerialCode(serialCode = t_serialCode)

        assertThat(savedProduct).isEqualTo(foundProduct)

        val savedtA_additionalProduct = additionalProductRepo.save(tA_additionalProduct)
        val savedtB_addtionalProduct = additionalProductRepo.save(tB_addtionalProduct)

        val foundtA_additionalProduct = additionalProductRepo.findBySerialCode(tA_serialCode)
        val foundtB_addtionalProduct = additionalProductRepo.findBySerialCode(tB_serialCode)

        assertThat(savedtA_additionalProduct).isEqualTo(foundtA_additionalProduct)
        assertThat(savedtB_addtionalProduct).isEqualTo(foundtB_addtionalProduct)

        val additionalProductRegistrationA = AdditionalProductRegistration()
        additionalProductRegistrationA.product = foundProduct
        additionalProductRegistrationA.additionalProduct = savedtA_additionalProduct

        val additionalProductRegistrationB = AdditionalProductRegistration()
        additionalProductRegistrationB.product = foundProduct
        additionalProductRegistrationB.additionalProduct = savedtB_addtionalProduct

        val savedContableA = additionalProductRegistrationRepo.save(additionalProductRegistrationA)
        val savedContableB = additionalProductRegistrationRepo.save(additionalProductRegistrationB)


        foundProduct!!.additionalProductRegistrations.add(savedContableA)
        foundProduct!!.additionalProductRegistrations.add(savedContableB)

        val updatedProduct = productRepo.findBySerialCode(t_serialCode)

        assertThat(updatedProduct!!.additionalProductRegistrations.contains(savedContableA)).isEqualTo(true)
        assertThat(updatedProduct.additionalProductRegistrations.contains(savedContableB)).isEqualTo(true)
    }
}