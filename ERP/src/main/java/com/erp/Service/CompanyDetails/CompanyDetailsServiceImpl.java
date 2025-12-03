package com.erp.Service.CompanyDetails;

import com.erp.CustomRepository.CompanyDetailsCustomRepository;
import com.erp.Dto.Request.CompanyDetailsRequestDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.ReviewStatus;
import com.erp.Exception.CompnayDetails.CompanyDetailsFoundException;
import com.erp.Exception.ResourceFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.companyDetails.CompanyDetailsMapper;
import com.erp.Model.CompanyDetails;
import com.erp.Repository.companyDetails.CompanyDetailsRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Service.DocumentDetails.DocumentDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CompanyDetailsServiceImpl implements CompanyDetailsService {

    private final CompanyDetailsRepository companyDetailsRepository;
    private final CompanyDetailsMapper companyDetailsMapper;
    private final CompanyDetailsCustomRepository companyDetailsCustomRepository;
    @Autowired
    private UserIdentity userIdentity;

    @Override
    public Optional<CompanyDetailsResponseDto> findById(final Long id) {
        return companyDetailsRepository.findById(id)
                .map(companyDetailsMapper::toResponseDto);
    }

    @Override
    public CompanyDetailsResponseDto findBySingleId(Long id) {
        CompanyDetails companyDetails = companyDetailsRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Company Details Not Found With Id : "+id));

        return companyDetailsMapper.toResponseDto(companyDetails);
    }

    @Transactional
    @Override
    public CompanyDetailsResponseDto saveAndUpdate(final CompanyDetailsRequestDto companyDetailsDto) {
        log.info("Into [CompanyDetailsService] [saveAndUpdate] - Starting to add/update company details");

            CompanyDetails newEntity = companyDetailsMapper.toEntity(companyDetailsDto);
            if (newEntity.getDocumentDetails() != null) {
                newEntity.getDocumentDetails().
                        forEach(doc -> doc.setCompanyDetails(newEntity));
            }

            String email = userIdentity.getCurrentUserEmail();

            if (companyDetailsRepository.existsByCompanyEmail(email)) {

                CompanyDetails companyDetails = companyDetailsRepository.findByCompanyEmail(email);

                if (companyDetails.getReviewStatus() == ReviewStatus.REJECTED || companyDetails.getReviewStatus() == ReviewStatus.ERROR) {

                    companyDetailsMapper.updateEntityFromEntity(newEntity, companyDetails);

                    companyDetails.setCompanyEmail(email);
                    companyDetails.setReviewedBy(null);
                    companyDetails.setReviewComment(null);
                    companyDetails.setReviewStatus(ReviewStatus.PENDING);

                    CompanyDetails savedEntity = companyDetailsRepository.save(companyDetails);
                    return companyDetailsMapper.toResponseDto(savedEntity);
                } else {
                    throw new ResourceFoundException("Company Already Exists With Email : " + email);
                }
            }

            newEntity.setCompanyEmail(email);
            CompanyDetails savedEntity = companyDetailsRepository.save(newEntity);
            return companyDetailsMapper.toResponseDto(savedEntity);
    }

    @Override
    public CompanyDetailsResponseDto reviewCompany(CompanyDetailsRequestDto companyDetailsRequestDto) {
        log.info("Into [CompanyDetailsService] [saveAndUpdate] - Starting to add/update company details");
        try {
            CompanyDetails newEntity = companyDetailsMapper.toEntity(companyDetailsRequestDto);
            if (newEntity.getDocumentDetails() != null) {
                newEntity.getDocumentDetails().
                        forEach(doc -> doc.setCompanyDetails(newEntity));
            }

            newEntity.setReviewedBy("Seravion Technologies");
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

    @Override
    public CompanyDetailsResponseDto getByEmail() {
        String email = userIdentity.getCurrentUserEmail();
        CompanyDetails companyDetails = companyDetailsRepository.findByCompanyEmail(email);

        if (companyDetails == null)
            throw new ResourceNotFoundException("Company Not Found With Email : " + email);

        return companyDetailsMapper.toResponseDto(companyDetails);
    }

    @Override
    public ResultDto<CompanyDetailsResponseDto> getAllCompanies() {
        return companyDetailsCustomRepository.getAllData();
    }
}
