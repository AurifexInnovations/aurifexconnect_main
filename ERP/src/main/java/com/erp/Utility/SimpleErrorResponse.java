package com.erp.Utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleErrorResponse
{
    private String type;
    private int status; // 404
    private String message; // failed to update the user, The user is not found by the given id
}
