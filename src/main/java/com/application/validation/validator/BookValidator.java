package com.application.validation.validator;

import com.application.validation.annotation.ValidBook;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookValidator implements ConstraintValidator<ValidBook, String> {
    private static final int FIRST_CHAR = 0;
    private static final int MIN_LENGTH = 3;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        if (title == null || title.isEmpty()) {
            return false;
        }
        return Character.isUpperCase(title.charAt(FIRST_CHAR))
                && title.length() >= MIN_LENGTH;
    }
}
