package com.example.momobe.tag.domain;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;

public class TagNotFoundException extends CustomException {
    public TagNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
