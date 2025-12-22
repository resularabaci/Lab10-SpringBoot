package com.example.Lab10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ForbiddenValidator implements ConstraintValidator<NotForbidden, String> {

    private final List<String> forbiddenNames = Arrays.asList("admin", "root", "superuser");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !forbiddenNames.contains(value.toLowerCase());
    }
}