package com.cozyhome.onlineshop.validation;

import com.cozyhome.onlineshop.validation.impl.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface ValidPassword {
    String message() default "Invalid password. Password must have: minimum 8 characters in length, " +
            "at least one special character: #?!@$%^&*-, at least one uppercase English letter, " +
            "at least one lowercase English letter, at least one digit.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
