package com.erp.Repository.SalesOrder;

import com.erp.Model.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Long> {

    List<SalesOrderItem> findBySoIdAndIsActiveTrue(Long soId);

    void deleteBySoId(Long soId);
}
