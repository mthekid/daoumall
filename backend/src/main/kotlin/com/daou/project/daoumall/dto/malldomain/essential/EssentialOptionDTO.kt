package com.daou.project.daoumall.dto.malldomain.essential

import com.daou.project.daoumall.model.mall.product.essential.EssentialOption

data class EssentialOptionDTO(
    val name: String,
    val price: Int,
    val metaInfo: String,
    val serialCode: String
) {
    data class EssentialOptionRESDTO(
        val name: String,
        val price: Int
    )
    fun toEntity(): EssentialOption {
        return EssentialOption(
            optionName = name,
            metaInfo = metaInfo,
            price = price,
            serialCode = serialCode
        )
    }

    companion object {
        fun fromEntity(essentialOption: EssentialOption?): EssentialOptionDTO? {
            if (essentialOption == null) {
                return null
            }
            return EssentialOptionDTO(
                name = essentialOption.optionName,
                metaInfo = essentialOption.metaInfo,
                price = essentialOption.price,
                serialCode = essentialOption.serialCode
            )
        }
    }
}