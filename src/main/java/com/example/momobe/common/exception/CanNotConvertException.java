package com.example.momobe.common.exception;

import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotConvertException extends CustomException {
    public CanNotConvertException(ErrorCode errorCode) {
        super(errorCode);
    }
}
