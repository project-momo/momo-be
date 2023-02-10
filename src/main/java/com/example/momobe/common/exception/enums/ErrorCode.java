package com.example.momobe.common.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    /*
    * Reservation, Payment, Meeting
    * */
    FULL_OF_PEOPLE(CONFLICT, "예약 인원이 가득 찼습니다."),
    AMOUNT_DOSE_NOT_MATCH(CONFLICT, "신청한 결제 금액과 실제 결제해야할 금액이 일치하지 않습니다. 관리자에게 문의하세요."),
    INVALID_RESERVATION_TIME(CONFLICT, "유효하지 않은 예약 시간대입니다."),
    CLOSED_MEETING(CONFLICT, "모집이 종료된 모임입니다."),
    CANCELED_RESERVATION(CONFLICT, "이미 취소된 예약입니다, 관리자에게 문의하세요."),
    CONFIRMED_RESERVATION(CONFLICT, "이미 확정된 예약입니다, 관리자에게 문의하세요."),
    CAN_NOT_FOUND_SETTLEMENT(NOT_FOUND,"금일 정산할 내역이 없습니다."),
    CAN_NOT_CHANGE_RESERVATION_STATE(CONFLICT, "해당 예약은 더 이상 수정할 수 없는 상태입니다"),
    INVALID_EMAIL(SERVICE_UNAVAILABLE, "유저의 메일 정보가 존재하지 않습니다"),
    ALREADY_EXIST_RESERVATION(CONFLICT, "해당 시간대에 이미 회원님의 예약건이 존재합니다"),
    EXCEEDED_EXPIRATION_DATE(CONFLICT, "지난 예약 일자는 조회할 수 없습니다"),
    CAN_NOT_PARTICIPATE_OWN_MEETING(CONFLICT, "자신의 모임에는 참여할 수 없습니다."),
    INVALID_PAYMENT_KEY(CONFLICT, "올바르지 않은 요청입니다."),

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
    CAN_NOT_CONVERT(BAD_REQUEST, "유효하지 않은 요청 형태입니다."),
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
