package com.erp.Repository.Inventory;

import com.erp.Model.InventoryV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepositoryV2 extends JpaRepository<InventoryV2, Long> {
    boolean existsByItemId(long itemId);
}
