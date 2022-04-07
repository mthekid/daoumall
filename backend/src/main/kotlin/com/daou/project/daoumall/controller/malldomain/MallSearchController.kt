package com.daou.project.daoumall.controller.malldomain

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.malldomain.ProductDTO
import com.daou.project.daoumall.service.mall.MallSearchService
import com.daou.project.daoumall.service.mall.payment.MallPaymentManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/daou/mall/search")
class MallSearchController (
    val mallSearchService: MallSearchService,
    val mallPaymentManagementService: MallPaymentManagementService
){
    @GetMapping("/products")
    private fun getProducts(@RequestParam mallName: String): ResponseEntity<Any?> {
        val productResDtos: Set<ProductDTO>? = mallSearchService.searchMallProducts(mallName)

        return ResponseEntity.ok().body(productResDtos)
    }

    @GetMapping("/product")
    private fun getProductInfo(@RequestParam productSerialCode: String): ResponseEntity<Any?> {
        val foundProductInfoDTO = mallSearchService.searchProductInfo(productSerialCode)

        return ResponseEntity.ok().body(foundProductInfoDTO)
    }

    @GetMapping("/additional-products")
    private fun getAllAdditionalProducts(@RequestParam mallName: String): ResponseEntity<Any?> {
        val foundAdditionalProductInfoDtos = mallSearchService.searchAdditionalProducts(mallName)

        return ResponseEntity.ok().body(foundAdditionalProductInfoDtos)
    }

    @GetMapping("/amount-coupons")
    private fun getAmountCoupons(@RequestParam mallName: String): ResponseEntity<Any?> {
        val foundAmountCouponResDtos = mallSearchService.searchAmountCoupons(mallName)

        return ResponseEntity.ok().body(foundAmountCouponResDtos)
    }

    @GetMapping("/rate-coupons")
    private fun getRateCoupons(@RequestParam mallName: String): ResponseEntity<Any?> {
        val foundRateCouponResDtos = mallSearchService.searchRateCoupons(mallName)

        return ResponseEntity.ok().body(foundRateCouponResDtos)
    }

    @GetMapping("/payment-managements")
    private fun getMallPaymentManagements(@RequestParam mallName: String): ResponseEntity<Any?> {
        return ResponseEntity.ok().body(mallPaymentManagementService.getAllMallPaymentManagements(mallName))
    }

    @GetMapping("/payment-managements/canceled")
    private fun getCanceledMallPaymentManagements(@RequestParam mallName: String): ResponseEntity<Any?> {
        return ResponseEntity.ok().body(mallPaymentManagementService.getCanceledMallPaymentManagements(mallName))
    }

    @GetMapping("/payment-managements/completed")
    private fun getCompletedMallPaymentManagements(@RequestParam mallName: String): ResponseEntity<Any?> {
        return ResponseEntity.ok().body(mallPaymentManagementService.getCompletedMallPaymentManagements(mallName))
    }
}