package com.daou.project.daoumall.model.mall

import com.daou.project.daoumall.model.mall.product.MallPaymentManagement
import com.daou.project.daoumall.model.mall.product.additional.AdditionalProduct
import com.daou.project.daoumall.model.mall.product.Product
import com.daou.project.daoumall.model.payment.PaymentManagement
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Table(name = "malls")
@Entity
class Mall(mallName: String, adminName: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mall_id", nullable = false)
    val id: Long? = 0

    @Column(name = "mall_name", nullable = false, unique = true)
    var mallName: String = mallName
        protected set

    @Column(name = "admin_name", nullable = false)
    var adminName: String = adminName
        protected set

    @Column(name = "create_at")
    val createDt: LocalDate = LocalDate.now()

    @OneToMany(
        targetEntity = Product::class,
        fetch = FetchType.LAZY,
        mappedBy = "mall"
    )
    var products: MutableSet<Product> = mutableSetOf()

    @OneToMany(
        targetEntity = AdditionalProduct::class,
        fetch = FetchType.LAZY,
        mappedBy = "mall"
    )
    var additionalProducts: MutableSet<AdditionalProduct> = mutableSetOf()

    @OneToMany(
        targetEntity = MallPaymentManagement::class,
        fetch = FetchType.LAZY,
        mappedBy = "mall"
    )
    @OrderBy(value = "create_date")
    var mallPaymentManagements: MutableSet<MallPaymentManagement> = mutableSetOf()

    override fun toString() = "Mall(id=$id, mallName=$mallName, adminName=$adminName," +
            "products=$products, additionalProducts=$additionalProducts, mallPaymentManagements=$mallPaymentManagements)"

}