package com.example.momobe.reservation.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class ReservationException extends CustomException {
    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
