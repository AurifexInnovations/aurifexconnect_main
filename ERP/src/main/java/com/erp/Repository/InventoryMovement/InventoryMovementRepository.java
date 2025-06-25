package com.erp.Repository.InventoryMovement;

import com.erp.Model.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    @Query(value = """
    SELECT 
        strftime(?1, tran_date) AS period,
        SUM(item_quantity) AS total_quantity
    FROM inventory_movement
    GROUP BY period
    ORDER BY period ASC
    """, nativeQuery = true)
    List<Object[]> getStockMovementSummary(String format);
}
