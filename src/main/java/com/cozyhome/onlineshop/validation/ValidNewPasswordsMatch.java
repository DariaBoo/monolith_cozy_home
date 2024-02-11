package com.cozyhome.onlineshop.validation;

import com.cozyhome.onlineshop.validation.impl.NewPasswordsMatchValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = NewPasswordsMatchValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidNewPasswordsMatch {
    String message() default "Fields values don't match!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String field();

    String fieldMatch();
}
