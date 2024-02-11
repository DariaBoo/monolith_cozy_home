package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.validation.ValidOptionalComment;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalCommentValidator implements ConstraintValidator<ValidOptionalComment, String> {

    @Override
    public void initialize(ValidOptionalComment constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String comment, ConstraintValidatorContext constraintValidatorContext) {
        if (comment == null || comment.isEmpty()) {
            return true;
        }
        String pattern = "^[а-яА-Яa-zA-ZґҐєЄіІїЇ0-9\\s-.+]{2,50}+$";
        return comment.matches(pattern);
    }
}
