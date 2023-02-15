package com.example.momobe.common.exception.ui;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.dto.ErrorResponseEntity;
import com.example.momobe.security.exception.SecurityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponseEntity> handleValidationExceptions(SecurityException e) {
        return ErrorResponseEntity.of(e.getErrorCode());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseEntity> handleIllegalArgumentExceptions(CustomException e) {
        return ErrorResponseEntity.of(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseEntity> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException e) {
        return ErrorResponseEntity.of(e);
    }
}
