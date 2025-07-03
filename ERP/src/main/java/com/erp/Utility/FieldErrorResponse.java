package com.erp.Utility;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class FieldErrorResponse extends SimpleErrorResponse{

    private List<CustomFieldError> errors;

    public static CustomFieldError createFieldError(String message, Object rejectedValue, String filed){
        CustomFieldError error = new CustomFieldError();
        error.message = message;
        error.rejectedValue = rejectedValue;
        error.field = filed;
        return error;
    }

    @Getter
    public static class CustomFieldError {
         private String message;
         private Object rejectedValue;
         private String field;
    }
}
