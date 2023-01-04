package com.example.momobe.common.resolver;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class NotFoundUserException extends CustomException {
    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
