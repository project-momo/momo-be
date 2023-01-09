package com.example.momobe.settlement.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class NotFoundEndMeetingException extends CustomException {
    public NotFoundEndMeetingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
