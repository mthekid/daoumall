package com.daou.project.daoumall.service.coupon

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.coupondomain.use.CanUseCouponDTO
import com.daou.project.daoumall.dto.coupondomain.use.UsedCouponDTO
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.daou.project.daoumall.model.coupon.RateCoupon
import com.daou.project.daoumall.model.coupon.connectionTable.USED
import com.daou.project.daoumall.repository.customer.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class CouponFindService(
    private val customerRepo: CustomerRepository
) {

    @Transactional(readOnly = true)
    fun findMostEfficiencyAmountCoupon(customerLoginId: String, mallName: String): CanUseCouponDTO {
        Log.info("$customerLoginId 가 사용할 수 있는 금액 쿠폰 목록을 반환합니다. [CouponFindService <findCanUseAmountCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val amountCouponContables = foundCustomer.customerAmountCoupons
        val filteredNotUsedAmountCouponContables = amountCouponContables.filter {
            it.isUsed == USED.NO && it.amountCoupon!!.expiredDate > LocalDate.now() && it.mallName == mallName
        }

        val sortedCanUseCoupons = filteredNotUsedAmountCouponContables.sortedWith(
            compareBy(
                { it.amountCoupon!!.discountAmount },
                { it.amountCoupon!!.expiredDate },
                { -it.amountCoupon!!.limitPrice }
            )
        )

        Log.info("$customerLoginId 가 사용할 수 있는 금액 쿠폰 목록을 반환했습니다. [CouponFindService <findCanUseAmountCoupons> end]")
        return CanUseCouponDTO.fromAmountCouponEntity(sortedCanUseCoupons[0].amountCoupon!!)
    }

    @Transactional(readOnly = true)
    fun findCanUseAmountCoupons(customerLoginId: String): Set<CanUseCouponDTO> {
        Log.info("$customerLoginId 가 사용할 수 있는 금액 쿠폰 목록을 반환합니다. [CouponFindService <findCanUseAmountCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val amountCouponContables = foundCustomer.customerAmountCoupons
        val filteredNotUsedAmountCouponContables = amountCouponContables.filter {
            it.isUsed == USED.NO && it.amountCoupon!!.expiredDate >= LocalDate.now()
        }

        Log.info("사용하지 않은 금액 쿠폰의 정보 $filteredNotUsedAmountCouponContables")

        val sortedNotUsedCoupons = filteredNotUsedAmountCouponContables.sortedWith(
            compareBy(
                { -it.amountCoupon!!.discountAmount },
                { it.amountCoupon!!.expiredDate },
                { it.amountCoupon!!.limitPrice }
            )
        )

        var canUsedCouponsDtos = mutableSetOf<CanUseCouponDTO>()
        for (contable in sortedNotUsedCoupons) {
            canUsedCouponsDtos.add(CanUseCouponDTO.fromAmountCouponEntity(contable.amountCoupon!!))
        }

        Log.info("$customerLoginId 가 사용할 수 있는 금액 쿠폰 목록을 반환했습니다. [CouponFindService <findCanUseAmountCoupons> end]")
        return canUsedCouponsDtos
    }

    @Transactional(readOnly = true)
    fun findCanUseAmountCoupons(customerLoginId: String, mallName: String): Set<CanUseCouponDTO> {
        Log.info("$customerLoginId 가 사용할 수 있는 금액 쿠폰 목록을 반환합니다. [CouponFindService <findCanUseAmountCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val amountCouponContables = foundCustomer.customerAmountCoupons
        val filteredNotUsedAmountCouponContables = amountCouponContables.filter {
            it.isUsed == USED.NO && it.amountCoupon!!.expiredDate >= LocalDate.now() && it.amountCoupon!!.mallName == mallName
        }

        Log.info("사용하지 않은 금액 쿠폰의 정보 $filteredNotUsedAmountCouponContables")

        val sortedNotUsedCoupons = filteredNotUsedAmountCouponContables.sortedWith(
            compareBy(
                { -it.amountCoupon!!.discountAmount },
                { it.amountCoupon!!.expiredDate },
                { it.amountCoupon!!.limitPrice }
            )
        )

        var canUsedCouponsDtos = mutableSetOf<CanUseCouponDTO>()
        for (contable in sortedNotUsedCoupons) {
            canUsedCouponsDtos.add(CanUseCouponDTO.fromAmountCouponEntity(contable.amountCoupon!!))
        }

        Log.info("$customerLoginId 가 사용할 수 있는 금액 쿠폰 목록을 반환했습니다. [CouponFindService <findCanUseAmountCoupons> end]")
        return canUsedCouponsDtos
    }

    @Transactional(readOnly = true)
    fun findMostEfficientAmountCoupon(customerLoginId: String, mallName: String, originalPrice: Long): AmountCoupon {
        Log.info("$customerLoginId 가 사용할 수 있는 금액 쿠폰 목록을 반환합니다. 판매점:$mallName, 가격:$originalPrice [CouponFindService <findCanUseAmountCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val amountCouponContables = foundCustomer.customerAmountCoupons

        Log.info("보유 금액 쿠폰 목록 $amountCouponContables")
        val canUseAmountCoupons = amountCouponContables.filter {
            it.isUsed == USED.NO && it.amountCoupon!!.expiredDate >= LocalDate.now() && it.amountCoupon!!.mallName == mallName && it.amountCoupon!!.limitPrice <= originalPrice
        }


        if (canUseAmountCoupons.isEmpty()) {
            return AmountCoupon(
                couponInfo = "NONE", discountAmount = 0L, expiredDate = LocalDate.now(), limitPrice = 1000000L,
                mallName = mallName, serialCode = "none"
            )
        }
        Log.info("사용 가능한 금액 쿠폰 목록 $canUseAmountCoupons")

        val sortedAmountCoupons = canUseAmountCoupons.sortedWith(
            compareBy(
                { -it.amountCoupon!!.discountAmount },
                { it.amountCoupon!!.expiredDate },
                { -it.amountCoupon!!.limitPrice }
            )
        )
        Log.info("정렬된 금액 쿠폰 목록 $sortedAmountCoupons")

        Log.info("$customerLoginId 가 사용할 수 있는 금액 쿠폰 목록을 반환했습니다. [CouponFindService <findCanUseAmountCoupons> end]")
        return sortedAmountCoupons[0].amountCoupon!!
    }

    private fun getCustomerByLoginId(customerLoginId: String): Customer {
        return customerRepo.findByLoginId(customerLoginId) ?: throw BaseException(CustomResponseCode.NOTFOUND_CUSTOMER)
    }

    @Transactional(readOnly = true)
    fun findUsedAmountCoupons(customerLoginId: String): Set<UsedCouponDTO> {
        Log.info("$customerLoginId 가 사용한 모든 금액 쿠폰을 반환합니다. [CouponFindService <findAllUsedAmountCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val customerAmountCoupons = foundCustomer.customerAmountCoupons
        val filteredUsedAmountCoupons = customerAmountCoupons.filter {
            it.isUsed == USED.YES
        }

        Log.info("사용한 금액 쿠폰의 테이블 정보 $customerAmountCoupons")
        Log.info("사용한 금액 쿠폰의 정보 $filteredUsedAmountCoupons")

        var usedAmountCoupons = mutableListOf<AmountCoupon>()
        var usedAmountCouponsDtos = mutableSetOf<UsedCouponDTO>()
        var usedDates = mutableListOf<LocalDateTime>()

        if (filteredUsedAmountCoupons.isNotEmpty()) {
            for (usedAmountCoupon in filteredUsedAmountCoupons) {
                usedAmountCoupons.add(usedAmountCoupon.amountCoupon!!)
                usedDates.add(usedAmountCoupon.usedDate!!)
            }

            for ((idx, usedAmountCoupon) in usedAmountCoupons.withIndex()) {
                usedAmountCouponsDtos.add(
                    UsedCouponDTO.fromAmountCouponEntity(
                        usedAmountCoupon,
                        usedDate = usedDates[idx]
                    )
                )
            }
        }
        Log.info("$customerLoginId 가 사용한 모든 금액 쿠폰을 반환합니다. [CouponFindService <findAllUsedAmountCoupons> end]")
        return usedAmountCouponsDtos
    }

    @Transactional(readOnly = true)
    fun findCanUseRateCoupons(customerLoginId: String): Set<CanUseCouponDTO> {
        Log.info("$customerLoginId 가 사용할 수 있는 비율 할인 쿠폰 목록을 반환합니다. [CouponFindService <findCanUseRateCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val customerRateCoupons = foundCustomer.customerRateCoupons
        val canUseRateCoupons = customerRateCoupons.filter {
            it.isUsed == USED.NO && it.rateCoupon!!.expiredDate >= LocalDate.now()
        }

        val sortedRateCoupons = canUseRateCoupons.sortedWith(
            compareBy(
                { it.rateCoupon!!.rate },
                { it.rateCoupon!!.expiredDate },
                { it.rateCoupon!!.upperBoundPrice }
            )
        )

        var canUseRateCouponsDtos = mutableSetOf<CanUseCouponDTO>()

        for (canUseRateCoupon in sortedRateCoupons) {
            canUseRateCouponsDtos.add(CanUseCouponDTO.fromRateCouponEntity(canUseRateCoupon.rateCoupon!!))
        }

        Log.info("$customerLoginId 가 사용할 수 있는 비율 할인 쿠폰 목록을 반환했습니다. [CouponFindService <findCanUseAmountCoupons> end]")
        return canUseRateCouponsDtos
    }

    @Transactional(readOnly = true)
    fun findCanUseRateCoupons(customerLoginId: String, mallName: String): Set<CanUseCouponDTO> {
        Log.info("$customerLoginId 가 사용할 수 있는 비율 할인 쿠폰 목록을 반환합니다. [CouponFindService <findCanUseRateCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val customerRateCoupons = foundCustomer.customerRateCoupons
        val canUseRateCoupons = customerRateCoupons.filter {
            it.isUsed == USED.NO && it.rateCoupon!!.expiredDate >= LocalDate.now() && it.rateCoupon!!.mallName == mallName
        }

        val sortedRateCoupons = canUseRateCoupons.sortedWith(
            compareBy(
                { it.rateCoupon!!.rate },
                { it.rateCoupon!!.expiredDate },
                { it.rateCoupon!!.upperBoundPrice }
            )
        )

        var canUseRateCouponsDtos = mutableSetOf<CanUseCouponDTO>()

        for (canUseRateCoupon in sortedRateCoupons) {
            canUseRateCouponsDtos.add(CanUseCouponDTO.fromRateCouponEntity(canUseRateCoupon.rateCoupon!!))
        }

        Log.info("$customerLoginId 가 사용할 수 있는 비율 할인 쿠폰 목록을 반환했습니다. [CouponFindService <findCanUseAmountCoupons> end]")
        return canUseRateCouponsDtos
    }

    @Transactional(readOnly = true)
    fun findMostEfficientRateCoupon(
        customerLoginId: String,
        mallName: String,
        originalPrice: Long
    ): CouponUseService.AppliedRateCouponInfo {
        Log.info("$customerLoginId 가 사용할 수 있는 비율 할인 쿠폰 목록을 반환합니다. [CouponFindService <findCanUseRateCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val customerRateCoupons = foundCustomer.customerRateCoupons

        Log.info("보유 비율 쿠폰 목록 $customerRateCoupons")
        val canUseRateCoupons = customerRateCoupons.filter {
            it.isUsed == USED.NO && it.rateCoupon!!.expiredDate >= LocalDate.now() && it.rateCoupon!!.mallName == mallName
        }
        Log.info("사용 가능한 비율 쿠폰 목록 $canUseRateCoupons")

        if (canUseRateCoupons.isEmpty()) {
            return CouponUseService.AppliedRateCouponInfo(
                discountPrice = 0,
                upperBound = 0,
                expiredDt = LocalDate.now(),
                serialCode = "NONE",
                rate = 0
            )
        }

        var appliedRateCouponList: MutableList<CouponUseService.AppliedRateCouponInfo> = mutableListOf()
        for (canUseRateCoupon in canUseRateCoupons) {
            var discountPrice = (originalPrice * canUseRateCoupon.rateCoupon!!.rate) / 100;
            if (discountPrice >= canUseRateCoupon.rateCoupon!!.upperBoundPrice) discountPrice =
                canUseRateCoupon.rateCoupon!!.upperBoundPrice

            appliedRateCouponList.add(
                CouponUseService.AppliedRateCouponInfo(
                    discountPrice = discountPrice,
                    upperBound = canUseRateCoupon.rateCoupon!!.upperBoundPrice,
                    expiredDt = canUseRateCoupon.rateCoupon!!.expiredDate,
                    serialCode = canUseRateCoupon.rateCoupon!!.serialCode,
                    rate = canUseRateCoupon.rateCoupon!!.rate
                )
            )
        }

        val sortedRateCoupons = appliedRateCouponList.sortedWith(
            compareBy(
                { -it.discountPrice },
                { it.upperBound },
                { it.expiredDt },
                { -it.rate },
            )
        )
        Log.info("정렬된 비율 쿠폰 목록 $sortedRateCoupons")

        Log.info("$customerLoginId 가 사용할 수 있는 비율 할인 쿠폰 목록을 반환했습니다. [CouponFindService <findCanUseAmountCoupons> end]")
        return sortedRateCoupons[0]
    }

    @Transactional(readOnly = true)
    fun findUsedRateCoupons(customerLoginId: String): Set<UsedCouponDTO> {
        Log.info("$customerLoginId 가 사용한 비율 할인 쿠폰 목록을 반환합니다. [CouponFindService <findUsedRateCoupons> start]")
        val foundCustomer = getCustomerByLoginId(customerLoginId)

        val customerRateCoupons = foundCustomer.customerRateCoupons
        val filteredUsedRateCoupons = customerRateCoupons.filter {
            it.isUsed == USED.YES
        }

        Log.info("사용한 비율 할인 쿠폰의 테이블 정보 $customerRateCoupons")
        Log.info("사용한 비율 할인 쿠폰의 정보 $filteredUsedRateCoupons")

        var usedRateCoupons = mutableListOf<RateCoupon>()
        var usedDts = mutableListOf<LocalDateTime>()

        if (filteredUsedRateCoupons.isNotEmpty()) {
            for (usedRateCoupon in filteredUsedRateCoupons) {
                usedRateCoupons.add(usedRateCoupon.rateCoupon!!)
                usedDts.add(usedRateCoupon.usedDate!!)
            }
        }

        var usedRateCouponsDtos = mutableSetOf<UsedCouponDTO>()
        for ((idx, usedRateCoupon) in usedRateCoupons.withIndex()) {
            usedRateCouponsDtos.add(UsedCouponDTO.fromRateCouponEntity(usedRateCoupon, usedDate = usedDts[idx]))
        }

        Log.info("$customerLoginId 가 사용한 비율 할인 쿠폰 목록을 반환합니다. [CouponFindService <findUsedRateCoupons> end]")
        return usedRateCouponsDtos
    }
}