package com.erp.Repository.Inventory;

import com.erp.Model.Branch;
import com.erp.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    /**
     * Retrieves a list of {@link Inventory} entities with the specified item name.
     *
     * @param id,name The name of the item to search for.
     * @return A list of matching {@link Inventory} entities, or an empty list if none found.
     */
    List<Inventory> findByItemIdOrItemName(long id, String name);

    List<Inventory> findByBranch_BranchId(long branchId);

    // New method for stock transfer approval
    Optional<Inventory> findByBranchAndItemName(Branch branch, String itemName);
}

//@Query("SELECT i FROM Inventory i " +
//        "WHERE (:id IS NULL OR i.itemId = :id) " +
//        "AND (:name IS NULL OR i.itemName = :name)")
//List<Inventory> findByFlexibleSearch(@Param("id") Long id, @Param("name") String name);
