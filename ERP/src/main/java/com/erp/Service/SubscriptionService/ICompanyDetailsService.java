package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.CompanyDetailsRequest;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.SubscriptionsDto.CompanyDetailsDto;

public interface ICompanyDetailsService {
    CompanyDetailsDto fetchCompanyDetailsByCode(String companyCode);

    CompanyDetailsResponse createCompany(CompanyDetailsRequest request);
}
