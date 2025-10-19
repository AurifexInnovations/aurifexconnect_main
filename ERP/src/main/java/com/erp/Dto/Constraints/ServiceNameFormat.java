package com.erp.Dto.Constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "Name cannot be blank")
@Pattern(
        regexp = "^[A-Za-z][A-Za-z0-9_ ]{3,}$",
        message = "Name must start with a letter and be at least 3 characters long. Only letters, numbers, spaces, and underscores are allowed."
)
@ReportAsSingleViolation

public @interface ServiceNameFormat
{
    String message() default "Invalid name format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
