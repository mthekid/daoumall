package com.daou.project.daoumall.repository.mall

import com.daou.project.daoumall.model.mall.Mall
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("판매점 생성 테스트[REPO]")
internal class MallRepoTest @Autowired constructor(
    val mallRepository: MallRepository,
){
    companion object {

        // 테스트 판매점
        val t_mallName = "testMall"
        val t_adminName = "testAdmin"
        lateinit var t_mall: Mall

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            t_mall = Mall(mallName = t_mallName, adminName = t_adminName)
        }
    }

    @Test
    internal fun `쇼핑몰을 생성하고 조회 테스트하기`() {

        val savedMall = mallRepository.save(t_mall)
        val foundMall: Mall? = mallRepository.findByMallName(t_mallName)

        assertThat(savedMall).isEqualTo(foundMall)
        if (foundMall != null) {
            assertThat(savedMall.id).isEqualTo(foundMall.id)
        }
    }

    @Test
    internal fun `쇼핑몰 생성 후 조회 실패`(){
        val foundMall: Mall? = mallRepository.findByMallName(t_mallName)

        if (foundMall != null) {
            assertThat(foundMall.mallName).isEqualTo(t_mallName)
        }
    }
    
    @Test
    fun `중복된 쇼핑몰 이름 등록시 에외 발생`() {
        val mall = Mall(mallName = t_mallName, adminName = t_adminName)
        val violatedMall = Mall(mallName = t_mallName, adminName = "testAdmin2")

        mallRepository.save(mall)
        Assertions.assertThrows(DataIntegrityViolationException::class.java) {
            mallRepository.save(violatedMall)
        }
    }
}
