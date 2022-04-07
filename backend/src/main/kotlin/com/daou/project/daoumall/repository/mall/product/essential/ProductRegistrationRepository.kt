package com.daou.project.daoumall.repository.mall.product.essential

import com.daou.project.daoumall.model.mall.product.essential.ProductRegistration
import org.springframework.data.jpa.repository.JpaRepository

/*
Product - essentialOption 연결 테이블
 */
interface ProductRegistrationRepository: JpaRepository<ProductRegistration, Long> {

}