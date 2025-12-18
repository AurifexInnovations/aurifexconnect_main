package com.erp.Repository.salesOrder;

import com.erp.Model.SaledOrderProductMapper;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaledOrderProductMapperRepository
        extends JpaRepository<SaledOrderProductMapper, Long> {
    List<SaledOrderProductMapper> findBySaledOrderId(Long saledOrderId);
}
