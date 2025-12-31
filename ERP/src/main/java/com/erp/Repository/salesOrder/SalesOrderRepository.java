package com.erp.Repository.salesOrder;

import com.erp.Model.SalesOrder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    List<SalesOrder> findAllByBranchBranchId(Long branchId);

}
