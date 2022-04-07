package com.daou.project.daoumall.repository.paymentmanagement.point

import com.daou.project.daoumall.model.point.Point
import org.springframework.data.jpa.repository.JpaRepository

interface PointRepository: JpaRepository<Point, Long> {
}