package com.daou.project.daoumall.service.customer

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.paymentdomain.point.PointSpecificationDTO
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.point.Point
import com.daou.project.daoumall.repository.customer.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CustomerPointUseService(
    private val customerRepo: CustomerRepository
) {
    @Transactional
    fun getPointAmount(loginId: String): Long? {
        Log.info("사용자가 사용할 수 있는 포인트 금액을 조회합니다. [CustomerService <getPointAmount> start]")
        val foundCustomer = getCustomer(loginId)

        val customerPoints = foundCustomer.customerPoints
        var pointsHistory: MutableList<Point> = mutableListOf()

        for(customerPoint in customerPoints) {
            var pointInfo = customerPoint.point
            if(pointInfo!!.remainAmount > 0 && pointInfo.expiredDateTime >= LocalDateTime.now()) {
                pointsHistory.add(pointInfo)
            }
        }

        Log.info("사용자가 사용할 수 있는 포인트 금액을 조회했습니다. [CustomerService <getPointAmount> end]")
        return pointsHistory.sumOf{ it.remainAmount }
    }

    private fun getCustomer(loginId: String): Customer {
        val foundCustomer =
            customerRepo.findByLoginId(loginId) ?: throw BaseException(CustomResponseCode.NOTFOUND_CUSTOMER)
        return foundCustomer
    }

    @Transactional
    fun getPointHistory(loginId: String): List<PointSpecificationDTO> {
        Log.info("사용자의 포인트 내역을 조회합니다. [CustomerService <getPointHistory> start]")
        val foundCustomer = getCustomer(loginId)

        val customerPoints = foundCustomer.customerPoints
        Log.info("사용자의 포인트 내역 정보 $customerPoints")
        var pointSpecificationHistory: MutableList<PointSpecificationDTO> = mutableListOf()

        for (customerPoint in customerPoints) {
            val pointInfo = customerPoint.point
            pointSpecificationHistory.add(PointSpecificationDTO.fromPointEntity(pointInfo!!))
        }
        Log.info("사용자의 DTO 변환 포인트 내역 정보 $pointSpecificationHistory")

        Log.info("사용자의 포인트 내역을 조회했습니다. [CustomerService <getPointHistory> end]")
        return pointSpecificationHistory.sortedWith(
            compareBy { it.expiredDateTime }
        )
    }
}