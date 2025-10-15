package com.erp.Dto.Response;

import lombok.Data;

@Data
public class CompanyDetailsResponse {
    private Long companyId;
    private String companyName;
    private String companyCode;
    private String activeYn;
}