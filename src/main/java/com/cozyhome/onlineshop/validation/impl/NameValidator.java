package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    @Override
    public void initialize(ValidName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        if (name == null) {
            return false;
        }
        String pattern = "^[а-яА-Яa-zA-ZґҐєЄіІїЇ]{2,32}(?:-[а-яА-Яa-zA-ZґҐєЄіІїЇ]{2,32})?$";
        return name.matches(pattern);
    }
}
