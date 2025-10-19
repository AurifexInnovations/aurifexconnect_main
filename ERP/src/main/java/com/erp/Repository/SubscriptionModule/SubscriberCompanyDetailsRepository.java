package com.erp.Repository.SubscriptionModule;

import com.erp.Model.CompanyDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriberCompanyDetailsRepository extends JpaRepository<CompanyDetailsEntity, Long> {
//    Optional<CompanyDetailsEntity> findByCompanycode(String companyCode);

    Optional<CompanyDetailsEntity> findByCompanyCode(String companyCode);
}