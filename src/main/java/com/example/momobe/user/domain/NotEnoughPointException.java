package com.example.momobe.user.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class NotEnoughPointException extends CustomException {
    public NotEnoughPointException(ErrorCode errorCode) {
        super(errorCode);
    }
}
