package com.erp.Repository.PurchaseOrder;

import com.erp.Model.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    List<PurchaseOrderItem> findByPoIdAndIsActiveTrue(Long poId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PurchaseOrderItem p WHERE p.poId = :poId")
    int deleteByPoId(Long poId);

}
