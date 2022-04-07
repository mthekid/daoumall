package com.daou.project.daoumall.repository.mall.product.additional

import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct
import org.springframework.data.jpa.repository.JpaRepository

interface AdditionalProductRepository: JpaRepository<AdditionalProduct, Long> {
    fun findBySerialCode(serialCode: String): AdditionalProduct?
    fun findAllByMall(mall: Mall): List<AdditionalProduct>?
}