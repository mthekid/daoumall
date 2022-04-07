package com.daou.project.daoumall.advice.exception

import org.springframework.http.HttpStatus

enum class CustomResponseCode(val status: HttpStatus, val message: String) {

    //쿠폰예외
    ALREADY_EXSISTS_COUPON(HttpStatus.BAD_REQUEST, "이미 존재하는 쿠폰입니다"),
    ALREADY_OFFERED_COUPON(HttpStatus.BAD_REQUEST, "이미 발급된 쿠폰입니다. 중복 발급은 불가능 합니다."),
    NOTFOUND_COUPON(HttpStatus.BAD_REQUEST, "해당 쿠폰은 존재하지 않는 쿠폰입니다."),


    EXPIRED_COUPON(HttpStatus.BAD_REQUEST, "만료기간이 지난 쿠폰을 발급할 수 없습니다."),
    // 판매점 & 상품 에외
    NOTFOUND_MALL(HttpStatus.BAD_REQUEST, "해당 상품몰은 존재하지 않습니다."),
    NOTFOUND_PRODUCT(HttpStatus.BAD_REQUEST, "해당 판매 상품은 존재하지 않습니다."),


    MUST_HAVE_ESSENTIALOPTION(HttpStatus.BAD_REQUEST, "상품은 반드시 필수 옵션을 가져야 합니다."),
    // 고객 예외,
    ALREADY_EXSISTS_CUSTOMER(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다"),
    NOTFOUND_CUSTOMER(HttpStatus.BAD_REQUEST, "해당 로그인 정보를 가진 사용자가 없습니다."),


    NOTFOUND_EMAIL(HttpStatus.BAD_REQUEST, "해당 이메일을 가진 사용자는 없습니다."),
    ALREADY_EXSISTS_MALL(HttpStatus.BAD_REQUEST, "이미 존재하는 판매점 이름은 사용할 수 없습니다"),
    PASSWORD_NOT_MAICH(HttpStatus.BAD_REQUEST, "이메일은 존재하지만 비밀번호가 틀립니다."),


    // 결제예외
    NOTFOUND_CARD(HttpStatus.BAD_REQUEST, "해당 결제 카드는 존재하지 않습니다."),
    NOTFOUND_BANKACCOUNT(HttpStatus.BAD_REQUEST, "해당 결제 계좌는 존재하지 않습니다."),
    NOTFOUND_PAYMENT(HttpStatus.BAD_REQUEST, "해당 주문번호를 가진 결제내역이 존재하지 않습니다."),
    ALREADY_COMPLETE_PAYMENT(HttpStatus.BAD_REQUEST, "해당 결제내역은 이미 결제 확정되었습니다."),
    ALREADY_CANCLE_PAYMENT(HttpStatus.BAD_REQUEST, "해당 결제내역은 이미 취소되었습니다."),
    OVERUSE_REMAIN_POINT(HttpStatus.BAD_REQUEST, "사용 포인트 금액이 보유한 포인트 금액보다 많습니다."),
    OVERPAYMENT(HttpStatus.BAD_REQUEST, "결제 금액이 보유한 금액보다 많습니다."),

    // 생성 예외
    CANNOT_CREATE_MINUS_BANKACCOUNT(HttpStatus.BAD_REQUEST, "잔액이 0원보다 낮은 은행계좌를 생성할 수 없습니다."),
    CANNOT_CREATE_MINUS_CARD(HttpStatus.BAD_REQUEST, "잔액이 0원보다 낮은 카드를 생성할 수 없습니다."),
    CANNOT_CREATE_MINUS_PAYMENT(HttpStatus.BAD_REQUEST, "결제 금액은 0원보다 커야 합니다."),
    CANNOT_CREATE_AMOUNTCOUPON_UNDERDISCOUT(HttpStatus.BAD_REQUEST, "할인 금액은 최소 금액보다 커야 합니다."),
    CANNOT_CREATE_RATECOUPON(HttpStatus.BAD_REQUEST, "할인 비율은 1~99 사이의 값을 가져야 합니다")
}