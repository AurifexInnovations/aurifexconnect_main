package com.erp.Dto.Request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaxAnalyticsRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}