package com.daou.project.daoumall.dto.malldomain

import com.daou.project.daoumall.model.mall.Mall

data class MallDTO(
    val mallName: String,
    val adminName: String
) {

    data class MallRESDTO(
        val mallName: String,
        val status: String
    )

    fun toEntity(): Mall {
        return Mall(
            mallName = mallName,
            adminName = adminName
        )
    }

    companion object {
        fun fromEntity(mall: Mall?): MallRESDTO? {
            if (mall == null) {
                return null
            }
            return MallRESDTO(
                mallName = mall.mallName,
                status = "OK"
            )
        }
    }
}