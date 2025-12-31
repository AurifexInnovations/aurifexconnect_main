package com.erp.Repository.payment;


import com.erp.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInvoiceId(Long invoiceId);

    List<Payment> findByCustomerId(Long customerId);

    Optional<Payment> findFirstByInvoiceId(Long invoiceId);


    List<Payment> findAllByBranchBranchId(Long branchId);
}
