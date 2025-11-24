package com.erp.Repository.SalesOrder;

import com.erp.Model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    Optional<SalesOrder> findActiveBySoId(Long soId);
}
