package com.example.momobe.settlement.domain.exception;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotSettleException extends CustomException {
    public CanNotSettleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
