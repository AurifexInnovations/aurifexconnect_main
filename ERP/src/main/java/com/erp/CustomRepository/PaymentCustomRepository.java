package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaymentResponseDTO;
import com.erp.Dto.Response.ResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class PaymentCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<PaymentResponseDTO> getFilteredPayments(FilterRequest filterRequest) {

        log.info("Into [PaymentCustomRepository] [getFilteredPayments]");

        // ========== BASE QUERY ==========
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    p.payment_id,
                    p.bill_id,
                    p.vendor_id,
                    p.date_paid,
                    p.amount_paid,
                    p.voucher_id,
                    p.payment_method,
                    p.payment_number,
                    p.notes,
                    p.is_active,
                    p.created_date,
                    p.updated_date
                FROM payments p
                WHERE 1=1
                  AND p.is_active = TRUE
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*)
                FROM payments p
                WHERE 1=1
                  AND p.is_active = TRUE
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ========== FILTERS ==========
        if (filters != null) {

            if (filters.containsKey("paymentId")) {
                sql.append(" AND p.payment_id = :paymentId");
                countSql.append(" AND p.payment_id = :paymentId");
            }

            if (filters.containsKey("billId")) {
                sql.append(" AND p.bill_id = :billId");
                countSql.append(" AND p.bill_id = :billId");
            }

            if (filters.containsKey("vendorId")) {
                sql.append(" AND p.vendor_id = :vendorId");
                countSql.append(" AND p.vendor_id = :vendorId");
            }

            if (filters.containsKey("paymentMethod")) {
                sql.append(" AND p.payment_method = :paymentMethod");
                countSql.append(" AND p.payment_method = :paymentMethod");
            }
        }

        // ========== SEARCH ==========
        if (search != null) {

            if (search.containsKey("paymentMethod")) {
                sql.append(" AND LOWER(p.payment_method) LIKE :paymentMethodSearch");
                countSql.append(" AND LOWER(p.payment_method) LIKE :paymentMethodSearch");
            }

            if (search.containsKey("notes")) {
                sql.append(" AND LOWER(p.notes) LIKE :notesSearch");
                countSql.append(" AND LOWER(p.notes) LIKE :notesSearch");
            }

            if (search.containsKey("paymentNumber")) {
                sql.append(" AND LOWER(p.payment_number) LIKE :paymentNumberSearch");
                countSql.append(" AND LOWER(p.payment_number) LIKE :paymentNumberSearch");
            }
        }

        // ========== ORDER BY ==========
        if (orderBy != null && !orderBy.isEmpty()) {

            sql.append(" ORDER BY ");
            List<String> orderList = new ArrayList<>();

            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";

                switch (entry.getKey()) {
                    case "amountPaid" -> orderList.add("p.amount_paid " + direction);
                    case "datePaid" -> orderList.add("p.date_paid " + direction);
                    case "paymentMethod" -> orderList.add("p.payment_method " + direction);
                    default -> orderList.add("p.payment_id DESC");
                }
            }

            sql.append(String.join(", ", orderList));

        } else {
            sql.append(" ORDER BY p.payment_id DESC");
        }

        log.info("[PaymentCustomRepository] FINAL QUERY: {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ========== BIND PARAMETERS ==========
        bindParameters(filters, search, dataQuery, countQuery);

        // ========== PAGINATION ==========
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        List<PaymentResponseDTO> resultList = new ArrayList<>();

        for (Object[] row : rows) {

            PaymentResponseDTO dto = new PaymentResponseDTO();

            dto.setPaymentId(((Number) row[0]).longValue());
            dto.setBillId(((Number) row[1]).longValue());
            dto.setVendorId(((Number) row[2]).longValue());
            dto.setDatePaid(row[3] != null ? ((java.sql.Date) row[3]).toLocalDate() : null);
            dto.setAmountPaid(row[4] != null ? new BigDecimal(row[4].toString()) : null);
            dto.setVoucherId(((Number) row[5]).longValue());
            dto.setPaymentMethod((String) row[6]);
            dto.setPaymentNumber((String) row[7]);
            dto.setNotes((String) row[8]);
            dto.setIsActive((Boolean) row[9]);
//
//            dto.setCreatedDate(
//                    row[10] != null ? ((java.sql.Timestamp) row[10]).toLocalDateTime() : null
//            );
//
//            dto.setUpdatedDate(
//                    row[11] != null ? ((java.sql.Timestamp) row[11]).toLocalDateTime() : null
//            );

            resultList.add(dto);
        }

        ResultDto<PaymentResponseDTO> result = new ResultDto<>();
        result.setCount(totalCount);
        result.setResults(resultList);

        return result;
    }

    private void bindParameters(Map<String, String> filters, Map<String, String> search,
                                Query dataQuery, Query countQuery) {

        if (filters != null) {
            filters.forEach((key, value) -> {

                if (value == null || value.isEmpty()) return;

                switch (key) {
                    case "paymentId" -> {
                        Long id = Long.parseLong(value);
                        dataQuery.setParameter("paymentId", id);
                        countQuery.setParameter("paymentId", id);
                    }
                    case "billId" -> {
                        Long id = Long.parseLong(value);
                        dataQuery.setParameter("billId", id);
                        countQuery.setParameter("billId", id);
                    }
                    case "vendorId" -> {
                        Long id = Long.parseLong(value);
                        dataQuery.setParameter("vendorId", id);
                        countQuery.setParameter("vendorId", id);
                    }
                    case "paymentMethod" -> {
                        dataQuery.setParameter("paymentMethod", value);
                        countQuery.setParameter("paymentMethod", value);
                    }
                }
            });
        }

        if (search != null) {
            search.forEach((key, value) -> {

                if (value == null || value.isEmpty()) return;

                String pattern = "%" + value.toLowerCase() + "%";

                switch (key) {
                    case "paymentMethod" -> {
                        dataQuery.setParameter("paymentMethodSearch", pattern);
                        countQuery.setParameter("paymentMethodSearch", pattern);
                    }
                    case "notes" -> {
                        dataQuery.setParameter("notesSearch", pattern);
                        countQuery.setParameter("notesSearch", pattern);
                    }
                    case "paymentNumber" -> {
                        dataQuery.setParameter("paymentNumberSearch", pattern);
                        countQuery.setParameter("paymentNumberSearch", pattern);
                    }
                }
            });
        }
    }
}
