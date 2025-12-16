package com.erp.Repository.QuotationServiceMapper;

import com.erp.Model.QuotationService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotationServiceMapperRepository extends JpaRepository<QuotationService, Long> {

    List<QuotationService> findByQuotationId(Long quotationId);
}
