package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidOptionalRegion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalRegionValidator implements ConstraintValidator<ValidOptionalRegion, String> {

    @Override
    public void initialize(ValidOptionalRegion constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String region, ConstraintValidatorContext constraintValidatorContext) {
        if (region == null || region.isEmpty()) {
            return true;
        }
        String pattern = "^[а-яА-Яa-zA-ZґҐєЄіІїЇ]{2,32}(?:-[а-яА-Яa-zA-ZґҐєЄіІїЇ]{2,32})?$";
        return region.matches(pattern);
    }
}
