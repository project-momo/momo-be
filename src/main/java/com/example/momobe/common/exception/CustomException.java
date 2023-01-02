package com.example.momobe.common.exception;

import com.example.momobe.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
