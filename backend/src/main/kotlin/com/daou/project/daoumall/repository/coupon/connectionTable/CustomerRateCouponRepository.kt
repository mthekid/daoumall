package com.daou.project.daoumall.repository.coupon.connectionTable

import com.daou.project.daoumall.model.coupon.connectionTable.CustomerRateCoupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface CustomerRateCouponRepository: JpaRepository<CustomerRateCoupon, Long> {

}