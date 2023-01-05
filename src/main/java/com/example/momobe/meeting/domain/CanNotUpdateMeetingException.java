package com.example.momobe.meeting.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CanNotUpdateMeetingException extends CustomException {
    public CanNotUpdateMeetingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
