package com.erp.Repository.Quotation;

import com.erp.Model.StatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatusLogRepository extends JpaRepository<StatusLog, Long> {
    List<StatusLog> findByQuotation_QuotationId(String quotationId);
}
