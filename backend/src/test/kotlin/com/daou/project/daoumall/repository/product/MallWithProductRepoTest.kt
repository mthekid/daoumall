package com.daou.project.daoumall.repository.product

import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.mall.product.Product
import com.daou.project.daoumall.model.mall.product.essential.EssentialOption
import com.daou.project.daoumall.model.mall.product.essential.ProductRegistration
import com.daou.project.daoumall.repository.mall.MallRepository
import com.daou.project.daoumall.repository.mall.product.ProductRepository
import com.daou.project.daoumall.repository.mall.product.essential.EssentialOptionRepository
import com.daou.project.daoumall.repository.mall.product.essential.ProductRegistrationRepository
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
@DisplayName("상품 생성 및 판매점 연결 테스트[REPO]")
internal class MallWithProductRepoTest @Autowired constructor(
    val mallRepository: MallRepository,
    val productRepository: ProductRepository,
    val productRegistrationRepository: ProductRegistrationRepository,
    val essentialOptionRepository: EssentialOptionRepository
) {

    companion object {
        // 테스트 mall  정보
        val t_mallName = "t_mall"
        val t_adminName = "testAdmin"
        lateinit var t_mall: Mall

        // 테스트 mall  정보2
        val t_mallName2 = "t_mall2"
        val t_adminName2 = "testAdmin2"
        lateinit var t_mall2: Mall

        // 테스트 상품 정보
        val t_productName = "t_product"
        val t_serialCode = "C_A1"
        val t_price: Long = 50000
        lateinit var t_product: Product

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            println("Before All 실행")
            // 테스트 상품점
            t_mall = Mall(mallName = t_mallName, adminName = t_adminName)
            t_mall2 = Mall(mallName = t_mallName2, adminName = t_adminName2)
            t_product = Product(name = t_productName, metaInfo = "test_product", serialCode = t_serialCode, price = t_price)
        }
    }


    @Test
    internal fun `상품 생성 후 쇼핑몰에 등록하기`() {
        val savedMall = mallRepository.save(t_mall)
        val foundMall = mallRepository.findByMallName(t_mallName)
        assertThat(savedMall).isEqualTo(foundMall)

        t_product.mall = foundMall
        productRepository.save(t_product)

        val foundProduct = productRepository.findBySerialCode(t_serialCode)
        if (foundProduct != null) {
            foundMall!!.products.add(foundProduct)
        }
        val updatedMall = mallRepository.save(foundMall!!)
        println("갱신된 쇼핑몰 정보 $updatedMall")

        assertThat(foundMall).isEqualTo(updatedMall)
    }

    @Test
    internal fun `필수 옵션을 가진 생산품 생성 후 쇼핑몰에 등록하기`() {
        val checkedMall = mallRepository.findByMallName(t_mallName)
        println("이미 등록된 판매점인지 확인하기 $checkedMall , $t_mall")

        val foundMall = mallRepository.findByMallName(t_mallName2)

        t_product.mall = foundMall
        productRepository.save(t_product)

        val foundProduct = productRepository.findBySerialCode(t_serialCode)

        val essentialOptions = listOf(
            EssentialOption(optionName = "essential_opt_name", metaInfo = "none", price = 1000, serialCode = "T-P-E1"),
            EssentialOption(optionName = "essential_opt_name2", metaInfo = "none", price = 2000, serialCode = "T-P-E2"),
            EssentialOption(optionName = "essential_opt_name3", metaInfo = "none", price = 3000, serialCode = "T-P-E3")
        )

        for(essentialOption in essentialOptions) {
            essentialOptionRepository.save(essentialOption)
        }

        val foundEssentialOptions = essentialOptionRepository.findAll()
        println("상품의 필수 옵션 정보들 $foundEssentialOptions")

        val productRegistrations = listOf(
            ProductRegistration(),
            ProductRegistration(),
            ProductRegistration()
        )

        println("생산품 연결 테이블 정보 $productRegistrations")
        for( (index, productRegistartion) in productRegistrations.withIndex()) {
            productRegistartion.product = foundProduct
            productRegistartion.essentialOption = essentialOptions.get(index)
        }

        val foundProductRegistration = productRegistrationRepository.findAll()
        println("product - essential option 연결 테이블 조회하기 $foundProductRegistration")
    }

}