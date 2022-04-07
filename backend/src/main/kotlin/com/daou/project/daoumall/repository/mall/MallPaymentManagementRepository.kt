package com.daou.project.daoumall.repository.mall

import com.daou.project.daoumall.model.mall.product.MallPaymentManagement
import org.springframework.data.jpa.repository.JpaRepository

interface MallPaymentManagementRepository: JpaRepository<MallPaymentManagement, Long> {

    fun findByOrderSerialCode(orderSerialCode: String): MallPaymentManagement
}