package com.daou.project.daoumall.controller.paymentdomain

import com.daou.project.daoumall.dto.malldomain.payment.MallPaymentManagementDTO
import com.daou.project.daoumall.dto.paymentdomain.PaymentAllInfoDTO
import com.daou.project.daoumall.dto.paymentdomain.PaymentManagementDTO
import com.daou.project.daoumall.dto.paymentdomain.PointAmountDTO
import com.daou.project.daoumall.dto.paymentdomain.UpdatePaymentManagementInfoDTO
import com.daou.project.daoumall.model.payment.PaymentManagement
import com.daou.project.daoumall.service.customer.CustomerService
import com.daou.project.daoumall.service.mall.payment.MallPaymentManagementService
import com.daou.project.daoumall.service.payment.PaymentManagementService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/daou/mall/payment")
class PaymentController(
    val paymentManagementService: PaymentManagementService,
    val customerService: CustomerService,
    val mallPaymentManagementService: MallPaymentManagementService
) {
    @GetMapping("/history")
    fun getPayManagementHistory(@RequestParam customerLoginId: String): ResponseEntity<Any?> {
        val paymentManagementDtos = customerService.getCustomerPayments(customerLoginId)

        return ResponseEntity.ok().body(paymentManagementDtos)
    }

    @GetMapping("/history/cancel")
    fun getCanceledPaymentHistory(@RequestParam customerLoginId: String): ResponseEntity<Any?> {
        val canceledPaymentDtos = customerService.getCanceledCustomerPayments(customerLoginId)

        return ResponseEntity.ok().body(canceledPaymentDtos)
    }

    @GetMapping("/history/complete")
    fun getCompletedPaymentHistory(@RequestParam customerLoginId: String): ResponseEntity<Any?> {
        val completedPamentDtos = customerService.getCompletedCustomerPayments(customerLoginId)

        return ResponseEntity.ok().body(completedPamentDtos)
    }

    @PostMapping("/buy-products")
    fun createPayManagementInfo(@RequestBody paymentAllInfoDto: PaymentAllInfoDTO): ResponseEntity<Any?> {

        val paymentManagementDto = paymentAllInfoDto.paymentManagementDto
        val orderPaperDtos = paymentAllInfoDto.orderPaperDtos
        val loginId = paymentManagementDto.loginId

        val paymentManagementEntity: PaymentManagement = paymentManagementDto.toEntity()
        val paymentManagementRESDto: PaymentManagementDTO.PaymentManagementRESDTO =
            paymentManagementService.createPaymentManagement(
                loginId = loginId,
                paymentManagement = paymentManagementEntity,
                orderPaperDtos = orderPaperDtos
            )
        paymentManagementRESDto.customerLoginId = loginId

        val mallPaymentManagementDto: MallPaymentManagementDTO = paymentAllInfoDto.paymentManagementDto.toMallPaymentManagementDto()
        val mallPaymentResDto = mallPaymentManagementService.createMallPaymentManagement(mallPaymentManagementDto)

        paymentManagementRESDto.mallPaymentManagement = mallPaymentResDto

        return ResponseEntity.ok().body(paymentManagementRESDto)
    }

    @PostMapping("/complete")
    fun completePaymentManagement(
        @RequestBody updatePaymentManagementInfoDto : UpdatePaymentManagementInfoDTO
    ): ResponseEntity<Any?> {
        val pointAmountDto: PointAmountDTO = paymentManagementService.completePaymentManagement(
            paymentManagementSerialCode = updatePaymentManagementInfoDto.paymentManagementSerialCode,
            loginId = updatePaymentManagementInfoDto.loginId
        )

        mallPaymentManagementService.updateToCompletePaymentManagement(orderSerialCode = updatePaymentManagementInfoDto.paymentManagementSerialCode)

        return ResponseEntity.ok().body(pointAmountDto)
    }

    @PostMapping("/cancel")
    fun cancelPaymentManagement(
        @RequestBody updatePaymentManagementInfoDto : UpdatePaymentManagementInfoDTO
    ): ResponseEntity<Any?> {
        val canceledPaymentInfoDto =
            paymentManagementService.cancelPaymentManagement(updatePaymentManagementInfoDto.loginId, updatePaymentManagementInfoDto.paymentManagementSerialCode)

        mallPaymentManagementService.updateToCancelMallPaymentManagement(orderSerialCode = updatePaymentManagementInfoDto.paymentManagementSerialCode)

        return ResponseEntity.ok().body(canceledPaymentInfoDto)
    }

    @GetMapping("/order-specification")
    fun getOrderSpecification(@RequestParam orderSerialCode: String): ResponseEntity<Any?> {
        val orderSpecification = paymentManagementService.getOrderSpecification(orderSerialCode)

        return ResponseEntity.ok().body(orderSpecification)
    }

}