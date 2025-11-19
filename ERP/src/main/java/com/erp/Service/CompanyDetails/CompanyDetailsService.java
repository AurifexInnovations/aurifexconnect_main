package com.erp.Service.CompanyDetails;

import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Dto.Response.ResultDto;

import java.util.Optional;

public interface CompanyDetailsService {

    Optional<CompanyDetailsResponseDto> findById(Long id);

    CompanyDetailsResponseDto findBySingleId(Long id);

    CompanyDetailsResponseDto saveAndUpdate(CompanyDetailsRequestDto companyDetails);

    CompanyDetailsResponseDto reviewCompany(CompanyDetailsRequestDto companyDetailsRequestDto);

    ResultDto<CompanyDetailsResponseDto> getFilterData(FilterRequest filterRequest);

    CompanyDetailsResponseDto getByEmail();
}
