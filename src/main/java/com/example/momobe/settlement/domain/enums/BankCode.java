package com.example.momobe.settlement.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankCode {
    카카오뱅크("090"),
    토스뱅크("092"),
    신한은행("088");

    private final String bankCode;
}
