package com.example.momobe.common.exception.ui;

import com.example.momobe.common.exception.dto.ErrorResponseEntity;
import com.example.momobe.security.exception.InvalidJwtTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ErrorResponseEntity> handleValidationExceptions(InvalidJwtTokenException e) {
        return ErrorResponseEntity.of(e.getErrorCode());
    }
}
