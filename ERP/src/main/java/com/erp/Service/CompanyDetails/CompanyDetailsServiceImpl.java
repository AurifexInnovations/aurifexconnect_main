package com.erp.Service.CompanyDetails;

import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Enum.IndustryType;
import com.erp.Enum.ReviewStatus;
import com.erp.Mapper.companyDetails.CompanyDetailsMapper;
import com.erp.Model.CompanyDetails;
import com.erp.Repository.companyDetails.CompanyDetailsRepository;
import com.erp.Security.util.UserIdentity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CompanyDetailsServiceImpl implements CompanyDetailsService {

    private static final String COMPANY_DETAILS_NOT_FOUND = "CompanyDetails not found with id: ";

    private final CompanyDetailsRepository companyDetailsRepository;
    private final CompanyDetailsMapper companyDetailsMapper;
    private final UserIdentity userIdentityService;

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
            CompanyDetails savedEntity = companyDetailsRepository.save(newEntity);
            return companyDetailsMapper.toResponseDto(savedEntity);
        } catch (Exception e) {
            log.error("Exception occurred in [CompanyDetailsService] [saveAndUpdate]: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save or update company details", e);
        }
    }

    private IndustryType parseIndustryType(String type) {
        try {
            return type != null ? IndustryType.valueOf(type) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid industry type: " + type);
        }
    }

    private ReviewStatus parseReviewStatus(String status) {
        try {
            return status != null ? ReviewStatus.valueOf(status) : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid review status: " + status);
        }
    }
}
