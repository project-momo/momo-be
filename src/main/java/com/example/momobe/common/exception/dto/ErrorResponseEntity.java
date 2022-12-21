package com.example.momobe.common.exception.dto;

import com.example.momobe.common.exception.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

public final class ErrorResponseEntity {
    @Getter
    private final int status;
    @Getter
    private final String code;
    @Getter
    private final String message;
    private Map<String, String> validation;

    public Map<String, String> getValidation() {
        return (validation != null) ? new HashMap<>(validation) : new HashMap<>();
    }

    private ErrorResponseEntity(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    private ErrorResponseEntity(int status, String code, String message, Map<String, String> validation) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public static ResponseEntity<ErrorResponseEntity> of(ErrorCode e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponseEntity(e.getHttpStatus().value(), e.name(), e.getMessage()));
    }

    public static ResponseEntity<ErrorResponseEntity> of(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getAllErrors()
                .forEach(error ->
                        errors.put(((FieldError)error).getField(), error.getDefaultMessage())
                );

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponseEntity(BAD_REQUEST.value(), BAD_REQUEST.toString(), e.getMessage(), errors));
    }
}
