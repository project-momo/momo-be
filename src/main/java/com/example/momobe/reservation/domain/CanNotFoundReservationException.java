package com.example.momobe.reservation.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotFoundReservationException extends CustomException {
    public CanNotFoundReservationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
