package com.example.momobe.common.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public enum ErrorCode {
    INVALID_TOKEN(UNAUTHORIZED, "invalid token exception"),
    SIGNATURE_EXCEPTION(UNAUTHORIZED, "signature key is different"),
    EXPIRED_EXCEPTION(UNAUTHORIZED, "expired token"),
    MALFORMED_EXCEPTION(UNAUTHORIZED, "malformed token"),
    ILLEGAL_ARGUMENTS_EXCEPTION(UNAUTHORIZED, "using illegal argument like null"),
    UNSUPPORTED_EXCEPTION(UNAUTHORIZED, "unsupported token");

    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus,String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
