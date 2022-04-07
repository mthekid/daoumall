package com.daou.project.daoumall.repository.coupon.connectionTable

import com.daou.project.daoumall.model.coupon.connectionTable.CustomerAmountCoupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface CustomerAmountCouponRepository: JpaRepository<CustomerAmountCoupon, Long> {
}