package com.daou.project.daoumall.repository.mall.product.additional

import com.daou.project.daoumall.model.mall.product.additional.AdditionalProductRegistration
import org.springframework.data.jpa.repository.JpaRepository

/*
Product - additonalProduct 연결 테이블
 */
interface AdditionalProductRegistrationRepository: JpaRepository<AdditionalProductRegistration, Long> {
}