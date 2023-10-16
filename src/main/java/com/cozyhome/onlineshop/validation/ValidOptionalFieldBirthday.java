package com.cozyhome.onlineshop.validation;

import com.cozyhome.onlineshop.validation.impl.OptionalFieldBirthdayConstraintValidator;
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
@Constraint(validatedBy = OptionalFieldBirthdayConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface ValidOptionalFieldBirthday {
    String message() default "Invalid date of birth. Date of birth must have format yyyy-MM-dd.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
