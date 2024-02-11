package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidOptionalAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalAddressValidator implements ConstraintValidator<ValidOptionalAddress, String> {

    @Override
    public void initialize(ValidOptionalAddress constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String address, ConstraintValidatorContext constraintValidatorContext) {
        if (address == null || address.isEmpty()) {
            return true;
        }
        String pattern = "^[а-яА-Яa-zA-ZґҐєЄіІїЇ0-9\\s-.]{2,32}+$";
        return address.matches(pattern);
    }
}
