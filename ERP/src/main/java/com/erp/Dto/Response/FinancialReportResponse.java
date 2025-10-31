package com.erp.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialReportResponse {

    private String reportTitle;
    private String reportType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalDate generatedDate;
    private String companyName;
    private Map<String, Object> reportData;
    private Map<String, Object> summary;
}
