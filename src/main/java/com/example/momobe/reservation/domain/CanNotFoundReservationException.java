package com.example.momobe.reservation.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotFoundReservation extends CustomException {
    public CanNotFoundReservation(ErrorCode errorCode) {
        super(errorCode);
    }
}
