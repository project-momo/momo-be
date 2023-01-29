package com.example.momobe.settlement.domain.exception;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotWithdrawalException extends CustomException {
    public CanNotWithdrawalException(ErrorCode errorCode) {
        super(errorCode);
    }
}
