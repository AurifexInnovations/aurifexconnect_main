package com.erp.Repository.Invoice;

import com.erp.Model.Invoice;
import com.erp.Model.InvoiceGenerator;
import com.erp.Projection.InvoiceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceMasterRepository extends JpaRepository<Invoice,Long> {



    @Query(value = """
        SELECT
            i.id AS invoiceId,
            i.customer_id AS customerId,
            i.sales_order_id AS salesOrderId,
            i.quotation_id AS quotationId,
            i.invoice_number AS invoiceNumber,
            i.invoice_date AS invoiceDate,
            i.due_date AS dueDate,
            i.service_category AS serviceCategory,
            i.sqft AS sqft,
            i.invoice_is_for AS invoiceIsFor,
            i.subtotal AS subtotal,
            i.tax_amount AS taxAmount,
            i.total_amount AS totalAmount,
            i.discount_amount AS discountAmount,
            i.grand_total AS grandTotal,
            i.amount_paid AS amountPaid,
            i.balance_amount AS balanceAmount,
            i.status AS status,
            i.notes AS notes,
            i.created_at AS createdAt,
            i.updated_at AS updatedAt,
            c.customer_name AS customerName
        FROM tenant_1_rak_gmail_com.invoices i
        LEFT JOIN tenant_1_rak_gmail_com.customer c 
            ON c.id = i.customer_id
        WHERE (:invoiceId IS NULL OR i.id = :invoiceId)
        ORDER BY i.id DESC
        LIMIT :limit OFFSET :offset
        """, nativeQuery = true)
    List<InvoiceProjection> getAllInvoiceDetailsList(
            @Param("invoiceId") Long invoiceId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
