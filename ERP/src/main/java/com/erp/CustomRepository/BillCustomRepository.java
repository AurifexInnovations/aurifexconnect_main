package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.BillResponseDTO;
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
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BillCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<BillResponseDTO> getFilteredBills(FilterRequest filterRequest) {
        log.info("Into [BillCustomRepository] [getFilteredBills]");

        // ========= BASE QUERY =========
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    b.bill_id,
                    b.po_id,
                    b.vendor_id,
                    b.bill_date,
                    b.due_date,
                    b.total_amount,
                    b.bill_number,
                    b.status,
                    b.is_active,
                    b.created_date,
                    b.updated_date
                FROM bills b
                WHERE b.is_active = TRUE
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) 
                FROM bills b
                WHERE b.is_active = TRUE
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ========= FILTERS =========
        if (filters != null) {
            if (filters.containsKey("billId")) {
                sql.append(" AND b.bill_id = :billId");
                countSql.append(" AND b.bill_id = :billId");
            }
            if (filters.containsKey("poId")) {
                sql.append(" AND b.po_id = :poId");
                countSql.append(" AND b.po_id = :poId");
            }
            if (filters.containsKey("vendorId")) {
                sql.append(" AND b.vendor_id = :vendorId");
                countSql.append(" AND b.vendor_id = :vendorId");
            }
            if (filters.containsKey("status")) {
                sql.append(" AND LOWER(b.status) = :status");
                countSql.append(" AND LOWER(b.status) = :status");
            }
        }

        // ========= SEARCH =========
        if (search != null) {
            if (search.containsKey("billNumber")) {
                sql.append(" AND LOWER(b.bill_number) LIKE :billNumberSearch");
                countSql.append(" AND LOWER(b.bill_number) LIKE :billNumberSearch");
            }
        }

        // ========= ORDER BY =========
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderList = new ArrayList<>();
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";
                switch (entry.getKey()) {
                    case "billNumber" -> orderList.add("b.bill_number " + direction);
                    case "billDate" -> orderList.add("b.bill_date " + direction);
                    case "createdDate" -> orderList.add("b.created_date " + direction);
                    default -> orderList.add("b.bill_id DESC");
                }
            }
            sql.append(String.join(", ", orderList));
        } else {
            sql.append(" ORDER BY b.bill_id DESC");
        }

        log.info("[BillCustomRepository] FINAL QUERY: {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ========= BIND PARAMETERS =========
        bindParameters(filters, search, dataQuery, countQuery);

        // ========= PAGINATION =========
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        // ========= MAP RESULT =========
        List<BillResponseDTO> resultList = rows.stream().map(row -> {
            BillResponseDTO dto = new BillResponseDTO();
            dto.setBillId(((Number) row[0]).longValue());
            dto.setPoId(((Number) row[1]).longValue());
            dto.setVendorId(((Number) row[2]).longValue());
            dto.setBillDate(row[3] != null ? ((java.sql.Date) row[3]).toLocalDate() : null);
            dto.setDueDate(row[4] != null ? ((java.sql.Date) row[4]).toLocalDate() : null);
            dto.setTotalAmount(row[5] != null ? new BigDecimal(row[5].toString()) : null);
            dto.setBillNumber((String) row[6]);
            dto.setStatus((String) row[7]);
            dto.setIsActive((Boolean) row[8]);
            dto.setCreatedDate(row[9] != null ? ((java.sql.Timestamp) row[9]).toLocalDateTime() : null);
            dto.setUpdatedDate(row[10] != null ? ((java.sql.Timestamp) row[10]).toLocalDateTime() : null);
            return dto;
        }).collect(Collectors.toList());

        ResultDto<BillResponseDTO> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(resultList);

        return resultDto;
    }

    private void bindParameters(Map<String, String> filters, Map<String, String> search,
                                Query dataQuery, Query countQuery) {

        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value == null || value.isEmpty()) return;
                switch (key) {
                    case "billId" -> {
                        Long id = Long.parseLong(value);
                        dataQuery.setParameter("billId", id);
                        countQuery.setParameter("billId", id);
                    }
                    case "poId" -> {
                        Long poId = Long.parseLong(value);
                        dataQuery.setParameter("poId", poId);
                        countQuery.setParameter("poId", poId);
                    }
                    case "vendorId" -> {
                        Long vendorId = Long.parseLong(value);
                        dataQuery.setParameter("vendorId", vendorId);
                        countQuery.setParameter("vendorId", vendorId);
                    }
                    case "status" -> {
                        dataQuery.setParameter("status", value.toLowerCase());
                        countQuery.setParameter("status", value.toLowerCase());
                    }
                }
            });
        }

        if (search != null) {
            search.forEach((key, value) -> {
                if (value == null || value.isEmpty()) return;
                switch (key) {
                    case "billNumber" -> {
                        String pattern = "%" + value.toLowerCase() + "%";
                        dataQuery.setParameter("billNumberSearch", pattern);
                        countQuery.setParameter("billNumberSearch", pattern);
                    }
                }
            });
        }
    }
}
