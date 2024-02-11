package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidNewPasswordsMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class NewPasswordsMatchValidator implements ConstraintValidator<ValidNewPasswordsMatch, Object> {

    private String field;
    private String fieldMatch;

    public void initialize(ValidNewPasswordsMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Object fieldValue = new BeanWrapperImpl(value)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value)
                .getPropertyValue(fieldMatch);

        if (fieldValue != null) {
            return fieldValue.equals(fieldMatchValue);
        } else {
            return false;
        }
    }
}
