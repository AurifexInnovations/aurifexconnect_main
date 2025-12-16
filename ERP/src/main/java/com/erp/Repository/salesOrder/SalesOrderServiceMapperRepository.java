package com.erp.Repository.salesOrder;

import com.erp.Model.SalesOrderServiceMapper;

import com.erp.Projection.SalesOrderServiceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesOrderServiceMapperRepository
        extends JpaRepository<SalesOrderServiceMapper, Long> {

    void deleteBySalesOrderId(Long salesOrderId);


        @Query(value = """
        SELECT service_id AS serviceId,
               quantity,
               subtotal,
               tax_amount AS taxAmount,
               total_amount AS totalAmount
        FROM sales_order_service_mapper
        WHERE sales_order_id = :salesOrderId
        """, nativeQuery = true)
        List<SalesOrderServiceProjection> findBySalesOrderId(@Param("salesOrderId") Long salesOrderId);



}
