package com.erp.Repository.Quotation;

import com.erp.Model.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long> {
    List<QuotationItem> findByQuotation_QuotationId(String quotationId);
}
