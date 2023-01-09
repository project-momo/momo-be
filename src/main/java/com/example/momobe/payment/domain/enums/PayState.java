package com.example.momobe.payment.domain.enums;

import lombok.Getter;

public enum PayState {
    BEFORE("결제 전"),
    PROCESS("결제 진행 중"),
    SUCCESS("결제 완료"),
    SETTLEMENT_DONE("정산 완료"),
    SETTLEMENT_REFUND("정산 환급");

    @Getter
    private String value;

    PayState(String value) {
        this.value = value;
    }
}
