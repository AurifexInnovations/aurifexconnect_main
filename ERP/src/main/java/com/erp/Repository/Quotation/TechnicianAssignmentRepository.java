package com.erp.Repository.Quotation;

import com.erp.Model.TechnicianAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TechnicianAssignmentRepository extends JpaRepository<TechnicianAssignment, Long> {
    List<TechnicianAssignment> findByQuotation_QuotationId(String quotationId);
}
