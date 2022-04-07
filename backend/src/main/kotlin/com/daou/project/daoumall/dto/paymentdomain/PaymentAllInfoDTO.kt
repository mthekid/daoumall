package com.daou.project.daoumall.dto.paymentdomain

data class PaymentAllInfoDTO(
    val orderPaperDtos: MutableSet<OrderPaperDTO>,
    val paymentManagementDto: PaymentManagementDTO
)