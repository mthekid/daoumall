package com.daou.project.daoumall.repository.mall

import com.daou.project.daoumall.model.mall.Mall
import org.springframework.data.jpa.repository.JpaRepository

interface MallRepository : JpaRepository<Mall, Long> {
    fun findByMallName(mallName: String): Mall?
}