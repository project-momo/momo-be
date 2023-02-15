package com.example.momobe.security.exception;

import com.example.momobe.common.exception.enums.ErrorCode;
import io.jsonwebtoken.JwtException;
import lombok.Getter;

public class SecurityException extends JwtException {
    @Getter
    private final ErrorCode errorCode;

    public SecurityException(ErrorCode errorCode) {
        super(null);
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
