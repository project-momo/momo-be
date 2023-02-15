package com.example.momobe.settlement.domain.enums;

import lombok.Getter;

public enum PointUsedType {
    SETTLEMENT("모임 예약 정산"),
    PAYMENT("결제 차감"),
    WITHDRAWAL("적립금 출금"),
    REFUND("적립금 환급");

    @Getter
    private String message;

    PointUsedType(String message) {
        this.message = message;
    }
}
