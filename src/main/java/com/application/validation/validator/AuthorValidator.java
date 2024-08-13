package com.application.validation.validator;

import com.application.validation.annotation.ValidAuthor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuthorValidator implements ConstraintValidator<ValidAuthor, String> {
    private static final int FIRST_CHAR = 0;

    @Override
    public boolean isValid(String author, ConstraintValidatorContext constraintValidatorContext) {
        if (author == null || author.isEmpty()) {
            return false;
        }
        String[] authorNames = author.split(" ");
        if (authorNames.length != 2) {
            return false;
        }
        for (String name : authorNames) {
            if (name.isEmpty() || !Character.isUpperCase(name.charAt(FIRST_CHAR))) {
                return false;
            }
        }
        return true;
    }
}
