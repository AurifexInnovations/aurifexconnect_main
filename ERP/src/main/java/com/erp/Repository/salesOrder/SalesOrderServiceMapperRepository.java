package com.erp.Repository.salesOrder;

import com.erp.Model.SalesOrderServiceMapper;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesOrderServiceMapperRepository
        extends JpaRepository<SalesOrderServiceMapper, Long> {
    List<SalesOrderServiceMapper> findBySalesOrderId(Long salesOrderId);
}
