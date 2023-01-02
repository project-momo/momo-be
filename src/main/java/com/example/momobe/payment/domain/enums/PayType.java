package com.example.momobe.payment.domain.enums;

import lombok.Getter;

public enum PayType {
    CARD("카드");

    @Getter
    private String value;

    PayType(String value) {
        this.value = value;
    }
}
