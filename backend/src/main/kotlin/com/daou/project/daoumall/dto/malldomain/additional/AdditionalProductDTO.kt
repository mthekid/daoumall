package com.daou.project.daoumall.dto.malldomain.additional

import com.daou.project.daoumall.dto.malldomain.ProductDTO
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct

data class AdditionalProductDTO(
    val mallName: String,
    val additionalProduct: ProductDTO
) {

    companion object {
        fun fromEntity(additionalProduct: AdditionalProduct?): AdditionalProductDTO? {
            if (additionalProduct == null) {
                return null
            }
            return AdditionalProductDTO(
                mallName = additionalProduct.mall!!.mallName,
                additionalProduct = ProductDTO(
                    name = additionalProduct.name,
                    price = additionalProduct.price,
                    serialCode = additionalProduct.serialCode,
                    metaInfo = additionalProduct.metaInformation
                )
            )
        }
    }

    fun toProductEntity(): AdditionalProduct {
        return AdditionalProduct(
            name = additionalProduct.name,
            metaInfo = additionalProduct.metaInfo,
            serialCode = additionalProduct.serialCode,
            price = additionalProduct.price
        )
    }
}