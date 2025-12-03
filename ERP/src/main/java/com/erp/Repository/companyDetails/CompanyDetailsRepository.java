package com.erp.Repository.companyDetails;

import com.erp.Model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails, Long> {
    boolean existsByCompanyEmail(String companyEmail);

    CompanyDetails findByCompanyEmail(String companyEmail);
}
