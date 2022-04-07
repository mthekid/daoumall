package com.daou.project.daoumall.repository.coupon

import com.daou.project.daoumall.model.coupon.AmountCoupon
import org.springframework.data.jpa.repository.JpaRepository

interface AmountCouponRepository: JpaRepository<AmountCoupon, Long> {
    fun findBySerialCode(serialCode: String): AmountCoupon?
    fun findAllByMallName(mallName: String): List<AmountCoupon>?
}