package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        Object[] enumValues = enumClass.getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value.toUpperCase().equals(enumValue.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
