package com.daou.project.daoumall.service.mall

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.malldomain.MallDTO
import com.daou.project.daoumall.dto.malldomain.ProductDTO
import com.daou.project.daoumall.dto.malldomain.essential.EssentialOptionDTO
import com.daou.project.daoumall.dto.malldomain.essential.ProductEssentialDTO
import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.mall.product.Product
import com.daou.project.daoumall.model.mall.product.essential.EssentialOption
import com.daou.project.daoumall.model.mall.product.essential.ProductRegistration
import com.daou.project.daoumall.repository.mall.MallRepository
import com.daou.project.daoumall.repository.mall.product.ProductRepository
import com.daou.project.daoumall.repository.mall.product.essential.EssentialOptionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MallService(
    private val mallRepo: MallRepository,
    private val productRepo: ProductRepository,
    private val essentialOptionRepo: EssentialOptionRepository,
) {

    @Transactional(readOnly = true)
    fun getMallList(): Set<MallDTO.MallRESDTO>? {
        val foundMalls = mallRepo.findAll();
        val sortedMall = foundMalls.sortedWith(
            compareBy() { it.mallName }
        )

        var mallResDtos: MutableSet<MallDTO.MallRESDTO> = mutableSetOf()
        for(mall in sortedMall) {
            mallResDtos.add(MallDTO.fromEntity(mall)!!)
        }
        return mallResDtos
    }
    @Transactional
    fun createMall(mall: Mall): MallDTO.MallRESDTO? {
        Log.info("판매점을 생성합니다. 판매점 정보 : ${mall} [MallService <createMall> start]")

        if(mallRepo.findByMallName(mall.mallName) != null) throw BaseException(CustomResponseCode.ALREADY_EXSISTS_MALL)

        val savedMall = mallRepo.save(mall)
        val responseMallDTO = MallDTO.fromEntity(savedMall)

        Log.info("판매점을 생성을 완료합니다. [MallService <createMall> end]")
        return responseMallDTO
    }

    @Transactional
    fun createProduct(mallName: String, product: Product): Product? {
        Log.info("판매점에서 판매할 상품을 생성합니다. [MallService <createProduct> start]")

        val foundMall = mallRepo.findByMallName(mallName) ?: throw BaseException(CustomResponseCode.NOTFOUND_MALL)

        product.mall = foundMall
        val savedProduct = productRepo.save(product)

        Log.info("판매점에서 판매할 상품을 생성합니다.저장 생산품 : $savedProduct [MallService <createProduct> end]")
        return savedProduct
    }

    @Transactional
    fun connectProductWithEssential(
        product: Product, essentialOptions: Set<EssentialOption>
    ): Set<EssentialOption> {
        Log.info("판매점에서 판매할 상품에 필수 옵션을 연결합니다. \"등록할 상품 정보 : $product, 필수옵션 정보 : $essentialOptions  [MallService <connectProductWithEssential> start]")

        val savedProduct = productRepo.findBySerialCode(product.serialCode)

        val savedEssentialOptions = mutableSetOf<EssentialOption>()
        for (essentialOption in essentialOptions) {
            savedEssentialOptions.add(essentialOptionRepo.save(essentialOption))
        }
        Log.info("생산품에 필수 정보 옵션 추가 완료. $essentialOptions")

        val productRegistrations = savedProduct!!.productRegistrations
        for (savedEssentialOption in savedEssentialOptions) {
            val productRegistration = ProductRegistration()
            productRegistration.product = savedProduct
            productRegistration.essentialOption = savedEssentialOption
            productRegistrations.add(productRegistration)
        }

        Log.info("생산품 <-> 필수 옵션 저장 연결 완료. $essentialOptions")
        Log.info("판매점에서 판매할 상품에 필수 옵션을 연결완료했습니다. [MallService <connectProductWithEssential> end]")
        return savedEssentialOptions
    }

    @Transactional
    fun createProductWithEssentialOption(
        mallName: String,
        product: Product,
        essentialOptions: Set<EssentialOption>
    ): ProductEssentialDTO.ProductEssentialRESDTO? {
        Log.info("판매점에서 판매할 상품을 필수 옵션과 함께 생성합니다. [MallService <createProductWithEssentialOption> start]")
        Log.info("상품 정보:  $product")

        val savedProduct = createProduct(mallName, product)

        Log.info("필수 옵션 정보: $essentialOptions -> ")
        val savedEssentials = connectProductWithEssential(savedProduct!!, essentialOptions)

        var essentialOptionDTOs = mutableSetOf<EssentialOptionDTO>()
        for (essentialOption in savedEssentials) {
            essentialOptionDTOs.add(EssentialOptionDTO.fromEntity(essentialOption)!!)
        }

        Log.info("판매점에서 판매할 상품을 필수 옵션과 함께 생성완료헀습니다. [MallService <createProductWithEssentialOption> end]")
        return ProductEssentialDTO.ProductEssentialRESDTO(
            ProductDTO.fromEntity(savedProduct),
            essentialOptionDTOs
        )
    }
}