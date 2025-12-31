package com.erp.Repository.EnhanceQuotation;

import com.erp.Model.EnhanceQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnhanceQuotationRepository extends JpaRepository<EnhanceQuotation, Long> {

    List<EnhanceQuotation> findAllByBranchBranchId(Long branchId);




}
