package com.elice.slowslow.domain.payment;

public enum PaymentStatus {
    WAITING, //결제 대기
    SUCCESS, //결제 성공
    FAIL, //결제 실패
    CANCEL //결제 취소
}
