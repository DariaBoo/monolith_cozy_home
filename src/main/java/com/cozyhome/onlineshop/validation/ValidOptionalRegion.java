package com.cozyhome.onlineshop.validation;

import com.cozyhome.onlineshop.validation.impl.OptionalRegionValidator;
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
@Constraint(validatedBy = OptionalRegionValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface ValidOptionalRegion {
    String message() default "Invalid region. Region must be greater than or equal to 2 and less than or equal " +
            "to 32, letters and - only.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
