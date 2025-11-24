package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SalesOrderResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class SalesOrderCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // =====================================================================
    // FILTER Sales Orders
    // =====================================================================
    public ResultDto<SalesOrderResponseDTO> filterSalesOrders(FilterRequest filterRequest) {

        log.info("START :: filterSalesOrders");

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    so.so_id,
                    so.customer_id,
                    so.quotation_id,
                    so.order_date,
                    so.delivery_terms,
                    so.sales_notes,
                    so.status,
                    so.total_value
                FROM sales_orders so
                WHERE so.is_active = TRUE
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*)
                FROM sales_orders so
                WHERE so.is_active = TRUE
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ============================================================
        // FILTER CONDITIONS
        // ============================================================
        if (filters != null) {

            if (filters.containsKey("soId")) {
                sql.append(" AND so.so_id = :soId");
                countSql.append(" AND so.so_id = :soId");
            }

            if (filters.containsKey("customerId")) {
                sql.append(" AND so.customer_id = :customerId");
                countSql.append(" AND so.customer_id = :customerId");
            }

            if (filters.containsKey("quotationId")) {
                sql.append(" AND so.quotation_id = :quotationId");
                countSql.append(" AND so.quotation_id = :quotationId");
            }

            if (filters.containsKey("status")) {
                sql.append(" AND so.status = :status");
                countSql.append(" AND so.status = :status");
            }

            if (filters.containsKey("orderDate")) {
                sql.append(" AND so.order_date = :orderDate");
                countSql.append(" AND so.order_date = :orderDate");
            }
        }

        // ============================================================
        // SEARCH CONDITIONS
        // Supports: salesNotes, deliveryTerms
        // ============================================================
        if (search != null) {

            if (search.containsKey("salesNotes")) {
                sql.append(" AND LOWER(so.sales_notes) LIKE :salesNotes");
                countSql.append(" AND LOWER(so.sales_notes) LIKE :salesNotes");
            }

            if (search.containsKey("deliveryTerms")) {
                sql.append(" AND LOWER(so.delivery_terms) LIKE :deliveryTerms");
                countSql.append(" AND LOWER(so.delivery_terms) LIKE :deliveryTerms");
            }
        }

        // ============================================================
        // ORDER BY
        // ============================================================
        sql.append(" ORDER BY ");

        if (orderBy != null && !orderBy.isEmpty()) {

            List<String> orderList = new ArrayList<>();

            for (var entry : orderBy.entrySet()) {

                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";

                switch (entry.getKey()) {
                    case "orderDate" -> orderList.add("so.order_date " + direction);
                    case "totalValue" -> orderList.add("so.total_value " + direction);
                    default -> orderList.add("so.so_id DESC");
                }
            }

            sql.append(String.join(", ", orderList));

        } else {
            sql.append(" so.so_id DESC");
        }

        log.info("FINAL SQL :: {}", sql);

        // ============================================================
        // QUERY EXECUTION
        // ============================================================
        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        bindParameters(filters, search, dataQuery, countQuery);

        // Pagination
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();

        List<Object[]> rows = dataQuery.getResultList();
        List<SalesOrderResponseDTO> results = new ArrayList<>();

        for (Object[] row : rows) {

            SalesOrderResponseDTO dto = new SalesOrderResponseDTO();

            dto.setSoId(getLong(row[0]));
            dto.setCustomerId(getLong(row[1]));
            dto.setQuoteId(getLong(row[2]));
            dto.setOrderDate(getLocalDate(row[3]));
            dto.setDeliveryTerms(getString(row[4]));
            dto.setSalesNotes(getString(row[5]));
            dto.setStatus(getString(row[6]));
            dto.setTotalValue(getBigDecimal(row[7]));

            results.add(dto);
        }

        ResultDto<SalesOrderResponseDTO> result = new ResultDto<>();
        result.setCount(totalCount);
        result.setResults(results);

        return result;
    }

    // =====================================================================
    // BIND PARAMETERS
    // =====================================================================
    private void bindParameters(Map<String, String> filters, Map<String, String> search,
                                Query dataQuery, Query countQuery) {

        if (filters != null) {

            filters.forEach((key, value) -> {
                if (value == null || value.isEmpty()) return;

                switch (key) {
                    case "soId" -> {
                        Long id = Long.parseLong(value);
                        dataQuery.setParameter("soId", id);
                        countQuery.setParameter("soId", id);
                    }
                    case "customerId" -> {
                        Long cid = Long.parseLong(value);
                        dataQuery.setParameter("customerId", cid);
                        countQuery.setParameter("customerId", cid);
                    }
                    case "quotationId" -> {
                        Long qid = Long.parseLong(value);
                        dataQuery.setParameter("quotationId", qid);
                        countQuery.setParameter("quotationId", qid);
                    }
                    case "status" -> {
                        dataQuery.setParameter("status", value);
                        countQuery.setParameter("status", value);
                    }
                    case "orderDate" -> {
                        LocalDate d = LocalDate.parse(value);
                        dataQuery.setParameter("orderDate", java.sql.Date.valueOf(d));
                        countQuery.setParameter("orderDate", java.sql.Date.valueOf(d));
                    }
                }
            });
        }

        if (search != null) {

            search.forEach((key, value) -> {
                if (value == null || value.trim().isEmpty()) return;

                String pattern = "%" + value.toLowerCase() + "%";

                switch (key) {
                    case "salesNotes" -> {
                        dataQuery.setParameter("salesNotes", pattern);
                        countQuery.setParameter("salesNotes", pattern);
                    }
                    case "deliveryTerms" -> {
                        dataQuery.setParameter("deliveryTerms", pattern);
                        countQuery.setParameter("deliveryTerms", pattern);
                    }
                }
            });
        }
    }

    // =====================================================================
    // SAFE TYPE CONVERSIONS
    // =====================================================================
    private Long getLong(Object o) {
        return o == null ? null : ((Number) o).longValue();
    }

    private String getString(Object o) {
        return o == null ? null : o.toString();
    }

    private LocalDate getLocalDate(Object o) {
        return o == null ? null : ((java.sql.Date) o).toLocalDate();
    }

    private BigDecimal getBigDecimal(Object o) {
        return o == null ? null : new BigDecimal(o.toString());
    }
}
