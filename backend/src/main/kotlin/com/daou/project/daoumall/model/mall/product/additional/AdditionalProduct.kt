package com.daou.project.daoumall.model.mall.product.additional

import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.payment.connectionTable.OrderWithAdditionalProduct
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "additional_products")
@Entity
class AdditionalProduct(
    name: String,
    price: Long,
    metaInfo: String,
    @Column(nullable = false, unique = true)
    val serialCode: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "additional_product_id")
    val id: Long = 0

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var price: Long = price
        protected set

    var metaInformation: String = metaInfo
        protected set

    val createDate: LocalDateTime = LocalDateTime.now()

    @ManyToOne(targetEntity = Mall::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "mall_id")
    @JsonIgnore
    var mall: Mall? = null

    @OneToMany(
        targetEntity = AdditionalProductRegistration::class,
        mappedBy = "additionalProduct"
    )
    @JsonIgnore
    var additionalProductRegistrations: MutableSet<AdditionalProductRegistration> = mutableSetOf()

    @OneToMany(
        targetEntity = OrderWithAdditionalProduct::class,
        mappedBy = "additionalProduct"
    )
    var orderWithAdditionalProduct: MutableSet<OrderWithAdditionalProduct> = mutableSetOf()

    override fun toString(): String =
        "AdditionalProduct(name=$name, price=$price, serialCode=$serialCode, metaInformation=$metaInformation"
}