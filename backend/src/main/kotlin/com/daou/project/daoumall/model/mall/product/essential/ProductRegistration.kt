package com.daou.project.daoumall.model.mall.product.essential

import com.daou.project.daoumall.model.mall.product.Product
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class ProductRegistration() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_con_essential_option_id")
    val id: Long = 0

    // Product와 1대다 매핑
    @ManyToOne(targetEntity = Product::class)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    var product: Product? = null

    // Essential Option과 1대다 매핑
    @ManyToOne(targetEntity = EssentialOption::class)
    @JoinColumn(name = "essential_option_id", nullable = false)
    var essentialOption: EssentialOption? = null


    override fun toString(): String = "essentialOption=$essentialOption)"

}