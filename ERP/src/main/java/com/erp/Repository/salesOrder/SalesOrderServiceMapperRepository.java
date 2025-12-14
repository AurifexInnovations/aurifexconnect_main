package com.erp.Repository.salesOrder;

import com.erp.Model.SalesOrderServiceMapper;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderServiceMapperRepository
        extends JpaRepository<SalesOrderServiceMapper, Long> {
}
