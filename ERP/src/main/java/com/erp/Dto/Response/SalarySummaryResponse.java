package com.erp.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalarySummaryResponse {
    private String label;
    private double amount;
}