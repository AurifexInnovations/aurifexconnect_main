package com.erp.Repository.salesOrder;

import com.erp.Model.SalesOrder;

import com.erp.Projection.SalesOrderProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {



    @Query(value = """
    SELECT 
        so.sales_order_number AS salesOrderNumber,
        so.quotation_id AS quotationId,
        so.customer_id AS customerId,
        so.phone_number AS phoneNumber,
        so.alternate_phone_number AS alternatePhoneNumber,
        so.sales_order_date AS salesOrderDate,
        so.company_name AS companyName,
        so.email AS email,
        so.address AS address,
        so.landmark AS landmark,
        so.city AS city,
        so.state AS state,
        so.country AS country,
        so.pincode AS pincode,
        so.location_url AS locationUrl,
        so.service_category AS serviceCategory,
        so.sqft AS sqft,
        so.sales_order_type AS salesOrderType,
        so.status AS status,
        so.notes AS notes,
        so.service_type AS serviceType
    FROM sales_orders so
    WHERE (:salesOrderId IS NULL OR so.sales_order_number = :salesOrderId)
    ORDER BY so.sales_order_number DESC
    LIMIT :limit OFFSET :offset
""", nativeQuery = true)
    List<SalesOrderProjection> findAllSalesOrders(
            @Param("salesOrderId") Long salesOrderId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


}
