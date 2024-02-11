package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidOptionalBirthday;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalBirthdayValidator implements ConstraintValidator<ValidOptionalBirthday, String> {

    @Override
    public void initialize(ValidOptionalBirthday constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String birthday, ConstraintValidatorContext constraintValidatorContext) {
        if (birthday == null || birthday.isEmpty()) {
            return true;
        }

        String pattern = "^(?:19|20)\\d{2}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])$";
        return birthday.matches(pattern);
    }
}
