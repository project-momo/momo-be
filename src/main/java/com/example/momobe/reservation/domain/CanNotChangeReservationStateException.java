package com.example.momobe.reservation.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotChangeReservationStateException extends CustomException {
    public CanNotChangeReservationStateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
