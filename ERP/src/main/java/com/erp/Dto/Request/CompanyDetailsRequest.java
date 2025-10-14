package com.erp.Dto.Request;

import lombok.Data;

@Data
public class CompanyDetailsRequest {
    private String companyName;
    private String companyCode;
    private String createdBy;
}