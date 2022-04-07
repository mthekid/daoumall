package com.daou.project.daoumall.service.payment

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.coupondomain.use.CATEGORY
import com.daou.project.daoumall.dto.coupondomain.use.UsedCouponDTO
import com.daou.project.daoumall.dto.malldomain.ProductDTO
import com.daou.project.daoumall.dto.malldomain.additional.AdditionalProductOrderPaperDTO
import com.daou.project.daoumall.dto.malldomain.essential.EssentialOptionDTO
import com.daou.project.daoumall.dto.paymentdomain.CanceledPaymentInfoDTO
import com.daou.project.daoumall.dto.paymentdomain.OrderPaperDTO
import com.daou.project.daoumall.dto.paymentdomain.PaymentManagementDTO
import com.daou.project.daoumall.dto.paymentdomain.PointAmountDTO
import com.daou.project.daoumall.dto.paymentdomain.method.BankAccountDTO
import com.daou.project.daoumall.dto.paymentdomain.method.CardDTO
import com.daou.project.daoumall.dto.paymentdomain.point.CanceledPointDTO
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.GRADE
import com.daou.project.daoumall.model.coupon.connectionTable.USED
import com.daou.project.daoumall.model.payment.PAYMENTMEHTOD
import com.daou.project.daoumall.model.payment.PaymentManagement
import com.daou.project.daoumall.model.payment.PaymentUsePoint
import com.daou.project.daoumall.model.payment.STATUS
import com.daou.project.daoumall.model.payment.method.Card
import com.daou.project.daoumall.model.point.Point
import com.daou.project.daoumall.model.point.connectionTable.CustomerPoint
import com.daou.project.daoumall.repository.customer.CustomerRepository
import com.daou.project.daoumall.repository.paymentmanagement.PaymentManagementRepository
import com.daou.project.daoumall.repository.paymentmanagement.connectiontable.CustomerPointRepository
import com.daou.project.daoumall.repository.paymentmanagement.method.BankAccountRepository
import com.daou.project.daoumall.repository.paymentmanagement.method.CardRepository
import com.daou.project.daoumall.repository.paymentmanagement.point.PaymentUsePointRepository
import com.daou.project.daoumall.repository.paymentmanagement.point.PointRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class PaymentManagementService(
    private val paymentManagementRepo: PaymentManagementRepository,
    private val pointRepo: PointRepository,
    private val customerPointRepo: CustomerPointRepository,
    private val bankAccountRepo: BankAccountRepository,
    private val cardRepo: CardRepository,
    private val paymentUsePointRepo: PaymentUsePointRepository,
    private val orderPaperService: OrderPaperService,
    private val customerRepo: CustomerRepository
) {
    @Transactional(readOnly = true)
    fun getOrderSpecification(orderSerialCode: String): Set<OrderPaperDTO.OrderPaperRESDTO> {
        Log.info("${orderSerialCode}의 상세 구매 내역을 살펴봅니다.")
        val foundPaymentManagement = paymentManagementRepo.findByOrderSerialCode(orderSerialCode) ?: throw BaseException(CustomResponseCode.NOTFOUND_PAYMENT)

        val orderPapers = foundPaymentManagement.orderPapers
        val anyOrderPaper = orderPapers.iterator().next()
        var productInfoResDto : ProductDTO.ProductRESDTO = ProductDTO.ProductRESDTO(
            name = anyOrderPaper.productName,
            price = anyOrderPaper.basePrice,
        )

        var orderPaperRESDtos : MutableSet<OrderPaperDTO.OrderPaperRESDTO> = mutableSetOf()
        for(orderPaper in orderPapers) {
            var essentialOptionResDto = EssentialOptionDTO.EssentialOptionRESDTO(
                name = orderPaper.essentialOptionName,
                price = orderPaper.essentialOptionPrice
            )

            var additionalProductOrderPaperDtos: MutableSet<AdditionalProductOrderPaperDTO> = mutableSetOf()
            val orderWithAdditionalProducts = orderPaper.orderWithAdditionalProducts

            Log.info("해당 주문 내역의 추가 상품 테이블 정보 $orderWithAdditionalProducts")
            for(orderWithAdditionalProduct in orderWithAdditionalProducts) {
                val additionalProductOrderPaperDto = AdditionalProductOrderPaperDTO(
                    name = orderWithAdditionalProduct.additionalProductName,
                    price = orderWithAdditionalProduct.additionalProductPrice
                )
                additionalProductOrderPaperDtos.add(additionalProductOrderPaperDto)
                Log.info("추가한 주문-추가상품 DTO 정보 $orderWithAdditionalProducts")
            }

            val count = orderPaper.count
            orderPaperRESDtos.add(
                OrderPaperDTO.OrderPaperRESDTO(
                    productRESDto = productInfoResDto,
                    essentialOptionRESDto = essentialOptionResDto,
                    additionalProductOrderPaperDtos = additionalProductOrderPaperDtos,
                    count = count
                )
            )
        }
        Log.info("${orderSerialCode}의 상세 구매 내역을 조회했습니다. 반환 결과 $orderPaperRESDtos.")
        return orderPaperRESDtos
    }
    @Transactional
    fun createPaymentManagement(
        loginId: String,
        paymentManagement: PaymentManagement,
        orderPaperDtos: Set<OrderPaperDTO>
    ): PaymentManagementDTO.PaymentManagementRESDTO {
        Log.info("결제 내역을 발행합니다. [PaymentManagementService <createPaymentManagement> start]")
        val foundCustomer = getCustomerByLoginId(loginId)

        usePoints(foundCustomer, paymentManagement)
        Log.info("포인트 사용 완료. ${paymentManagement.usedPointAmount}를 사용했습니다.")

        val payResInfo: BankAccountDTO.AfterPayRESDTO =
            when (paymentManagement.paymentMethod) {
                PAYMENTMEHTOD.CARD -> payByCard(
                    foundCustomer,
                    paymentManagement.paymentSerialCode,
                    paymentManagement.payAmount
                )
                PAYMENTMEHTOD.BANKACCOUNT -> payByBankAccount(
                    foundCustomer,
                    paymentManagement.paymentSerialCode,
                    paymentManagement.payAmount
                )
            }

        val couponUseInfo =
            when (paymentManagement.couponCategory) {
                CATEGORY.AMOUNT -> useAmountCoupon(foundCustomer, paymentManagement.couponSerialCode)
                CATEGORY.RATE -> useRateCoupon(foundCustomer, paymentManagement.couponSerialCode)
                CATEGORY.NONE -> UsedCouponDTO(
                    discountInfo = 0,
                    createDate = null,
                    constraint = 0,
                    serialCode = "none",
                    couponCategory = CATEGORY.NONE
                )
            }

        Log.info("결제내역 발행하기")
        paymentManagement.customer = foundCustomer
        val savedPaymentManagement = paymentManagementRepo.save(paymentManagement)

        val orderPapers = orderPaperService.createOrderPaper(orderPaperDtos = orderPaperDtos)
        var buyProductsInfoDTOs: MutableSet<OrderPaperDTO.BuyProductsInfoDTO> = mutableSetOf()
        for (orderPaper in orderPapers) {
            orderPaper.paymentManagement = savedPaymentManagement
            buyProductsInfoDTOs.add(OrderPaperDTO.fromOrderPaperEntity(orderPaper))
        }

        Log.info("${foundCustomer.loginId}에 ${savedPaymentManagement}를 발행했습니다.")
        Log.info("결제 내역을 발행했습니다. [PaymentManagementService <createPaymentManagement> end]")
        return PaymentManagementDTO.fromEntityAndPayInfoAndCouponInfoAndOrderPaperInfo(
            paymentManagement = savedPaymentManagement,
            payInfo = payResInfo,
            couponInfo = couponUseInfo,
            buyProductsInfoDTOs = buyProductsInfoDTOs
        )
    }

    private fun getCustomerByLoginId(loginId: String): Customer {
        return customerRepo.findByLoginId(loginId) ?: throw BaseException(CustomResponseCode.NOTFOUND_CUSTOMER)
    }

    @Transactional
    fun usePoints(customer: Customer, paymentManagement: PaymentManagement) {
        Log.info("${customer.userName}의 포인트 내역 중 ${paymentManagement.usedPointAmount}을 사용하는 로직 진행. [PaymentManagementService <usePoints> start ]")

        var usedPointAmount = paymentManagement.usedPointAmount
        var canUsePointAmount: Long = 0

        val customerPoints = customer.customerPoints
        Log.info("사용자의 포인트 내역 정보 $customerPoints")

        var savedPoints: MutableList<Point> = mutableListOf()
        for (customerPoint in customerPoints) {
            val point = customerPoint.point
            if (point!!.remainAmount != 0L && point.expiredDateTime >= LocalDateTime.now()) {
                savedPoints.add(point)
                canUsePointAmount += point.remainAmount
            }
        }

        if (canUsePointAmount < usedPointAmount) throw BaseException(CustomResponseCode.OVERUSE_REMAIN_POINT)


        val sortedSavedPoints = savedPoints.sortedWith(
            compareBy { it.expiredDateTime }
        )

        for (savedPoint in sortedSavedPoints) {
            if (usedPointAmount == 0L) {
                Log.info("더이상 사용할 금액이 없습니다.")
                break
            } else if (usedPointAmount >= savedPoint.remainAmount) {
                Log.info("${usedPointAmount}는 보유한 포인트 : ${savedPoint.remainAmount}보다 더 많습니다. 보유 포인트를 사용하고 다음 포인트로 넘어갑니다.")
                usedPointAmount -= savedPoint.remainAmount
                val paymentUsePoint = PaymentUsePoint(
                    orderSerialCode = paymentManagement.orderSerialCode,
                    usedPoint = savedPoint.remainAmount
                )
                savedPoint.remainAmount = 0
                paymentUsePoint.point = savedPoint

                val savedPaymentUsePoint = paymentUsePointRepo.save(paymentUsePoint)
                Log.info("저장된 포인트 사용 내역 $savedPaymentUsePoint")
                savedPoint.paymentUsePoint.add(savedPaymentUsePoint)
                Log.info("포인트 정보 $savedPoint")
            } else {
                Log.info("사용할 포인트 ${usedPointAmount}가 현재 보유한 포인트 : ${savedPoint.remainAmount}보다 작습니다. ")
                val paymentUsePoint =
                    PaymentUsePoint(orderSerialCode = paymentManagement.orderSerialCode, usedPoint = usedPointAmount)
                savedPoint.remainAmount -= usedPointAmount
                usedPointAmount = 0
                paymentUsePoint.point = savedPoint

                val savedPaymentUsePoint = paymentUsePointRepo.save(paymentUsePoint)
                Log.info("마지막으로 저장된 포인트 사용 내역 $savedPaymentUsePoint")
                savedPoint.paymentUsePoint.add(savedPaymentUsePoint)
                Log.info("마지막으로 처리된 포인트 정보 $savedPoint")
            }
        }

        Log.info("${customer.userName}의 포인트 내역 중 ${paymentManagement.usedPointAmount} 사용 완료 [PaymentManagementService <usePoints> end]")
    }

    @Transactional
    fun useAmountCoupon(customer: Customer, serialCode: String): UsedCouponDTO {
        Log.info("${customer.userName}의 $serialCode 금액 쿠폰을 사용합니다. [ PaymentManagementService <useAmountCoupon> start]")
        var foundCustomerAmountCoupons = customer.customerAmountCoupons

        var couponConTables = foundCustomerAmountCoupons.find {
            it.amountCoupon!!.serialCode == serialCode
        }

        val useAmountCoupon = couponConTables!!.amountCoupon ?: throw BaseException(CustomResponseCode.NOTFOUND_COUPON)

        couponConTables.usedDate = LocalDateTime.now()
        couponConTables.isUsed = USED.YES

        Log.info("${customer.userName}의 $serialCode 금액 쿠폰을 사용했습니다. [ PaymentManagementService <useAmountCoupon> end]")
        return UsedCouponDTO.fromAmountCouponEntity(useAmountCoupon, usedDate = LocalDateTime.now())
    }

    @Transactional
    fun useRateCoupon(customer: Customer, serialCode: String): UsedCouponDTO {
        Log.info("${customer.userName}의 $serialCode 비율 할인 쿠폰을 사용합니다. [ PaymentManagementService <useRateCoupon> start]")
        var foundCustomerRateCoupons = customer.customerRateCoupons

        var couponContableEntity = foundCustomerRateCoupons.find {
            it.rateCoupon!!.serialCode == serialCode
        }

        val useRateCoupon = couponContableEntity!!.rateCoupon

        couponContableEntity.usedDate = LocalDateTime.now()
        couponContableEntity.isUsed = USED.YES

        Log.info("${customer.userName}의 $serialCode 비율 할인 쿠폰을 사용했습니다. [ PaymentManagementService <useRateCoupon> end]")
        return UsedCouponDTO.fromRateCouponEntity(useRateCoupon!!, usedDate = LocalDateTime.now())
    }

    @Transactional
    fun payByBankAccount(customer: Customer, serialInfo: String, payAmount: Long): BankAccountDTO.AfterPayRESDTO {
        Log.info("${customer.userName}의 은행 계좌로 결제를 진행합니다. [ PaymentManagementService <payByBankAccount> start]")
        var foundBankAccount = bankAccountRepo.findBySecretSerialInfo(serialInfo) ?: throw BaseException(CustomResponseCode.NOTFOUND_BANKACCOUNT)
        try {
            foundBankAccount.pay(payAmount)
        } catch (e: BaseException) {
            throw BaseException(CustomResponseCode.OVERPAYMENT)
        }

        Log.info("${customer.userName}의 은행 계좌로 결제를 완료했습니다. [ PaymentManagementService <payByBankAccount> end]")
        return BankAccountDTO.afterPay(customer = customer, bankAccount = foundBankAccount, totalPrice = payAmount)
    }

    @Transactional
    fun payByCard(customer: Customer, serialInfo: String, payAmount: Long): BankAccountDTO.AfterPayRESDTO {
        Log.info("${customer.userName}의 카드로 결제를 진행합니다. [ PaymentManagementService <payByCard> start]")
        var foundCard = getCardBySerialInfo(serialInfo)
        try {
            foundCard.pay(payAmount)
        } catch (e: BaseException) {
            throw BaseException(CustomResponseCode.OVERPAYMENT)
        }

        Log.info("${customer.userName}의 카드로 결제를 진행합니다. [ PaymentManagementService <payByCard> end]")
        return CardDTO.afterPay(customer = customer, card = foundCard, totalPrice = payAmount)
    }

    @Transactional
    fun completePaymentManagement(loginId: String, paymentManagementSerialCode: String): PointAmountDTO {
        Log.info("결제 내역의 진행 중(Progress)... 상태를 완료(Complete)로 변경합니다. [PaymentManagementService <completePaymentManagement> start]")
        val foundCustomer = getCustomerByLoginId(loginId)

        val foundPaymentManagement = paymentManagementRepo.findByOrderSerialCode(paymentManagementSerialCode) ?: throw BaseException(CustomResponseCode.NOTFOUND_PAYMENT)

        if (foundPaymentManagement.status == STATUS.COMPLETE) {
            throw BaseException(CustomResponseCode.ALREADY_COMPLETE_PAYMENT)
        } else if (foundPaymentManagement.status == STATUS.CANCEL) {
            throw BaseException(CustomResponseCode.ALREADY_CANCLE_PAYMENT)
        }

        foundPaymentManagement.status = STATUS.COMPLETE
        foundPaymentManagement.modifiedDate = LocalDate.now()

        Log.info("변경된 결제 내역 상태 확인 $foundPaymentManagement")
        val pointAmountDto = createPoint(customer = foundCustomer, paymentManagement = foundPaymentManagement)

        Log.info("결제 내역의 상태를 완료로 변경 완료했습니다. [PaymentManagementService <completePaymentManagement> end]")
        return pointAmountDto
    }

    @Transactional
    fun createPoint(customer: Customer, paymentManagement: PaymentManagement): PointAmountDTO {
        Log.info("결제내역이 확정되어 ${customer.loginId}에 포인트를 추가합니다. [PaymentManagementService <completePaymentManagement> start]")

        val pointAmount: Long = (paymentManagement.payAmount * GRADE.getRatio(customer.grade)) / 100
        val point = Point(initialAmount = pointAmount)

        val savedPoint = pointRepo.save(point)
        Log.info("저장된 포인트 정보 $savedPoint")
        paymentManagement.point = savedPoint

        var customerPoint = CustomerPoint() // connectionTable
        customerPoint.point = savedPoint
        customerPoint.customer = customer
        val savedCustomerPoint = customerPointRepo.save(customerPoint)

        customer.customerPoints.add(savedCustomerPoint)
        Log.info("포인트를 추가한 이후의 고객 정보 $customer")

        val pointAmountDto = PointAmountDTO(
            customerLoginId = customer.loginId,
            pointAmount = savedPoint.initialAmount
        )

        Log.info("결제내역이 확정되어 컨트롤러로 pointDTO : ${pointAmountDto}를 보냅니다.[PaymentManagementService <completePaymentManagement> start]")
        return pointAmountDto
    }

    @Transactional
    fun cancelPaymentManagement(loginId: String, paymentManagementSerialCode: String): CanceledPaymentInfoDTO {
        Log.info("사용자 : ${loginId}의 주문번호가 ${paymentManagementSerialCode}인 결제 내역을 취소합니다.")
        val foundCustomer = getCustomerByLoginId(loginId)

        val foundPaymentManagement = paymentManagementRepo.findByOrderSerialCode(paymentManagementSerialCode) ?: throw BaseException(CustomResponseCode.NOTFOUND_PAYMENT)

        Log.info("찾아낸 결제 내역 정보는 $foundPaymentManagement 입니다.")

        if (foundPaymentManagement.status == STATUS.COMPLETE) {
            throw BaseException(CustomResponseCode.ALREADY_COMPLETE_PAYMENT)
        } else if (foundPaymentManagement.status == STATUS.CANCEL) {
            throw BaseException(CustomResponseCode.ALREADY_CANCLE_PAYMENT)
        }

        val usedCouponCategory = foundPaymentManagement.couponCategory
        val usedCouponSerialCode = foundPaymentManagement.couponSerialCode

        when (usedCouponCategory) {
            CATEGORY.AMOUNT -> cancelUsedAmountCoupon(foundCustomer, usedCouponSerialCode)
            CATEGORY.RATE -> cancelUsedRateCoupon(foundCustomer, usedCouponSerialCode)
            CATEGORY.NONE -> UsedCouponDTO.CanceledCouponDTO(serialCode = "NONE", category = CATEGORY.NONE)
        }
        val canceledCouponDto = UsedCouponDTO.CanceledCouponDTO(category = usedCouponCategory, serialCode = usedCouponSerialCode)
        Log.info("취소된 쿠폰 정보 $canceledCouponDto")

        var canceledPointsDtos: MutableSet<CanceledPointDTO> = mutableSetOf()
        if (foundPaymentManagement.usedPointAmount != 0L) {
            canceledPointsDtos = cancelUsedPoints(foundPaymentManagement)
        }
        Log.info("취소한 포인트 정보들 : $canceledPointsDtos")

        val canceledPaymentSerialCode = foundPaymentManagement.paymentSerialCode
        val canceledPayAmount = foundPaymentManagement.payAmount
        when(foundPaymentManagement.paymentMethod) {
            PAYMENTMEHTOD.CARD -> cancelCardPay(canceledPaymentSerialCode, canceledPayAmount)
            PAYMENTMEHTOD.BANKACCOUNT -> cancelBankAccountPay(canceledPaymentSerialCode, canceledPayAmount)
        }

        foundPaymentManagement.status = STATUS.CANCEL
        foundPaymentManagement.modifiedDate = LocalDate.now()

        return CanceledPaymentInfoDTO(cancelUsedCouponDto = canceledCouponDto, cancelUsedPointDtos = canceledPointsDtos, returnedPayAmount = canceledPayAmount)
    }

    private fun cancelCardPay(paymentSerialCode: String, payAmount: Long) {
        Log.info("카드 사용 결제 금액을 되돌립니다.")
        val foundCard = getCardBySerialInfo(paymentSerialCode)
        foundCard.remainMoney += payAmount
    }

    private fun getCardBySerialInfo(paymentSerialCode: String): Card {
        return cardRepo.findBySecretSerialInfo(paymentSerialCode)
            ?: throw BaseException(CustomResponseCode.NOTFOUND_CARD)
    }

    private fun cancelBankAccountPay(paymentSerialCode: String, payAmount: Long){
        Log.info("계좌 사용 결제 금액을 되돌립니다.")
        val foundBankAccount = bankAccountRepo.findBySecretSerialInfo(paymentSerialCode) ?: throw BaseException(CustomResponseCode.NOTFOUND_BANKACCOUNT)
        foundBankAccount.remainMoney += payAmount
    }

    private fun cancelUsedPoints(
        foundPaymentManagement: PaymentManagement
    ): MutableSet<CanceledPointDTO> {
        Log.info("결제 내역에서 사용한 포인트 금액을 되돌립니다.")
        var canceledPointDtos: MutableSet<CanceledPointDTO> = mutableSetOf()

        val usedPaymentUsePoints = paymentUsePointRepo.findAllByOrderSerialCode(foundPaymentManagement.orderSerialCode)
        for (usedPaymentUsePoint in usedPaymentUsePoints) {
            val usedPoint = usedPaymentUsePoint.usedPoint
            usedPaymentUsePoint.point!!.remainAmount += usedPoint
            canceledPointDtos.add(
                CanceledPointDTO(
                    initialAmount = usedPaymentUsePoint.point!!.initialAmount,
                    remainAmount = usedPaymentUsePoint.point!!.remainAmount,
                    canceledPoint = usedPoint
                )
            )
        }
        Log.info("결제 내역에서 사용한 포인트 금액을 되돌리기 완료.")
        return canceledPointDtos
    }

    private fun cancelUsedAmountCoupon(customer: Customer, usedCouponSerialCode: String) {
        Log.info("사용자 ${customer.userName}의 구매쿠폰을 사용 가능으로 설정합니다. ")
        var customersAmountCoupons = customer.customerAmountCoupons
        for (customerAmountCoupon in customersAmountCoupons) {
            if (customerAmountCoupon.amountCoupon!!.serialCode == usedCouponSerialCode) {
                customerAmountCoupon.isUsed = USED.NO
                customerAmountCoupon.usedDate = null
                break
            }
        }
    }

    private fun cancelUsedRateCoupon(customer: Customer, usedCouponSerialCode: String) {
        Log.info("사용자 ${customer.userName}의 할인 쿠폰을 사용 가능으로 설정합니다. ")
        var customerRateCoupons = customer.customerRateCoupons
        for (customerRateCoupon in customerRateCoupons) {
            if (customerRateCoupon.rateCoupon!!.serialCode == usedCouponSerialCode) {
                customerRateCoupon.isUsed = USED.NO
                customerRateCoupon.usedDate = null
                break
            }
        }
    }
}