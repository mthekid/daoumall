package com.daou.project.daoumall.repository.mall.product

import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.mall.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, Long>{
    fun findBySerialCode(serialCode: String): Product?
    fun findAllByMall(mall: Mall?): Set<Product>?
    fun save(findedProduct: Product?): Product?
}