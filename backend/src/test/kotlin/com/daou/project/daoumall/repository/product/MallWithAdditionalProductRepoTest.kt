package com.daou.project.daoumall.repository.product

import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.mall.product.Product
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProductRegistration
import com.daou.project.daoumall.repository.mall.MallRepository
import com.daou.project.daoumall.repository.mall.product.ProductRepository
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
@DisplayName("추가 상품 생성 및 판매점 연결 테스트[REPO]")
internal class MallWithAdditionalProductRepoTest @Autowired constructor(
    val mallRepo: MallRepository,
    val additionalProductRepo: AdditionalProductRepository,
    val productRepo: ProductRepository
) {
    companion object {
        // mall 테스트 정보 생성 
        var t_adminName = "t_mall"
        var t_mallName = "testAdmin"
        var t_mall = Mall(mallName = t_mallName, adminName = t_adminName)

        // 추가상품 정보 생성 A
        val tA_productName = "t_additionalProduct_A"
        val tA_price: Long = 50000
        val tA_metaInfo = "test AProduct MetaInfo1"
        val tA_serialCode = "A1"
        var tA_addtionalProduct = AdditionalProduct( name = tA_productName, price = tA_price, metaInfo = tA_metaInfo, serialCode = tA_serialCode)

        // 추가상품 정보 생성 B
        val tB_productName = "t_additionalProduct_B"
        val tB_price: Long = 5000
        val tB_metaInfo = "test AProduct MetaInfo2"
        val tB_serialCode = "A2"
        var tB_addtionalProduct = AdditionalProduct(name = tB_productName, price = tB_price, metaInfo = tB_metaInfo, serialCode = tB_serialCode)

        // test product
        val t_productName = "test Product"
        val t_serialCode = "TA-1"
        val t_metaInfo = "test Product data"
        val t_price: Long = 50000
        var t_product = Product( name = t_productName, price = t_price, metaInfo = t_metaInfo, serialCode = t_serialCode)
    }

    @Test
    internal fun `쇼핑몰에 2개의 추가상품 추가하기`() {
        val savedMall = mallRepo.save(t_mall)

        // 추가상품-상품몰 등록[1]
        tA_addtionalProduct.mall = savedMall

        val savedAdditionalProduct = additionalProductRepo.save(tA_addtionalProduct)
        val foundAdditionalProduct = additionalProductRepo.findBySerialCode(tA_serialCode)
        assertThat(savedAdditionalProduct).isEqualTo(foundAdditionalProduct)

        // 추가상품-상품몰 등록[2]
        tB_addtionalProduct.mall = savedMall
        val savedtB_addtionalProduct = additionalProductRepo.save(tB_addtionalProduct)
        val foundtB_addtionalProduct = additionalProductRepo.findBySerialCode(tB_serialCode)
        assertThat(savedtB_addtionalProduct).isEqualTo(foundtB_addtionalProduct)

        println("쇼핑몰에 저장된 추가 상품 목록 확인하기. $savedMall")
    }

    @Test
    internal fun `상품에 2개의 추가상품 연결하기`() {
        val savedProduct = productRepo.save(t_product)
        val savedAdditionalProduct = additionalProductRepo.save(tA_addtionalProduct)

        val additionalProductRegistration = AdditionalProductRegistration()
        additionalProductRegistration.product = savedProduct
        additionalProductRegistration.additionalProduct = savedAdditionalProduct

        val additionalProductRegistrations_A = savedProduct!!.additionalProductRegistrations
        additionalProductRegistrations_A.add(additionalProductRegistration)

        println("첫번째 추가상품 연결 후 Product 정보 $savedProduct")

        // 두번째 추가 상품
        val savedtB_addtionalProduct = additionalProductRepo.save(tB_addtionalProduct)

        val additionalProductRegistration2 = AdditionalProductRegistration()
        additionalProductRegistration2.product = savedProduct
        additionalProductRegistration2.additionalProduct = savedtB_addtionalProduct

        val foundProduct = productRepo.findBySerialCode(t_serialCode)
        val additionalProductRegistrations_B = foundProduct!!.additionalProductRegistrations
        additionalProductRegistrations_B.add(additionalProductRegistration2)

        val savedProduct2 = productRepo.save(foundProduct)
        println("연결 테이블을 저장한이후에 product 정보 $savedProduct2 " )
    }
}