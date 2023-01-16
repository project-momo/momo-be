package com.example.momobe.settlement.domain.exception;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class NotFoundBankAccountException extends CustomException {
    public NotFoundBankAccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
