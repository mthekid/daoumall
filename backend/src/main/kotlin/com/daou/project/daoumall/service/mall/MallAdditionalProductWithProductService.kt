package com.daou.project.daoumall.service.mall

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.malldomain.additional.AdditionalProductRegistrationDTO
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProductRegistration
import com.daou.project.daoumall.repository.mall.MallRepository
import com.daou.project.daoumall.repository.mall.product.ProductRepository
import com.daou.project.daoumall.repository.mall.product.additional.AdditionalProductRegistrationRepository
import com.daou.project.daoumall.repository.mall.product.additional.AdditionalProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MallAdditionalProductWithProductService (
    private val mallRepo: MallRepository,
    private val additionalProductRepo: AdditionalProductRepository,
    private val productRepo: ProductRepository,
    private val additionalProductRegistrationRepo: AdditionalProductRegistrationRepository // 연결 테이블용
    ) {

    @Transactional
    fun connectProductWithAdditionalProduct(
        productSerialCode: String,
        additionalProductSerialCode: String
    ): AdditionalProductRegistrationDTO {
        Log.info("상품에 추가 상품을 연결합니다. [MallAdditionalProductWithProductService <connectProductWithAddtionalProduct> start]")

        val foundProduct = productRepo.findBySerialCode(productSerialCode)
        val foundAdditionalProduct = additionalProductRepo.findBySerialCode(additionalProductSerialCode)

        Log.info("조회된 상품 $foundProduct")
        Log.info("조회된 추가 상품 $foundAdditionalProduct")

        var additionalProductRegistrations = foundProduct!!.additionalProductRegistrations
        val additionalProductRegistration = AdditionalProductRegistration()
        additionalProductRegistration.product = foundProduct
        additionalProductRegistration.additionalProduct = foundAdditionalProduct

        additionalProductRegistrations.add(additionalProductRegistration)

        Log.info("상품에 추가 상품을 연결을 완료합니다. [MallAdditionalProductWithProductService <connectProductWithAddtionalProduct> end]")
        return AdditionalProductRegistrationDTO(
            productName = foundProduct.name,
            additionalProductName = foundAdditionalProduct!!.name
        )
    }

    @Transactional
    fun createAdditionalProduct(mallName: String, additionalProduct: AdditionalProduct): AdditionalProduct? {
        Log.info("추가 상품을 추가합니다. [MallAdditionalProductWithProductService <createAdditionalProduct> start]")
        val foundMall = mallRepo.findByMallName(mallName) ?: throw BaseException(CustomResponseCode.NOTFOUND_MALL)

        additionalProduct.mall = foundMall
        val savedAdditionalProduct = additionalProductRepo.save(additionalProduct)
        Log.info("저장된 추가상품 : $savedAdditionalProduct")

        Log.info("추가 상품 서비스를 종료합니다 [MallAdditionalProductWithProductService <createAdditionalProduct> end]")
        return savedAdditionalProduct
    }

}