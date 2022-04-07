package com.daou.project.daoumall.dto.malldomain

import com.daou.project.daoumall.dto.malldomain.additional.AdditionalProductDTO
import com.daou.project.daoumall.dto.malldomain.essential.EssentialOptionDTO

data class ProductInfoDTO(
    val productRes: ProductDTO,
    val additionalProducts: Set<AdditionalProductDTO>,
    val essentialOptions: Set<EssentialOptionDTO>
)