package com.erp.Repository.salesOrder;

import com.erp.Model.SaledOrderProductMapper;

import com.erp.Projection.SalesOrderProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SaledOrderProductMapperRepository
        extends JpaRepository<SaledOrderProductMapper, Long> {


    @Transactional
    @Modifying
    void deleteBySalesOrderId(Long saledOrderId);


        @Query(value = """
        SELECT product_id AS productId,
               quantity,
               subtotal,
               tax_amount AS taxAmount,
               total_amount AS totalAmount
        FROM saled_order_product_mapper
        WHERE sales_order_id = :salesOrderId
        """, nativeQuery = true)
        List<SalesOrderProductProjection> findBySalesOrderId(@Param("salesOrderId") Long salesOrderId);



}
