package com.daou.project.daoumall.service.payment

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.paymentdomain.OrderPaperDTO
import com.daou.project.daoumall.model.mall.product.Product
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct
import com.daou.project.daoumall.model.mall.product.essential.EssentialOption
import com.daou.project.daoumall.model.payment.connectionTable.OrderPaper
import com.daou.project.daoumall.model.payment.connectionTable.OrderWithAdditionalProduct
import com.daou.project.daoumall.repository.mall.product.ProductRepository
import com.daou.project.daoumall.repository.mall.product.additional.AdditionalProductRepository
import com.daou.project.daoumall.repository.mall.product.essential.EssentialOptionRepository
import com.daou.project.daoumall.repository.paymentmanagement.connectiontable.OrderPaperRepository
import com.daou.project.daoumall.repository.paymentmanagement.connectiontable.OrderWithAdditionalProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderPaperService(
    private val productRepo: ProductRepository,
    private val additionalProductRepo: AdditionalProductRepository,
    private val orderPaperRepo: OrderPaperRepository,
    private val orderWithAdditionalProductRepo: OrderWithAdditionalProductRepository,
    private val essentialOptionRepo: EssentialOptionRepository
) {
    @Transactional
    fun createOrderPaper(orderPaperDtos: Set<OrderPaperDTO>): Set<OrderPaper> {
        Log.info("주문서 정보를 생성합니다. 받은 주문 정보 $orderPaperDtos [OrderPaperService <createOrderPaper> start]")
        var orderPapers: MutableSet<OrderPaper> = mutableSetOf()

        Log.info("주문 정보를 순회합니다.")
        for(orderPaperDto in orderPaperDtos) {
            val foundProduct = productRepo.findBySerialCode(orderPaperDto.baseProductSerialCode)
            Log.info("구매할 상품 정보 : ${foundProduct!!.forOrderInfoString()}")

            val foundEssentialOption = essentialOptionRepo.findBySerialCode(orderPaperDto.essentialOptionSerialCode)
            Log.info("구매할 상품 정보의 필수 옵션 : $foundEssentialOption")

            var orderPaper = createOrderPaper(orderPaperDto, foundProduct, foundEssentialOption)
            var savedOrderPaper = orderPaperRepo.save(orderPaper)

            savedOrderPaper.product = foundProduct
            Log.info("저장된 주문서 정보 $savedOrderPaper")

            var additionalProducts: MutableSet<AdditionalProduct> = getAdditionalProducts(orderPaperDto)
            Log.info("추가 상품 주문서 내역 저장하기 시작")

            Log.info("주문서 - 추가상품 연결 테이블 생성하기 순회 시작")
            if(additionalProducts.isNotEmpty()) {
                for(additionalProduct in additionalProducts) {
                    var orderWithAdditionalProduct = OrderWithAdditionalProduct(
                        additionalProductPrice = additionalProduct.price,
                        additionalProductName = additionalProduct.name
                    )
                    orderWithAdditionalProduct.orderPaper = savedOrderPaper
                    orderWithAdditionalProduct.additionalProduct = additionalProduct

                    val savedOrderWithAdditionalProduct = orderWithAdditionalProductRepo.save(orderWithAdditionalProduct)
                    Log.info("주문서 - 추가상품 연결 테이블: $savedOrderWithAdditionalProduct")
                    savedOrderPaper.orderWithAdditionalProducts.add(savedOrderWithAdditionalProduct)
                }
            }
            Log.info("추가할 주문서 정보 : $savedOrderPaper")
            orderPapers.add(savedOrderPaper)
        }
        Log.info("주문서 정보를 모두 생성했습니다. 주문서 정보 : $orderPapers [OrderPaperService <createOrderPaper> end]")
        return orderPapers
    }

    private fun createOrderPaper(
        orderPaperDto: OrderPaperDTO,
        foundProduct: Product,
        foundEssentialOption: EssentialOption?
    ): OrderPaper {
        var orderPaper = OrderPaper(
            count = orderPaperDto.count,
            basePrice = foundProduct.price,
            productName = foundProduct.name,
            essentialOptionName = foundEssentialOption!!.optionName,
            essentialSerialCode = orderPaperDto.essentialOptionSerialCode,
            essentialOptionPrice = foundEssentialOption.price
        )
        return orderPaper
    }

    private fun getAdditionalProducts(orderPaperDto: OrderPaperDTO): MutableSet<AdditionalProduct> {
        Log.info("추가상품 정보 반환하기")
        var additionalProducts: MutableSet<AdditionalProduct> = mutableSetOf()
        for (additionalProductSerialCode in orderPaperDto.additionalProductSerialCodes) {
            val foundAdditionalProduct = additionalProductRepo.findBySerialCode(additionalProductSerialCode)!!
            additionalProducts.add(foundAdditionalProduct)
            Log.info("함께 구매하는 추가 상품 : $foundAdditionalProduct")
        }
        return additionalProducts
    }
}