package com.erp.Repository;

import com.erp.Model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    boolean existsByInvoiceNumber(String invoiceNumber);

}
