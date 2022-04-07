package com.daou.project.daoumall.controller.coupondomain

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.coupondomain.use.CanUseCouponDTO
import com.daou.project.daoumall.dto.coupondomain.use.UsedCouponDTO
import com.daou.project.daoumall.service.coupon.CouponFindService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/daou/mall/customer/coupon")
class CouponFindController(
    val couponFindService: CouponFindService
) {
    @GetMapping("/can-use-coupons")
    private fun findAllCanUseCoupons(@RequestParam customerLoginId: String): ResponseEntity<Any?> {
        val canUsedCouponDtos = mutableSetOf<CanUseCouponDTO>()

        canUsedCouponDtos.addAll(couponFindService.findCanUseAmountCoupons(customerLoginId))
        canUsedCouponDtos.addAll(couponFindService.findCanUseRateCoupons(customerLoginId))

        return ResponseEntity.ok().body(canUsedCouponDtos)
    }

    @GetMapping("/mall/can-use-coupons")
    private fun findCanUseCouponsByMallName(@RequestParam customerLoginId: String, @RequestParam mallName: String): ResponseEntity<Any?> {
        val canUsedCouponDtos = mutableSetOf<CanUseCouponDTO>()
        Log.info("사용자 ID : $customerLoginId | 판매점 이름 $mallName")

        canUsedCouponDtos.addAll(couponFindService.findCanUseAmountCoupons(customerLoginId, mallName))
        canUsedCouponDtos.addAll(couponFindService.findCanUseRateCoupons(customerLoginId, mallName))

        return ResponseEntity.ok().body(canUsedCouponDtos)
    }

    @GetMapping("/used-coupons")
    private fun findAllUsedCoupons(@RequestParam customerLoginId: String): ResponseEntity<Any?> {
        var usedCouponDtos: MutableSet<UsedCouponDTO> = mutableSetOf()

        usedCouponDtos.addAll(couponFindService.findUsedAmountCoupons(customerLoginId))
        usedCouponDtos.addAll(couponFindService.findUsedRateCoupons(customerLoginId))

        return ResponseEntity.ok().body(usedCouponDtos)
    }
}