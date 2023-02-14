package com.example.momobe.reservation.domain.enums;

import lombok.Getter;

public enum ReservationState {
    PAYMENT_BEFORE("결제 진행 전"),
    PAYMENT_PROGRESS("결제 진행 중"),
    PAYMENT_SUCCESS("신청 확정 대기"),
    DENY("거절"),
    ACCEPT("참여 확정"),
    CANCEL("취소"),
    FINISH("참여 완료")
    ;

    @Getter
    private String korType;

    ReservationState(String korType) {
        this.korType = korType;
    }
}
