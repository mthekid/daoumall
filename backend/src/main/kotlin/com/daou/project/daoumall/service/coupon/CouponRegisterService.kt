package com.daou.project.daoumall.service.coupon

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.coupondomain.AmountCouponDTO
import com.daou.project.daoumall.dto.coupondomain.RateCouponDTO
import com.daou.project.daoumall.dto.coupondomain.offer.OfferCouponDTO
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.coupon.AmountCoupon
import com.daou.project.daoumall.model.coupon.RateCoupon
import com.daou.project.daoumall.model.coupon.connectionTable.CustomerAmountCoupon
import com.daou.project.daoumall.model.coupon.connectionTable.CustomerRateCoupon
import com.daou.project.daoumall.repository.coupon.AmountCouponRepository
import com.daou.project.daoumall.repository.coupon.RateCouponRepository
import com.daou.project.daoumall.repository.customer.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponRegisterService(
    private val amountCouponRepo: AmountCouponRepository,
    private val rateCouponRepo: RateCouponRepository,
    private val customerRepo: CustomerRepository
) {
    @Transactional
    fun offerAmountCouponToCustomer(offerCouponDto: OfferCouponDTO): OfferCouponDTO.OfferCouponRESDTO {
        Log.info("금액 쿠폰을 사용자에게 발행합니다. [CouponRegisterService <offerAmountCouponToCustomer> start]")

        val foundCustomer = getCustomerByLoginId(offerCouponDto.customerLoginId)
        Log.info("찾은 고객 $foundCustomer")

        val foundAmountCoupon = amountCouponRepo.findBySerialCode(offerCouponDto.couponSerialCode)
        Log.info("찾은 금액 쿠폰 $foundAmountCoupon")

        for(customerAmountCoupon in foundCustomer.customerAmountCoupons) {
            if(customerAmountCoupon.amountCoupon == foundAmountCoupon) {
                throw BaseException(CustomResponseCode.ALREADY_OFFERED_COUPON)
            }
        }

        val customerAmountCoupon = CustomerAmountCoupon(mallName = offerCouponDto.mallName)
        customerAmountCoupon.customer = foundCustomer
        customerAmountCoupon.amountCoupon = foundAmountCoupon

        foundCustomer.customerAmountCoupons.add(customerAmountCoupon)

        Log.info("사용자 - 쿠폰 등록 서비스 끝. [CouponRegisterService <offerAmountCouponToCustomer> END]")
        return OfferCouponDTO.fromAmountEntity(foundCustomer, foundAmountCoupon!!)
    }

    private fun getCustomerByLoginId(customerLoginId: String): Customer {
        return customerRepo.findByLoginId(customerLoginId) ?: throw BaseException(CustomResponseCode.NOTFOUND_CUSTOMER)
    }

    @Transactional
    fun offerRateCouponToCustomer(offerCouponDto: OfferCouponDTO): OfferCouponDTO.OfferCouponRESDTO {
        Log.info("비율 할인 쿠폰을 사용자에게 발급했습니다. [CouponRegisterService <offerRateCouponToCustomer> start]")
        
        val foundCustomer = getCustomerByLoginId(offerCouponDto.customerLoginId)
        val foundRateCoupon = rateCouponRepo.findBySerialCode(offerCouponDto.couponSerialCode)

        for(contable in foundCustomer.customerRateCoupons) {
            if(contable.rateCoupon == foundRateCoupon) {
                throw BaseException(CustomResponseCode.ALREADY_OFFERED_COUPON)
            }
        }

        var customerRateCoupon = CustomerRateCoupon(mallName = offerCouponDto.mallName)
        customerRateCoupon.customer = foundCustomer
        customerRateCoupon.rateCoupon = foundRateCoupon
        foundCustomer.customerRateCoupons.add(customerRateCoupon)

        Log.info("비율 할인 쿠폰을 사용자에게 발급했습니다. [CouponRegisterService <offerRateCouponToCustomer> end]")
        return OfferCouponDTO.fromRateEntity(customer = foundCustomer, rateCoupon = foundRateCoupon!!)
    }
    
    @Transactional
    fun createAmountCoupon(amountCoupon: AmountCoupon): AmountCouponDTO.AmountCouponRESDTO {
        Log.info("금액 쿠폰을 발행합니다. [ CouponRegisterService <createAmountCoupon> start]")
        if(amountCouponRepo.findBySerialCode(amountCoupon.serialCode) != null) throw BaseException(CustomResponseCode.ALREADY_EXSISTS_COUPON)
        val savedAmountCoupon = amountCouponRepo.save(amountCoupon)

        Log.info("발행된 금액 쿠폰 정보. $savedAmountCoupon[ CouponRegisterService <createAmountCoupon> end]")
        return AmountCouponDTO.fromEntity(savedAmountCoupon)
    }

    @Transactional
    fun createRateCoupon(rateCoupon: RateCoupon): RateCouponDTO.RateCouponRESDTO {
        Log.info("금액 쿠폰을 발행합니다. [ CouponRegisterService <createRateCoupon> start]")
        if(rateCouponRepo.findBySerialCode(rateCoupon.serialCode) != null) throw BaseException(CustomResponseCode.ALREADY_EXSISTS_COUPON)
        val savedRateCoupon = rateCouponRepo.save(rateCoupon)

        Log.info("발행된 비율 할인 쿠폰 정보. $savedRateCoupon[ CouponRegisterService <createRateCoupon> end]")
        return RateCouponDTO.fromEntity(savedRateCoupon)
    }
}