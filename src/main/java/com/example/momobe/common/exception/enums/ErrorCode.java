package com.example.momobe.common.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    /*
    * Reservation
    * */
    FULL_OF_PEOPLE(CONFLICT, "예약 인원이 가득 찼습니다."),
    AMOUNT_DOSE_NOT_MATCH(CONFLICT, "신청한 결제 금액과 실제 결제해야할 금액이 일치하지 않습니다. 관리자에게 문의하세요."),
    INVALID_RESERVATION_TIME(CONFLICT, "유효하지 않은 예약 시간대입니다."),
    CLOSED_MEETING(CONFLICT, "모집이 종료된 모임입니다."),

    /*
    * Token Exception
    * */
    INVALID_TOKEN(UNAUTHORIZED, "invalid token exception"),
    SIGNATURE_EXCEPTION(UNAUTHORIZED, "signature key is different"),
    EXPIRED_EXCEPTION(UNAUTHORIZED, "expired token"),
    MALFORMED_EXCEPTION(UNAUTHORIZED, "malformed token"),
    ILLEGAL_ARGUMENTS_EXCEPTION(UNAUTHORIZED, "using illegal argument like null"),
    UNSUPPORTED_EXCEPTION(UNAUTHORIZED, "unsupported token"),

    /*
    * resolver
    * */
    NOT_FOUND_USER(UNAUTHORIZED, "유효하지 않은 계정입니다, 관리자에게 문의하세요."),

    /*
    * etc
    * */
    REQUEST_CONFLICT(CONFLICT, "해당 요청을 수행할 수 없습니다, 관리자에게 문의하세요."),
    UNABLE_TO_PROCESS(SERVICE_UNAVAILABLE, "현재 해당 요청을 수행할 수 없습니다, 관리자에게 문의하세요."),
    DATA_NOT_FOUND(NOT_FOUND, "조회하려는 데이터가 존재하지 않습니다."),
    REQUEST_DENIED(FORBIDDEN, "권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
