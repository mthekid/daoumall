package com.daou.project.daoumall.repository.paymentmanagement.connectiontable

import com.daou.project.daoumall.model.point.connectionTable.CustomerPoint
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerPointRepository: JpaRepository<CustomerPoint, Long> {
}