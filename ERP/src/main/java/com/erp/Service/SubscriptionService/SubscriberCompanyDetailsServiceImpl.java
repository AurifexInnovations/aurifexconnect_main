package com.erp.Service.SubscriptionService;

import com.erp.Dto.Request.CompanyDetailsRequest;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.SubscriptionsDto.CompanyDetailsDto;
import com.erp.Mapper.SubscriptionModule.CompanyDetailsMapper;
import com.erp.Model.CompanyDetailsEntity;
import com.erp.Repository.SubscriptionModule.SubscriberCompanyDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class SubscriberCompanyDetailsServiceImpl implements ICompanyDetailsService {

    @Autowired
    private SubscriberCompanyDetailsRepository subscriberCompanyDetailsRepository;

    @Override
    public CompanyDetailsDto fetchCompanyDetailsByCode(String companyCode) {
        try {
            CompanyDetailsEntity entity = subscriberCompanyDetailsRepository.findByCompanyCode(companyCode).orElse(null);
            return entity != null ? CompanyDetailsMapper.toDto(entity) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CompanyDetailsResponse createCompany(CompanyDetailsRequest request) {
        CompanyDetailsEntity entity = new CompanyDetailsEntity();
        entity.setCompanyName(request.getCompanyName());
        entity.setCompanyCode(request.getCompanyCode());
        entity.setCreatedBy(request.getCreatedBy());
        entity.setActiveYn("Y");
        entity.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));

        CompanyDetailsEntity saved = subscriberCompanyDetailsRepository.save(entity);

        CompanyDetailsResponse response = new CompanyDetailsResponse();
        response.setCompanyId(saved.getCompanyId());
        response.setCompanyName(saved.getCompanyName());
        response.setCompanyCode(saved.getCompanyCode());
        response.setActiveYn(saved.getActiveYn());

        return response;
    }

}