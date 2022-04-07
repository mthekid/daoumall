package com.daou.project.daoumall.model.mall.product.additional

import com.daou.project.daoumall.model.mall.product.Product
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class AdditionalProductRegistration() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "additional_product_registration_id")
    val id: Long = 0

    @ManyToOne(
        targetEntity = Product::class,
        fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    var product: Product? = null

    @ManyToOne(
        targetEntity = AdditionalProduct::class,
        fetch = FetchType.EAGER)
    @JoinColumn(name = "additional_product_id", nullable = false)
    var additionalProduct: AdditionalProduct? = null

    override fun toString(): String = "AdditionalProductRegistration (additionalProduct=$additionalProduct)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdditionalProductRegistration

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}