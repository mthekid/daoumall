package com.daou.project.daoumall.controller.coupondomain

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.service.coupon.CouponUseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/daou/mall/coupon/use")
class CouponUseController(
    val couponUseService: CouponUseService
) {

    @GetMapping("/auto-apply-coupon")
    private fun applyAmountCoupon(@RequestParam customerLoginId: String, @RequestParam mallName: String, @RequestParam totalPrice: Long): ResponseEntity<Any?> {
        val appliedCouponResDto = couponUseService.applyAutoCoupon(customerLoginId = customerLoginId, mallName = mallName, originalPrice =  totalPrice)

        return ResponseEntity.ok().body(appliedCouponResDto)
    }
}
