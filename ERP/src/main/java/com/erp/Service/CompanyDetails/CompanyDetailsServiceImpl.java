package com.erp.Service.CompanyDetails;

import com.erp.CustomRepository.CompanyDetailsCustomRepository;
import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Mapper.companyDetails.CompanyDetailsMapper;
import com.erp.Model.CompanyDetails;
import com.erp.Repository.companyDetails.CompanyDetailsRepository;
import com.erp.Service.DocumentDetails.DocumentDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CompanyDetailsServiceImpl implements CompanyDetailsService {

    private final CompanyDetailsRepository companyDetailsRepository;
    private final CompanyDetailsMapper companyDetailsMapper;
    private final CompanyDetailsCustomRepository companyDetailsCustomRepository;

    @Override
    public Optional<CompanyDetailsResponseDto> findById(final Long id) {
        return companyDetailsRepository.findById(id)
                .map(companyDetailsMapper::toResponseDto);
    }

    @Transactional
    @Override
    public CompanyDetailsResponseDto saveAndUpdate(final CompanyDetailsRequestDto companyDetailsDto) {
        log.info("Into [CompanyDetailsService] [saveAndUpdate] - Starting to add/update company details");
        try {
            CompanyDetails newEntity = companyDetailsMapper.toEntity(companyDetailsDto);
            if (newEntity.getDocumentDetails() != null) {
                newEntity.getDocumentDetails().
                        forEach(doc -> doc.setCompanyDetails(newEntity));
            }
            CompanyDetails savedEntity = companyDetailsRepository.save(newEntity);
            return companyDetailsMapper.toResponseDto(savedEntity);
        } catch (Exception e) {
            log.error("Exception occurred in [CompanyDetailsService] [saveAndUpdate]: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save or update company details", e);
        }
    }

    @Override
    public ResultDto<CompanyDetailsResponseDto> getFilterData(FilterRequest filterRequest) {
        return companyDetailsCustomRepository.getFilterData(filterRequest);
    }
}
