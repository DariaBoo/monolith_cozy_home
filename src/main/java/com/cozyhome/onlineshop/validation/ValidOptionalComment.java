package com.cozyhome.onlineshop.validation;

import com.cozyhome.onlineshop.validation.impl.OptionalCommentValidator;
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
@Constraint(validatedBy = OptionalCommentValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface ValidOptionalComment {
    String message() default "Comment must be greater than or equal to 2 and less than or equal to 50.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
