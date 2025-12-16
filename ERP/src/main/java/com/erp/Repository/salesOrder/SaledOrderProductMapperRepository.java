package com.erp.Repository.salesOrder;

import com.erp.Model.SaledOrderProductMapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface SaledOrderProductMapperRepository
        extends JpaRepository<SaledOrderProductMapper, Long> {


    @Transactional
    @Modifying
    void deleteBySalesOrderId(Long saledOrderId);

}
