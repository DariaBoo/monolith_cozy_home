package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber == null) {
            return false;
        }
        String pattern = "\\+38 \\(\\d{3}\\) \\d{3} - \\d{2} - \\d{2}";
        return phoneNumber.matches(pattern);
    }
}
