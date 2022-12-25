package com.example.momobe.user.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class TokenNotFoundException extends CustomException {
    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
