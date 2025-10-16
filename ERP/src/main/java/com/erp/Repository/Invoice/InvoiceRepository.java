package com.erp.Repository.Invoice;

import com.erp.Model.InvoiceGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceGenerator,Long> {
}
