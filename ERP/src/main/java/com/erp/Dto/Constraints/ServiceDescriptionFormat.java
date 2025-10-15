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
@NotBlank(message = "Service description cannot be blank")
@Pattern(
        regexp = "^[A-Za-z0-9][A-Za-z0-9 ,._-]{4,}$",
        message = "Description must be at least 5 characters long and can contain letters, numbers, spaces, commas, periods, hyphens, or underscores."
)
@ReportAsSingleViolation

public @interface ServiceDescriptionFormat
{
    String message() default "Invalid name format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
