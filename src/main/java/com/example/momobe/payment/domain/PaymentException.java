package com.example.momobe.payment.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class PaymentException extends CustomException {
    public PaymentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
