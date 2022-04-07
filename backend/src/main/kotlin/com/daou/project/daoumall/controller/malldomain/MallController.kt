package com.daou.project.daoumall.controller.malldomain

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.malldomain.MallDTO
import com.daou.project.daoumall.dto.malldomain.additional.AdditionalProductConnectProductDTO
import com.daou.project.daoumall.dto.malldomain.additional.AdditionalProductDTO
import com.daou.project.daoumall.dto.malldomain.essential.ProductEssentialDTO
import com.daou.project.daoumall.service.mall.MallAdditionalProductWithProductService
import com.daou.project.daoumall.service.mall.MallService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/daou/mall")
class MallController (
    val mallService: MallService,
    val mallAdditionalProductService: MallAdditionalProductWithProductService
){
    @PostMapping("/create")
    private fun createMall(@RequestBody mallDTO: MallDTO): ResponseEntity<Any?> {
        val responseDTO = mallService.createMall(mallDTO.toEntity())

        return ResponseEntity.ok().body(responseDTO)
    }

    @PostMapping("/product-essential-option")
    private fun createProductWithEssentialOption(@RequestBody productEssentialDto: ProductEssentialDTO): ResponseEntity<Any?> {
        val productEntity = productEssentialDto.toProductEntity()
        val essentialOptions = productEssentialDto.toEssentialEntities()

        val productEssentialResDto = mallService.createProductWithEssentialOption(
            mallName = productEssentialDto.mallName,
            product = productEntity,
            essentialOptions = essentialOptions
        )
        return ResponseEntity.ok().body(productEssentialResDto)
    }

    @PostMapping("/additional-product")
    private fun createAdditionalProduct(@RequestBody additionalProductDto: AdditionalProductDTO): ResponseEntity<Any?> {
        val additionalProductEntity = additionalProductDto.toProductEntity()
        val savedAdditionalProduct =
            mallAdditionalProductService.createAdditionalProduct(additionalProductDto.mallName, additionalProductEntity)

        val responseDTO = AdditionalProductDTO.fromEntity(savedAdditionalProduct)
        return ResponseEntity.ok().body(responseDTO)
    }

    @PostMapping("/additional-with-product")
    private fun registerProductWithAdditionalProduct(@RequestBody additionalProductWithProductDto: AdditionalProductConnectProductDTO): ResponseEntity<Any?> {
        val responseDTO = mallAdditionalProductService.connectProductWithAdditionalProduct(
            additionalProductWithProductDto.productSerialCode,
            additionalProductWithProductDto.additionalProductSerialCode
        )

        return ResponseEntity.ok().body(responseDTO)
    }

    @GetMapping("/list")
    private fun getMallList(): ResponseEntity<Any?> {
        val foundMallResDtos = mallService.getMallList();
        return ResponseEntity.ok().body(foundMallResDtos)
    }
}