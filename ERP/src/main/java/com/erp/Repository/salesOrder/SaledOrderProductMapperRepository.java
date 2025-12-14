package com.erp.Repository.salesOrder;

import com.erp.Model.SaledOrderProductMapper;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SaledOrderProductMapperRepository
        extends JpaRepository<SaledOrderProductMapper, Long> {
}
