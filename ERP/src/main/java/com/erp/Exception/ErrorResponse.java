package com.erp.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse
{
    private String message;
    private int code = 500 ;
    private String status = "Failed";

    public ErrorResponse(String message)
    {
        this.message = message;
    }
}
