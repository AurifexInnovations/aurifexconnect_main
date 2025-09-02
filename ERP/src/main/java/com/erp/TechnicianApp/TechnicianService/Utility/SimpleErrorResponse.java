package com.erp.TechnicianApp.TechnicianService.Utility;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SimpleErrorResponse {
    private String type;
    private int status; // 404
    private String message; // failed to update the user, The user is not found by the given id
}
