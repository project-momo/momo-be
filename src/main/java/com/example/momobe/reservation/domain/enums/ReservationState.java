package com.example.momobe.reservation.domain.enums;

public enum ReservationState {
    PAYMENT_BEFORE,
    PAYMENT_PROGRESS,
    PAYMENT_SUCCESS,
    DENY,
    ACCEPT,
    CANCEL,
    /*
    * 기존 DB에 저장된 Enum과의 호환 문제로 남겨놓았으나 추후 삭제 예정입니다.
    * Author : Yang eun chan
    * Date : 2023/01/30
    * */
    @Deprecated
    SUCCESS,
}
