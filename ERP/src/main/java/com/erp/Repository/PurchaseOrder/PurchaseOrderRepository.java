package com.erp.Repository.PurchaseOrder;

import com.erp.Model.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("SELECT p FROM PurchaseOrder p WHERE p.isActive = true")
    Page<PurchaseOrder> getAllActive(Pageable pageable);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.poId = :id AND p.isActive = true")
    Optional<PurchaseOrder> findActiveById(@Param("id") Long id);
}
