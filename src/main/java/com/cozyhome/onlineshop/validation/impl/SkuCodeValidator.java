package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidSkuCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SkuCodeValidator implements ConstraintValidator<ValidSkuCode, String> {

    @Override
    public void initialize(ValidSkuCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String skuCode, ConstraintValidatorContext constraintValidatorContext) {
        if (skuCode == null) {
            return false;
        }
        String pattern = "^\\d{6}$";
        return  skuCode.matches(pattern);
    }
}
