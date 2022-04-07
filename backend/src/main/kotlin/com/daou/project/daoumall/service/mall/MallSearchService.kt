package com.daou.project.daoumall.service.mall

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.coupondomain.AmountCouponDTO
import com.daou.project.daoumall.dto.coupondomain.RateCouponDTO
import com.daou.project.daoumall.dto.malldomain.ProductDTO
import com.daou.project.daoumall.dto.malldomain.ProductInfoDTO
import com.daou.project.daoumall.dto.malldomain.additional.AdditionalProductDTO
import com.daou.project.daoumall.dto.malldomain.essential.EssentialOptionDTO
import com.daou.project.daoumall.repository.coupon.AmountCouponRepository
import com.daou.project.daoumall.repository.coupon.RateCouponRepository
import com.daou.project.daoumall.repository.mall.MallRepository
import com.daou.project.daoumall.repository.mall.product.ProductRepository
import com.daou.project.daoumall.repository.mall.product.additional.AdditionalProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MallSearchService (
    private val mallRepo: MallRepository,
    private val productRepo: ProductRepository,
    private val additionalProductRepo: AdditionalProductRepository,
    private val amountCouponRepo: AmountCouponRepository,
    private val rateCouponRepo: RateCouponRepository
) {
    @Transactional(readOnly = true)
    fun searchMallProducts(mallName: String): Set<ProductDTO>? {
        Log.info("판매점${mallName}의 상품들을 조회합니다. [MallSearchService <searchMallProducts> start]")

        val foundMall = findMall(mallName)

        val foundProducts = productRepo.findAllByMall(foundMall)
        val productResDtos = mutableSetOf<ProductDTO>()

        Log.info("$mallName 판매점에서 검색할 상품을 UI에 전달할 DTO 정보로 추출합니다.")
        if (foundProducts != null) {
            for (foundProduct in foundProducts) {
                productResDtos.add(ProductDTO.fromEntity(foundProduct))
            }
        }

        Log.info("판매점의 상품들을 조회 완료합니다. 상품몰이 가진 상품들입니다 $foundProducts [MallSearchService <searchMallProducts> end]")
        return productResDtos
    }

    @Transactional(readOnly = true)
    fun searchAdditionalProducts(mallName: String): Set<AdditionalProductDTO> {
        Log.info("판매점의 추가상품들을 조회합니다.")

        val foundMall = findMall(mallName)
        val foundAdditionalProducts = additionalProductRepo.findAllByMall(foundMall) ?: throw BaseException(CustomResponseCode.NOTFOUND_PRODUCT)

        val additionalProductResDtos = mutableSetOf<AdditionalProductDTO>()
        if(foundAdditionalProducts != null) {
            for(foundAdditionalProduct in foundAdditionalProducts) {
                additionalProductResDtos.add(AdditionalProductDTO.fromEntity(foundAdditionalProduct)!!)
            }
        }
        return additionalProductResDtos
    }

    @Transactional(readOnly = true)
    fun searchAmountCoupons(mallName: String): Set<AmountCouponDTO.AmountCouponRESDTO> {
        Log.info("판매점${mallName}이 보유한 금액 쿠폰 목록을 확인합니다")
        findMall(mallName)
        val foundAmountCoupons = amountCouponRepo.findAllByMallName(mallName)!!

        var foundAmountCouponResDtos : MutableSet<AmountCouponDTO.AmountCouponRESDTO> = mutableSetOf()

        for(foundAmountCoupon in foundAmountCoupons) {
            foundAmountCouponResDtos.add(AmountCouponDTO.fromEntity(foundAmountCoupon))
        }
        return foundAmountCouponResDtos
    }

    @Transactional(readOnly = true)
    fun searchRateCoupons(mallName: String): Set<RateCouponDTO.RateCouponRESDTO> {
        Log.info("판매점${mallName}이 보유한 금액 쿠폰 목록을 확인합니다")
        findMall(mallName)
        val foundRateCoupons = rateCouponRepo.findAllByMallName(mallName)!!

        var foundRateCouponResDtos : MutableSet<RateCouponDTO.RateCouponRESDTO> = mutableSetOf()

        for(foundRateCoupon in foundRateCoupons) {
            foundRateCouponResDtos.add(RateCouponDTO.fromEntity(foundRateCoupon))
        }
        return foundRateCouponResDtos
    }

    private fun findMall(mallName: String) =
        mallRepo.findByMallName(mallName) ?: throw BaseException(CustomResponseCode.NOTFOUND_MALL)

    @Transactional(readOnly = true)
    fun searchProductInfo(serialCode: String): ProductInfoDTO? {
        Log.info("판매점의 상품 [${serialCode}]을 세부적으로 조회합니다. [MallSearchService <searchProductInfo> start]")

        val foundProducts = productRepo.findBySerialCode(serialCode)

        val additionalProductDtos: MutableSet<AdditionalProductDTO> = mutableSetOf()
        val essentialOptionsDtos: MutableSet<EssentialOptionDTO> = mutableSetOf()

        if (foundProducts != null) {
            val additionalProductContables = foundProducts.additionalProductRegistrations // Additional Product 정보를 가진다.
            val sortedAdditionalProducts = additionalProductContables.sortedWith(
                compareBy() {it.additionalProduct!!.price}
            )
            for (contable in sortedAdditionalProducts) {
                additionalProductDtos.add(AdditionalProductDTO.fromEntity(contable.additionalProduct)!!)
            }

            val EssentialOptionContables = foundProducts.productRegistrations // Essential Option 가져오기
            val sortedEssentialOption = EssentialOptionContables.sortedWith(
                compareBy() {it.essentialOption!!.price}
            )

            for (contable in sortedEssentialOption) {
                essentialOptionsDtos.add(EssentialOptionDTO.fromEntity(contable.essentialOption)!!)
            }
        }

        val productInfoDTO = ProductInfoDTO(
            productRes = ProductDTO.fromEntity(foundProducts!!),
            essentialOptions = essentialOptionsDtos,
            additionalProducts = additionalProductDtos
        )

        Log.info("판매점의 상품 세부 조회를 종료합니다.. [MallSearchService <searchProductInfo> end]")
        return productInfoDTO
    }

}