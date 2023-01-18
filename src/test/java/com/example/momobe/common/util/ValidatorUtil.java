package com.example.momobe.common.util;

import javax.validation.Validation;
import javax.validation.Validator;

public interface ValidatorUtil {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    
    static <T> Object[] getArray(T dto, String path) {
        return validator.validate(dto).stream()
                .filter(v -> v.getPropertyPath().toString().equals(path))
                .toArray();
    }
}
