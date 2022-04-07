package com.daou.project.daoumall.repository.coupon

import com.daou.project.daoumall.model.coupon.RateCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface RateCouponRepository: JpaRepository<RateCoupon, Long> {
    fun findBySerialCode(serialCode: String): RateCoupon?
    fun findAllByMallName(mallName: String): List<RateCoupon>?
}