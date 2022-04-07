package com.daou.project.daoumall.dto.malldomain.essential

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.dto.malldomain.ProductDTO
import com.daou.project.daoumall.model.mall.product.essential.EssentialOption
import com.daou.project.daoumall.model.mall.product.Product

data class ProductEssentialDTO(
    val mallName: String,
    val productName: String,
    val price: Long,
    val serialCode: String,
    val metaInfo: String,
    val essentialOptions: Set<EssentialOptionDTO>
) {

    data class ProductEssentialRESDTO(
        val productResDTO: ProductDTO ,
        val essentialOptionDTOs: Set<EssentialOptionDTO>
    )

    fun toProductEntity(): Product {
        return Product(
            name = productName,
            serialCode = serialCode,
            price = price,
            metaInfo = metaInfo
        )
    }

    fun toEssentialEntities(): Set<EssentialOption> {
        val essentialOptionEntities = mutableSetOf<EssentialOption>()

        if(essentialOptions.isEmpty()) throw BaseException(CustomResponseCode.MUST_HAVE_ESSENTIALOPTION)
        for (essentialOptionDTO in essentialOptions) {
            val essentialEntity = essentialOptionDTO.toEntity()
            essentialOptionEntities.add(essentialEntity)
        }
        return essentialOptionEntities
    }
}