package com.daou.project.daoumall.dto.paymentdomain.point

import com.daou.project.daoumall.model.coupon.connectionTable.USED
import com.daou.project.daoumall.model.point.Point
import java.time.LocalDate
import java.time.LocalDateTime

data class PointSpecificationDTO (
    val initialPointAmount: Long,
    val remainPointAmount: Long,
    val createDate: LocalDate,
    val expiredDateTime: LocalDateTime,
    val serialOrderInfo: String,
) {
    lateinit var isUsed: USED

    companion object {
        fun fromPointEntity(point: Point): PointSpecificationDTO{
            var res = PointSpecificationDTO(
                initialPointAmount = point.initialAmount,
                remainPointAmount = point.remainAmount,
                createDate = point.createDate,
                expiredDateTime = point.expiredDateTime,
                serialOrderInfo = point.paymentManagement!!.orderSerialCode,
            )
            res.isUsed = USED.NO
            if(res.remainPointAmount == 0L) res.isUsed = USED.YES
            return res
        }
    }
}