package com.daou.project.daoumall.model.payment.connectionTable

import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class OrderWithAdditionalProduct(
    val additionalProductPrice: Long,
    val additionalProductName: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_with_additional_product_id")
    val id: Long = 0

    @ManyToOne(
        targetEntity = OrderPaper::class
    )
    @JoinColumn(name = "order_paper_id")
    lateinit var orderPaper: OrderPaper

    @ManyToOne(
        targetEntity = AdditionalProduct::class
    )
    @JoinColumn(name = "additional_product_id")
    lateinit var additionalProduct: AdditionalProduct

    override fun toString(): String = "additionalProductName=$additionalProductName," +
            "additionalProductPrice=$additionalProductPrice"
}