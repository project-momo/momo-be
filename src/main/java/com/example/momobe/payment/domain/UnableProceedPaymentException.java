package com.example.momobe.payment.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class UnableProceedPaymentException extends CustomException {
    public UnableProceedPaymentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
