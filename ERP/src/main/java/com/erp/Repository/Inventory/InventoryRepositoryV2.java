package com.erp.Repository.Inventory;

import com.erp.Model.InventoryV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryRepositoryV2 extends JpaRepository<InventoryV2, Long> {
    boolean existsByItemId(long itemId);

    List<InventoryV2> findByBranch_BranchId(long branchId);
}
