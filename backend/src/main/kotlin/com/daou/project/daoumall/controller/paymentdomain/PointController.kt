package com.daou.project.daoumall.controller.paymentdomain

import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.paymentdomain.PointAmountDTO
import com.daou.project.daoumall.dto.paymentdomain.point.PointSpecificationDTO
import com.daou.project.daoumall.service.customer.CustomerPointUseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/daou/mall/payment/point")
class PointController(
    val customerPointUseService: CustomerPointUseService
) {
    @GetMapping("/can-use")
    fun getCanUsePointAmount(@RequestParam customerLoginId: String): ResponseEntity<Any?> {
        var canUsePointAmount: Long = customerPointUseService.getPointAmount(customerLoginId)!!
        val pointDto = PointAmountDTO( customerLoginId = customerLoginId, pointAmount = canUsePointAmount)

        return ResponseEntity.ok().body(pointDto)
    }

    @GetMapping("/history")
    fun getPointsHistory(@RequestParam customerLoginId: String): ResponseEntity<Any?> {
        var pointHistoryDtos: List<PointSpecificationDTO> = customerPointUseService.getPointHistory(customerLoginId)

        return ResponseEntity.ok().body(pointHistoryDtos)
    }

}