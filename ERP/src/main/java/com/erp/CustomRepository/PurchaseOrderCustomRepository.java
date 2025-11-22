package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.PurchaseOrderResponseDTO;
import com.erp.Dto.Response.ResultDto;
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
public class PurchaseOrderCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<PurchaseOrderResponseDTO> filterPurchaseOrders(FilterRequest filterRequest) {

        log.info("Into [PurchaseOrderCustomRepository] [getFilteredPOs]");

        // ========== BASE QUERY ==========  
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    po.po_id,
                    po.vendor_id,
                    v.vendor_name,
                    po.po_number,
                    po.status,
                    po.date_issued,
                    po.expected_delivery_date,
                    po.shipping_cost,
                    po.total_value,
                    po.created_date,
                    po.updated_date
                FROM purchase_orders po
                JOIN vendors v ON po.vendor_id = v.vendor_id
                WHERE 1=1
                  AND po.is_active = TRUE
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*)
                FROM purchase_orders po
                JOIN vendors v ON po.vendor_id = v.vendor_id
                WHERE 1=1
                  AND po.is_active = TRUE
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ========== FILTERS ==========
        if (filters != null) {

            if (filters.containsKey("poId")) {
                sql.append(" AND po.po_id = :poId");
                countSql.append(" AND po.po_id = :poId");
            }

            if (filters.containsKey("vendorId")) {
                sql.append(" AND po.vendor_id = :vendorId");
                countSql.append(" AND po.vendor_id = :vendorId");
            }

            if (filters.containsKey("status")) {
                sql.append(" AND po.status = :status");
                countSql.append(" AND po.status = :status");
            }

            if (filters.containsKey("dateIssued")) {
                sql.append(" AND po.date_issued = :dateIssued");
                countSql.append(" AND po.date_issued = :dateIssued");
            }
        }

        // ========== SEARCH ==========
        if (search != null) {

            if (search.containsKey("vendorName")) {
                sql.append(" AND LOWER(v.vendor_name) LIKE :vendorNameSearch");
                countSql.append(" AND LOWER(v.vendor_name) LIKE :vendorNameSearch");
            }

            if (search.containsKey("poNumber")) {
                sql.append(" AND LOWER(po.po_number) LIKE :poNumberSearch");
                countSql.append(" AND LOWER(po.po_number) LIKE :poNumberSearch");
            }
        }

        // ========== ORDER BY ==========
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderList = new ArrayList<>();

            for (Map.Entry<String, String> entry : orderBy.entrySet()) {

                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";

                switch (entry.getKey()) {
                    case "vendorName" -> orderList.add("v.vendor_name " + direction);
                    case "dateIssued" -> orderList.add("po.date_issued " + direction);
                    case "totalValue" -> orderList.add("po.total_value " + direction);
                    case "status" -> orderList.add("po.status " + direction);
                    default -> orderList.add("po.po_id DESC");
                }
            }

            sql.append(String.join(", ", orderList));

        } else {
            sql.append(" ORDER BY po.po_id DESC");
        }

        log.info("[PurchaseOrderCustomRepository] FINAL QUERY: {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

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

        // ========== MAP RESULTS ==========
        List<PurchaseOrderResponseDTO> resultList = new ArrayList<>();

        for (Object[] row : rows) {

            PurchaseOrderResponseDTO dto = new PurchaseOrderResponseDTO();

            dto.setPoId(((Number) row[0]).longValue());
            dto.setVendorId(((Number) row[1]).longValue());
            dto.setVendorName((String) row[2]);
            dto.setPoNumber((String) row[3]);
            dto.setStatus((String) row[4]);

            dto.setDateIssued(
                    row[5] != null ? ((java.sql.Date) row[5]).toLocalDate() : null
            );

            dto.setExpectedDeliveryDate(
                    row[6] != null ? ((java.sql.Date) row[6]).toLocalDate() : null
            );

            dto.setShippingCost(
                    row[7] != null ? new BigDecimal(row[7].toString()) : null
            );

            dto.setTotalValue(
                    row[8] != null ? new BigDecimal(row[8].toString()) : null
            );

            resultList.add(dto);
        }

        ResultDto<PurchaseOrderResponseDTO> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(resultList);

        return resultDto;
    }

    // ---------------- BIND PARAMETERS ----------------
    private void bindParameters(Map<String, String> filters, Map<String, String> search,
                                Query dataQuery, Query countQuery) {

        if (filters != null) filters.forEach((key, value) -> {
            if (value == null || value.isEmpty()) return;

            switch (key) {
                case "poId" -> {
                    Long id = Long.parseLong(value);
                    dataQuery.setParameter("poId", id);
                    countQuery.setParameter("poId", id);
                }
                case "vendorId" -> {
                    Long vendorId = Long.parseLong(value);
                    dataQuery.setParameter("vendorId", vendorId);
                    countQuery.setParameter("vendorId", vendorId);
                }
                case "status" -> {
                    dataQuery.setParameter("status", value);
                    countQuery.setParameter("status", value);
                }
                case "dateIssued" -> {
                    LocalDate date = LocalDate.parse(value);
                    dataQuery.setParameter("dateIssued", java.sql.Date.valueOf(date));
                    countQuery.setParameter("dateIssued", java.sql.Date.valueOf(date));
                }
            }
        });

        if (search != null) search.forEach((key, value) -> {
            if (value == null || value.isEmpty()) return;

            String pattern = "%" + value.toLowerCase() + "%";

            switch (key) {
                case "vendorName" -> {
                    dataQuery.setParameter("vendorNameSearch", pattern);
                    countQuery.setParameter("vendorNameSearch", pattern);
                }
                case "poNumber" -> {
                    dataQuery.setParameter("poNumberSearch", pattern);
                    countQuery.setParameter("poNumberSearch", pattern);
                }
            }
        });
    }
}
