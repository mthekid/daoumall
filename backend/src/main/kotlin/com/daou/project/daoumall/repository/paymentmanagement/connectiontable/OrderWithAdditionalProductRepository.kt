package com.daou.project.daoumall.repository.paymentmanagement.connectiontable

import com.daou.project.daoumall.model.payment.connectionTable.OrderWithAdditionalProduct
import org.springframework.data.jpa.repository.JpaRepository

interface OrderWithAdditionalProductRepository: JpaRepository<OrderWithAdditionalProduct, Long> {
}