package com.example.momobe.meeting.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotChangeMeetingStateException extends CustomException {
    public CanNotChangeMeetingStateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
