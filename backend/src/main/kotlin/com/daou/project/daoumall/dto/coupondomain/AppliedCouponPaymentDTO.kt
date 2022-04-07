package com.daou.project.daoumall.dto.coupondomain

import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import java.time.LocalDate

data class AppliedCouponPaymentDTO(
    val originalPrice: Long,
    val couponSerialCode: String,
    val couponCATEGORY: CATEGORY,
) {
    var discountPrice: Long = 0
    private val usedDate: LocalDate = LocalDate.now()

    override fun toString() =
        "AppliedCouponPaymentDTO(usedDate=$usedDate, originalPrice=$originalPrice, couponSerialCode=$couponSerialCode, couponCATEGORY=$couponCATEGORY, discountPrice=$discountPrice"
}