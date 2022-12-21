package com.example.momobe.security.exception;

import com.example.momobe.common.exception.enums.ErrorCode;
import io.jsonwebtoken.JwtException;
import lombok.Getter;

public class InvalidJwtTokenException extends JwtException {
    @Getter
    private final ErrorCode errorCode;

    public InvalidJwtTokenException(ErrorCode errorCode) {
        super(null);
        this.errorCode = errorCode;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
