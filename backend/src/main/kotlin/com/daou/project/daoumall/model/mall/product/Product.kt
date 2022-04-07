package com.daou.project.daoumall.model.mall.product

import com.daou.project.daoumall.model.mall.Mall
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProductRegistration
import com.daou.project.daoumall.model.mall.product.essential.ProductRegistration
import com.daou.project.daoumall.model.payment.connectionTable.OrderPaper
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import javax.persistence.*

@Table(name = "products")
@Entity
class Product(
    name: String,
    @Column(nullable = false, unique = true)
    val serialCode: String,
    metaInfo: String,
    price: Long) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    val id: Long = 0

    var name: String = name
    var metaInformation: String = metaInfo

    @Column(nullable = false)
    var price: Long = price

    val createDate: LocalDate = LocalDate.now()

    @ManyToOne(
        targetEntity = Mall::class,
        fetch = FetchType.LAZY)
    @JoinColumn(name = "mall_id")
    @JsonIgnore
    var mall: Mall? = null

    // ProductRegistration과 일대다 매핑 [필수옵션 전용]
    @OneToMany(
        targetEntity = ProductRegistration::class,
        mappedBy = "product",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    var productRegistrations: MutableSet<ProductRegistration> = mutableSetOf()


    // 주문서 정보와 매칭되는 정보이다.
    @OneToMany(
        targetEntity = OrderPaper::class,
        mappedBy = "product",
    )
    var orderPapers: MutableSet<OrderPaper> = mutableSetOf()

    // AdditionalProductRegistration 일대다 매핑 [ 추가상품 전용 ]
    @OneToMany(
        targetEntity = AdditionalProductRegistration::class,
        mappedBy = "product",
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    var additionalProductRegistrations: MutableSet<AdditionalProductRegistration> = mutableSetOf()

    override fun toString(): String = "Product(id=$id, name=$name, serialCode=$serialCode, " +
            "metaInformation=$metaInformation, price=$price, createDate=$createDate, " +
            "additionalProductRegistrations=$additionalProductRegistrations" +
            "productRegistrations=$productRegistrations"

    fun forOrderInfoString(): String = "name=$name, serialCode=$serialCode, price=$price"
}