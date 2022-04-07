package com.daou.project.daoumall.model.mall.product.essential

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Table(name = "essential_options")
@Entity
class EssentialOption(
    optionName: String,
    metaInfo: String,
    val price: Int,
    @Column(unique = true)
    val serialCode: String
    )
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "essential_option_id")
    val id: Long = 0

    @Column(nullable = false)
    var optionName = optionName

    @Column(name = "meta_information")
    var metaInfo = metaInfo

    val createDate: LocalDateTime = LocalDateTime.now()

    // 일대다 ProductRegistration과 등록
    @OneToMany(targetEntity = ProductRegistration::class,
        mappedBy = "essentialOption",
        cascade = [CascadeType.ALL])
    @JsonIgnore
    var productRegistrations: MutableSet<ProductRegistration> = mutableSetOf()

    override fun toString(): String = "essentialOption(id=$id, optionName=$optionName, metaInfo=$metaInfo, price=$price)"
}