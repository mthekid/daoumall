package com.daou.project.daoumall.model.payment.connectionTable

import com.daou.project.daoumall.model.mall.product.Product
import com.daou.project.daoumall.model.payment.PaymentManagement
import org.hibernate.criterion.Order
import javax.persistence.*


/*
[paymentManagement]와 [Product] & [Additional Product]와의 연결 테이블이다.
*/
@Entity
class OrderPaper(
    val count: Int,
    val basePrice: Long,
    val productName: String,
    val essentialSerialCode: String,
    val essentialOptionPrice: Int,
    val essentialOptionName: String
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_paper_id")
    var id: Long = 0

    @ManyToOne(
        targetEntity = Product::class,
        fetch = FetchType.EAGER
    )
    @JoinColumn(name = "product_id")
    var product: Product? = null

    @ManyToOne(
        targetEntity = PaymentManagement::class,
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "payment_management_id")
    var paymentManagement: PaymentManagement? = null

    @OneToMany(
        targetEntity = OrderWithAdditionalProduct::class,
        mappedBy = "orderPaper",
        fetch = FetchType.EAGER
    )
    var orderWithAdditionalProducts: MutableSet<OrderWithAdditionalProduct> = mutableSetOf()

    override fun toString(): String = "orderWithAdditionalProducts=$orderWithAdditionalProducts" +
            ", productName=$productName, basePrice=$basePrice, count=$count, " +
            ", essentialOptionName=$essentialOptionName, essentialOptionPrice=$essentialOptionPrice"
}