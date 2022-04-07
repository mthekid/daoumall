package com.daou.project.daoumall.service.customer

import com.daou.project.daoumall.advice.exception.BaseException
import com.daou.project.daoumall.advice.exception.CustomResponseCode
import com.daou.project.daoumall.config.log.Log
import com.daou.project.daoumall.dto.CustomerDTO
import com.daou.project.daoumall.dto.customer.CustomerLoginDTO
import com.daou.project.daoumall.dto.paymentdomain.PaymentManagementDTO
import com.daou.project.daoumall.model.Customer
import com.daou.project.daoumall.model.payment.STATUS
import com.daou.project.daoumall.repository.customer.CustomerRepository
import com.fasterxml.jackson.databind.ser.Serializers.Base
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerService (
    private val customerRepo: CustomerRepository,
//    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @Transactional
    fun createCustomer(customer: Customer): CustomerDTO.CustomerDTOResult? {
        Log.info("사용자를 생성합니다. [CustomerService <createCustomer> start]")
        if(customerRepo.findByLoginId(customer.loginId) != null) throw BaseException(CustomResponseCode.ALREADY_EXSISTS_CUSTOMER)

        val savedCustomer = customerRepo.save(customer)
        val customerResDto = CustomerDTO.fromEntity(savedCustomer)

        Log.info("Customer 정보가 영속성으로 처리되었습니다. $savedCustomer [CustomerService <createCustomer> end]")
        return customerResDto
    }

    @Transactional(readOnly = true)
    fun getCustomerInfo(loginId: String): CustomerDTO.CustomerDTOResult? {
        Log.info("사용자를 조회합니다. [CustomerService <getCustomerInfo> start]")

        val foundCustomer = findCustomer(loginId)
        val customerResDto = CustomerDTO.fromEntity(foundCustomer)

        Log.info("사용자 조회를 완료합니다. [CustomerService <getCustomerInfo> end]")
        return customerResDto
    }

    @Transactional(readOnly = true)
    fun getCustomerPayments(loginId: String): Set<PaymentManagementDTO> {
        Log.info("사용자 : ${loginId}의 결제 내역을 조회합니다.")
        val foundCustomer = findCustomer(loginId)

        var paymentManagementDtos: MutableSet<PaymentManagementDTO> = mutableSetOf()
        val paymentManagements = foundCustomer.paymentManagementInfos
        Log.info("조회한 사용자의 결제 내역 : $paymentManagements")

        for(paymentManagement in paymentManagements) {
            paymentManagementDtos.add(PaymentManagementDTO.fromPayManagementEntity(paymentManagement))
        }
        return paymentManagementDtos
    }

    @Transactional(readOnly = true)
    fun getCanceledCustomerPayments(loginId: String): Set<PaymentManagementDTO> {
        Log.info("사용자 : ${loginId}의 취소된 결제 내역을 조회합니다.")
        val foundCustomer = findCustomer(loginId)

        var canceledPaymentDtos : MutableSet<PaymentManagementDTO> = mutableSetOf()
        val paymentManagements = foundCustomer.paymentManagementInfos

        for(paymentManagement in paymentManagements) {
            if(paymentManagement.status == STATUS.CANCEL) {
                canceledPaymentDtos.add(PaymentManagementDTO.fromPayManagementEntity(paymentManagement))
            }
        }
        Log.info("사용자 : ${loginId}의 취소된 결제 내역을 조회했습니다. 반환 결과 $canceledPaymentDtos")
        return canceledPaymentDtos
    }

    @Transactional(readOnly = true)
    fun getCompletedCustomerPayments(loginId: String): Set<PaymentManagementDTO> {
        Log.info("사용자 : ${loginId}의 확정된 결제 내역을 조회합니다.")
        val foundCustomer = findCustomer(loginId)

        var completedPaymentDtos : MutableSet<PaymentManagementDTO> = mutableSetOf()
        val paymentManagements = foundCustomer.paymentManagementInfos

        for(paymentManagement in paymentManagements) {
            if(paymentManagement.status == STATUS.COMPLETE) {
                completedPaymentDtos.add(PaymentManagementDTO.fromPayManagementEntity(paymentManagement))
            }
        }

        return completedPaymentDtos
    }

    private fun findCustomer(loginId: String) =
        customerRepo.findByLoginId(loginId) ?: throw BaseException(CustomResponseCode.NOTFOUND_CUSTOMER)

    @Transactional(readOnly = true)
    fun login(loginDto : CustomerLoginDTO): CustomerLoginDTO.LoginStatusDTO {

        val foundCustomer = customerRepo.findByLoginId(loginDto.loginId) ?: throw BaseException(CustomResponseCode.NOTFOUND_EMAIL)
        if(foundCustomer.password != loginDto.password) throw BaseException(CustomResponseCode.PASSWORD_NOT_MAICH)

        return CustomerLoginDTO.LoginStatusDTO(loginId = foundCustomer.loginId, status = "login")
    }

    @Transactional(readOnly = true)
    fun getCustomerInfoByEmail(email: String): Customer {
        Log.info("사용자의 이메일 정보 ${email}을 받아 사용자를 조회합니다.")
        return customerRepo.findByEmail(email) ?: throw BaseException(CustomResponseCode.NOTFOUND_EMAIL)
    }

    @Transactional()
    fun getByCredentials(email: String, password: String): CustomerDTO.AuthenticateCustomerResDTO {
        val foundCustomer = customerRepo.findByEmail(email) ?: throw BaseException(CustomResponseCode.NOTFOUND_EMAIL)

        Log.info("패스워드가 같은지 확인합니다.")
        return CustomerDTO.fromCustomerEntity(foundCustomer)
    }
}