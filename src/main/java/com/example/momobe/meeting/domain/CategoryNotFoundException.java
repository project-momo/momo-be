package com.example.momobe.meeting.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class CategoryNotFoundException extends CustomException {
    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
