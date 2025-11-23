package com.erp.Repository.Bill;

import com.erp.Model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT b FROM Bill b WHERE b.billId = :id AND b.isActive = true")
    Optional<Bill> findActiveBillById(Long id);
}
