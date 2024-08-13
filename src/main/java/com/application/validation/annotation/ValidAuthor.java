package com.application.validation.annotation;

import com.application.validation.validator.AuthorValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AuthorValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAuthor {
    String message() default "Author should contain two capital words with name and "
            + "surname and space between. Example: Paulo Coelho.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
