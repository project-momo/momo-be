package com.example.momobe.common.exception;

import com.example.momobe.common.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public final class CustomException extends RuntimeException {
    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
