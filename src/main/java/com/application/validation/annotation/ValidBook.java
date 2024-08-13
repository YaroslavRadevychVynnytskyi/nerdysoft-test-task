package com.application.validation.annotation;

import com.application.validation.validator.BookValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBook {
    String message() default "Title should start with a capital letter, min length - 3 symbols";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
