package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IdValidator implements ConstraintValidator<ValidId, String> {

    @Override
    public void initialize(ValidId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null) {
            return false;
        }
        String pattern = "[A-Za-z0-9]{24}+$";
        return id.matches(pattern);
    }
}
