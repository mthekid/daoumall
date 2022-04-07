package com.daou.project.daoumall.dto.paymentdomain

import java.time.LocalDate

data class PointAmountDTO(
    val customerLoginId: String,
    val pointAmount: Long,
) {
    val createDate: LocalDate = LocalDate.now()

    override fun toString(): String = "PointDTO (customerLoginId=$customerLoginId, pointAmount=$pointAmount, createDate=$createDate)"
}