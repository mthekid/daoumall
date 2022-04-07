package com.daou.project.daoumall.dto.paymentdomain

import com.daou.project.daoumall.dto.malldomain.ProductDTO
import com.daou.project.daoumall.dto.malldomain.additional.AdditionalProductOrderPaperDTO
import com.daou.project.daoumall.dto.malldomain.essential.EssentialOptionDTO
import com.daou.project.daoumall.model.payment.connectionTable.OrderPaper
import com.daou.project.daoumall.model.payment.connectionTable.OrderWithAdditionalProduct


class OrderPaperDTO(
    val baseProductSerialCode: String,
    val essentialOptionSerialCode: String,
    val additionalProductSerialCodes: MutableSet<String>,
    val count: Int
) {
    data class OrderPaperRESDTO(
        val count: Int,
        val productRESDto: ProductDTO.ProductRESDTO,
        val essentialOptionRESDto: EssentialOptionDTO.EssentialOptionRESDTO,
        val additionalProductOrderPaperDtos: MutableSet<AdditionalProductOrderPaperDTO>
    )

    data class BuyProductsInfoDTO(
        val productName: String,
        val productPrice: Long,
        val essentialOptionName: String,
        val essentialOptionPrice: Int,
        val buyAdditionalProductsInfoDtos: Set<BuyAdditionalProductsInfoDTO>,
        val count: Int
    )

    data class BuyAdditionalProductsInfoDTO(
        val additionalProductName: String,
        val additionalProductPrice: Long
    )


    companion object {
        fun fromOrderPaperEntity(orderPaper: OrderPaper): BuyProductsInfoDTO {
            return BuyProductsInfoDTO(
                productName = orderPaper.productName,
                productPrice = orderPaper.basePrice,
                essentialOptionName = orderPaper.essentialOptionName,
                essentialOptionPrice = orderPaper.essentialOptionPrice,
                buyAdditionalProductsInfoDtos = toBuyAdditionalProductsInfoDtos(orderPaper.orderWithAdditionalProducts),
                count = orderPaper.count
            )
        }

        fun toBuyAdditionalProductsInfoDtos(orderWithAdditionalProducts: Set<OrderWithAdditionalProduct>): Set<BuyAdditionalProductsInfoDTO> {
            var buyAdditionalproductsInfos: MutableSet<BuyAdditionalProductsInfoDTO> = mutableSetOf()
            for(orderWithAdditionalProduct in orderWithAdditionalProducts) {
                buyAdditionalproductsInfos.add(BuyAdditionalProductsInfoDTO(orderWithAdditionalProduct.additionalProductName, orderWithAdditionalProduct.additionalProductPrice))
            }
            return buyAdditionalproductsInfos
        }
    }

    override fun toString(): String = "baseProductSerialCode=$baseProductSerialCode, essentialOptionSerialCode=$essentialOptionSerialCode" +
            ", additionalProductSerialCodes=$additionalProductSerialCodes, count=$count"
}