package com.example.momobe.meeting.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class MeetingException extends CustomException {
    public MeetingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
