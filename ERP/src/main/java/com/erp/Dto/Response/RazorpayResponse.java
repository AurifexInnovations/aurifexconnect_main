package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayResponse {
    private String id;
    private String entity;
    private String amount;
    private String currency;
    private String receipt;
    private String status;

    // Getters and setters
}