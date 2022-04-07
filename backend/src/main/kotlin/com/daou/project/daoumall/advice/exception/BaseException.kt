package com.daou.project.daoumall.advice.exception

data class BaseException(
    var customResponseCode: CustomResponseCode): RuntimeException(){
}