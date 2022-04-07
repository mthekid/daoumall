package com.daou.project.daoumall.service.mall.payment

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.malldomain.payment.MallPaymentManagementDTO
import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.mall.product.MallPaymentManagement
import com.daou.project.daoumall.model.payment.STATUS
import com.daou.project.daoumall.repository.mall.MallPaymentManagementRepository
import com.daou.project.daoumall.repository.mall.MallRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MallPaymentManagementService(
    private val mallRepo: MallRepository,
    private val mallPaymentManagementRepo: MallPaymentManagementRepository
) {

    @Transactional
    fun createMallPaymentManagement(mallPaymentManagementDto: MallPaymentManagementDTO): MallPaymentManagementDTO {
        Log.info("판매점에서 관리할 결제 내역을 생성합니다. $mallPaymentManagementDto")
        val mallPaymentManagementEntity = mallPaymentManagementDto.toEntity()
        val savedMallPaymentManagement = mallPaymentManagementRepo.save(mallPaymentManagementEntity)

        val foundMall = mallRepo.findByMallName(mallPaymentManagementDto.mallName) ?: throw BaseException(CustomResponseCode.NOTFOUND_MALL)
        savedMallPaymentManagement.mall = foundMall
        mallPaymentManagementDto.payAmount = (savedMallPaymentManagement.payAmount)

        return mallPaymentManagementDto
    }

    @Transactional
    fun updateToCompletePaymentManagement(orderSerialCode: String) {
        Log.info("판매점에서 관리하는 결제 내역의 상태를 확정으로 변경합니다.")

        val foundMallPaymentManagement = getMallPaymentManagement(orderSerialCode)
        foundMallPaymentManagement.orderStatus = STATUS.COMPLETE
        foundMallPaymentManagement.modifiedDate = LocalDateTime.now()
    }

    @Transactional
    fun updateToCancelMallPaymentManagement(orderSerialCode: String) {
        Log.info("판매점에서 관리하는 결제 내역의 상태를 취소로 변경합니다.")

        val foundMallPaymentManagement = getMallPaymentManagement(orderSerialCode)
        foundMallPaymentManagement.orderStatus = STATUS.CANCEL
        foundMallPaymentManagement.modifiedDate = LocalDateTime.now()
    }

    @Transactional(readOnly = true)
    fun getAllMallPaymentManagements(mallName: String) : Set<MallPaymentManagementDTO> {
        Log.info("판매점의 전체 결제 내역을 반환합니다.")
        val mallPaymentManagementDtos: MutableSet<MallPaymentManagementDTO> = mutableSetOf();

        val foundMall = getMall(mallName)
        for(mallPaymentManagement in foundMall.mallPaymentManagements) {
            var mallPaymentManagementDto = MallPaymentManagementDTO.fromEntity(mallPaymentManagement)
            mallPaymentManagementDto.payAmount = mallPaymentManagement.payAmount
            mallPaymentManagementDto.orderStatus = mallPaymentManagement.orderStatus
            mallPaymentManagementDto.createDate = mallPaymentManagement.createDate
            mallPaymentManagementDtos.add(mallPaymentManagementDto)
        }

        return mallPaymentManagementDtos
    }

    @Transactional(readOnly = true)
    fun getCanceledMallPaymentManagements(mallName: String) : Set<MallPaymentManagementDTO> {
        Log.info("판매점의 취소 결제 내역을 반환합니다.")
        val mallPaymentManagementDtos: MutableSet<MallPaymentManagementDTO> = mutableSetOf();

        val foundMall = getMall(mallName)
        val mallPaymentManagements = foundMall.mallPaymentManagements

        val canceledPaymentManagements = mallPaymentManagements.filter {
            it.orderStatus == STATUS.CANCEL
        }

        for(canceledMallPaymentManagement in canceledPaymentManagements) {
            var mallPaymentManagementDto = MallPaymentManagementDTO.fromEntity(canceledMallPaymentManagement)
            mallPaymentManagementDto.payAmount = canceledMallPaymentManagement.payAmount
            mallPaymentManagementDto.orderStatus = STATUS.CANCEL
            mallPaymentManagementDto.createDate = canceledMallPaymentManagement.createDate
            mallPaymentManagementDtos.add(mallPaymentManagementDto)
        }

        return mallPaymentManagementDtos
    }

    @Transactional(readOnly = true)
    fun getCompletedMallPaymentManagements(mallName: String) : Set<MallPaymentManagementDTO> {
        Log.info("판매점의 주문 확정 내역을 반환합니다.")
        val mallPaymentManagementDtos: MutableSet<MallPaymentManagementDTO> = mutableSetOf();

        val foundMall = getMall(mallName)
        val mallPaymentManagements = foundMall.mallPaymentManagements

        val completedPaymentManagements = mallPaymentManagements.filter {
            it.orderStatus == STATUS.COMPLETE
        }

        for(completedmallPaymentManagement in completedPaymentManagements) {
            var mallPaymentManagementDto = MallPaymentManagementDTO.fromEntity(completedmallPaymentManagement)
            mallPaymentManagementDto.payAmount = completedmallPaymentManagement.payAmount
            mallPaymentManagementDto.orderStatus = STATUS.COMPLETE
            mallPaymentManagementDto.createDate = completedmallPaymentManagement.createDate
            mallPaymentManagementDtos.add(mallPaymentManagementDto)
        }

        return mallPaymentManagementDtos
    }

    private fun getMall(mallName: String): Mall {
        return mallRepo.findByMallName(mallName) ?: throw BaseException(CustomResponseCode.NOTFOUND_MALL)
    }

    private fun getMallPaymentManagement(orderSerialCode: String): MallPaymentManagement {
        return mallPaymentManagementRepo.findByOrderSerialCode(orderSerialCode)
    }


}