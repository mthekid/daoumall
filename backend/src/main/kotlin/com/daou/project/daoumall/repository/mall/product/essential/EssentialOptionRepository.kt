package com.daou.project.daoumall.repository.mall.product.essential

import com.daou.project.daoumall.model.mall.product.essential.EssentialOption
import org.springframework.data.jpa.repository.JpaRepository

interface EssentialOptionRepository: JpaRepository<EssentialOption, Long> {
    fun findBySerialCode(serialCode: String): EssentialOption?
}