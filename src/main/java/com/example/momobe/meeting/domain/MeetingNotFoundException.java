package com.example.momobe.meeting.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class MeetingNotFoundException extends CustomException {
    public MeetingNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
