package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidUUID;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UUIDValidator implements ConstraintValidator<ValidUUID, String> {
    @Override
    public void initialize(ValidUUID constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String uuidId, ConstraintValidatorContext constraintValidatorContext) {
        if (uuidId == null) {
            return false;
        }
        String pattern = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
        return uuidId.matches(pattern);
    }
}
