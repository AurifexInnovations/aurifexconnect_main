package com.erp.Repository.Quotation;

import com.erp.Model.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    Quotation findByQuotationId(String quotationId);
}
