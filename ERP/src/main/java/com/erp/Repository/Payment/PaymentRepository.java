package com.erp.Repository.Payment;

import com.erp.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.paymentId = :id AND p.isActive = true")
    Optional<Payment> findActivePaymentById(Long id);
}
