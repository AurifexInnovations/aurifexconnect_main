package com.erp.Repository.salesOrder;

import com.erp.Model.SalesOrder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
}
