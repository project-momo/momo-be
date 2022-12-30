package com.example.momobe.reservation.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class ReservationNotPossibleException extends CustomException {
    public ReservationNotPossibleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
