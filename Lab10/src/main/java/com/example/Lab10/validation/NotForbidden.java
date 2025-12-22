package com.example.Lab10.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ForbiddenValidator.class)
public @interface NotForbidden {
    String message() default "This username is forbidden";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}