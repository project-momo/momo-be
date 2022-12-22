package com.example.momobe.user.infrastructure;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class UnableToProcessException extends CustomException {
    public UnableToProcessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
