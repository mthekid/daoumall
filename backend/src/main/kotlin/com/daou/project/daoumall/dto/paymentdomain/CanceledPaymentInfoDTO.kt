package com.daou.project.daoumall.dto.paymentdomain

import com.daou.project.daoumall.dto.coupondomain.use.UsedCouponDTO
import com.daou.project.daoumall.dto.paymentdomain.point.CanceledPointDTO

class CanceledPaymentInfoDTO(
    val cancelUsedCouponDto: UsedCouponDTO.CanceledCouponDTO,
    val cancelUsedPointDtos: Set<CanceledPointDTO>,
    val returnedPayAmount: Long
) {
}