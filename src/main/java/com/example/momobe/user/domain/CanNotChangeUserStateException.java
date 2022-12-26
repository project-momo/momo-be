package com.example.momobe.user.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotChangeUserStateException extends CustomException {
    public CanNotChangeUserStateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
