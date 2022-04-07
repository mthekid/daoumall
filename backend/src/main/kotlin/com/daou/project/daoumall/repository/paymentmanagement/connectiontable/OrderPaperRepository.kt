package com.daou.project.daoumall.repository.paymentmanagement.connectiontable

import com.daou.project.daoumall.model.payment.connectionTable.OrderPaper
import org.springframework.data.jpa.repository.JpaRepository

interface OrderPaperRepository: JpaRepository<OrderPaper, Long>{
}