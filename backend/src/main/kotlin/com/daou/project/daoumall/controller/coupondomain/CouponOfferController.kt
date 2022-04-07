package com.daou.project.daoumall.controller.coupondomain

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.coupondomain.AmountCouponDTO
import com.daou.project.daoumall.dto.coupondomain.RateCouponDTO
import com.daou.project.daoumall.dto.coupondomain.offer.OfferCouponDTO
import com.daou.project.daoumall.service.coupon.CouponRegisterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/daou/mall/coupon/")
class CouponOfferController(
    val couponRegisterService: CouponRegisterService
) {
    @PostMapping("/amount")
    private fun createAmountCoupon(@RequestBody amountCouponDTO: AmountCouponDTO): ResponseEntity<Any?> {
        val amountCouponResDto = couponRegisterService.createAmountCoupon(amountCouponDTO.toEntity())

        return ResponseEntity.ok().body(amountCouponResDto)
    }

    @PostMapping("/rate")
    private fun createRateCoupon(@RequestBody rateCouponDTO: RateCouponDTO): ResponseEntity<Any?> {
        val rateCouponResDto = couponRegisterService.createRateCoupon(rateCouponDTO.toEntity())

        return ResponseEntity.ok().body(rateCouponResDto)
    }

    @PostMapping("/offer-amount")
    private fun offerAmountCouponToCustomer(@RequestBody offerCouponDTO: OfferCouponDTO ): ResponseEntity<Any?> {
        val offerCouponRESDTO = couponRegisterService.offerAmountCouponToCustomer(offerCouponDTO)

        return ResponseEntity.ok().body(offerCouponRESDTO)
    }

    @PostMapping("/offer-rate")
    private fun offerRateCouponToCustomer(@RequestBody offerCouponDto: OfferCouponDTO): ResponseEntity<Any?> {
        val offerCouponResDto = couponRegisterService.offerRateCouponToCustomer(offerCouponDto)

        return ResponseEntity.ok().body(offerCouponResDto)
    }
}