package com.erp.Service.CompanyDetails;

import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Response.CompanyDetailsResponseDto;

import java.util.Optional;

public interface CompanyDetailsService {

    Optional<CompanyDetailsResponseDto> findById(Long id);

    CompanyDetailsResponseDto saveAndUpdate(CompanyDetailsRequestDto companyDetails);
}
