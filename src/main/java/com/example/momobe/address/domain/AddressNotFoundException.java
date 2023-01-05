package com.example.momobe.address.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class AddressNotFoundException extends CustomException {
    public AddressNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
