package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidQuantity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class QuantityValidator implements ConstraintValidator<ValidQuantity, Short> {
    @Override
    public void initialize(ValidQuantity constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Short value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        String pattern = "^[1-9]\\d*$";
        return value.toString().matches(pattern);
    }
}
