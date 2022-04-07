package com.daou.project.daoumall.dto.malldomain

import com.daou.project.daoumall.model.mall.product.Product

data class ProductDTO(
    val name: String,
    val price: Long,
    val serialCode: String,
    val metaInfo: String
) {
    data class ProductRESDTO(
        val name: String,
        val price: Long
    )

    companion object {
        fun fromEntity(product: Product): ProductDTO {
            return ProductDTO(
                name = product.name,
                price = product.price,
                metaInfo = product.metaInformation,
                serialCode = product.serialCode
            )
        }
    }

    fun toEntity(productDTO: ProductDTO): Product {
        return Product(
            name = name,
            price = price,
            serialCode = serialCode,
            metaInfo = metaInfo )
    }
}