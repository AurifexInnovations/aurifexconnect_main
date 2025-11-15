package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.StockTransferResponse;
import com.erp.Enum.StockTransferStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class StockTransferCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<StockTransferResponse> getStockTransferDetails(FilterRequest filterRequest) {

        log.info("Into [StockTransferCustomRepository] [getStockTransferDetails]");

        // ---------- DATA QUERY ----------
        StringBuilder sql = new StringBuilder("""
                SELECT
                    s.id,
                    fb.branch_name AS fromBranchName,
                    tb.branch_name AS toBranchName,
                    i.item_name,
                    i.brand_name,
                    s.quantity,
                    s.status,
                    s.approved_by,
                    s.initiated_by,
                    s.reason,
                    s.created_at
                FROM stocktransfer s
                INNER JOIN inventory i ON s.inventory_item_id = i.item_id
                INNER JOIN branch fb ON s.from_branch_id = fb.branch_id
                INNER JOIN branch tb ON s.to_branch_id = tb.branch_id
                WHERE 1=1
                """);

        // ---------- COUNT QUERY ----------
        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(s.id)
                FROM stocktransfer s
                INNER JOIN inventory i ON s.inventory_item_id = i.item_id
                INNER JOIN branch fb ON s.from_branch_id = fb.branch_id
                INNER JOIN branch tb ON s.to_branch_id = tb.branch_id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();
        Map<String, String> orderby = filterRequest.getOrderByColumns();

        // ---- FILTERS ----
        if (filters != null) {
            if (filters.containsKey("status")) {
                jpql.append(" AND s.status = :status");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                jpql.append(" AND s.createdAt BETWEEN :startDate AND :endDate");
            }

            if (filters.containsKey("fromBranchId")) {
                jpql.append(" AND s.fromBranch.branchId = :fromBranchId");
            }

            if (filters.containsKey("toBranchId")) {
                jpql.append(" AND s.toBranch.branchId = :toBranchId");
            }
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("approverName")) {
            jpql.append(" AND s.approvedBy LIKE :approverName");
        }

        // ---- ORDER BY ----
        if (orderby != null && !orderby.isEmpty()) {
            jpql.append(" ORDER BY ");
            List<String> orderClause = new ArrayList<>();
            orderby.forEach((column, direction) -> {
                switch (column) {
                    case "quantity" -> orderClause.add("s.quantity " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                }
            });
            jpql.append(String.join(", ", orderClause));
        }

        TypedQuery<StockTransfer> query = entityManager.createQuery(jpql.toString(), StockTransfer.class);

        // ---------- FILTERS ----------
        if (filters != null) {
            if (filters.containsKey("status")) {
                sql.append(" AND s.status = :status");
                countSql.append(" AND s.status = :status");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                sql.append(" AND s.created_at BETWEEN :startDate AND :endDate");
                countSql.append(" AND s.created_at BETWEEN :startDate AND :endDate");
            }
            if (filters.containsKey("fromBranchId")) {
                sql.append(" AND s.from_branch_id = :fromBranchId");
                countSql.append(" AND s.from_branch_id = :fromBranchId");
            }
            if (filters.containsKey("toBranchId")) {
                sql.append(" AND s.to_branch_id = :toBranchId");
                countSql.append(" AND s.to_branch_id = :toBranchId");
            }
            if (filters.containsKey("itemId")) {
                sql.append(" AND s.inventory_item_id = :itemId");
                countSql.append(" AND s.inventory_item_id = :itemId");
            }
        }

        // ---------- SEARCH ----------
        if (search != null) {
            if (search.containsKey("itemName")) {
                sql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :itemName, '%'))");
                countSql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :itemName, '%'))");
            }
            if (search.containsKey("brandName")) {
                sql.append(" AND LOWER(i.brand_name) LIKE LOWER(CONCAT('%', :brandName, '%'))");
                countSql.append(" AND LOWER(i.brand_name) LIKE LOWER(CONCAT('%', :brandName, '%'))");
            }
            if (search.containsKey("approverName")) {
                sql.append(" AND LOWER(s.approved_by) LIKE LOWER(CONCAT('%', :approverName, '%'))");
                countSql.append(" AND LOWER(s.approved_by) LIKE LOWER(CONCAT('%', :approverName, '%'))");
            }

            if (filters.containsKey("fromBranchId")) {
                countJpql.append(" AND s.fromBranch.branchId = :fromBranchId");
            }

            if (filters.containsKey("toBranchId")) {
                countJpql.append(" AND s.toBranch.branchId = :toBranchId");
            }


        }
        if (search != null && search.containsKey("approverName")) {
            countJpql.append(" AND s.approvedBy LIKE :approverName");
        }

        // ---------- ORDER BY ----------
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> clause = new ArrayList<>();
            orderBy.forEach((col, dir) -> {
                String d = dir.equalsIgnoreCase("desc") ? "DESC" : "ASC";
                switch (col) {
                    case "itemName" -> clause.add("i.item_name " + d);
                    case "brandName" -> clause.add("i.brand_name " + d);
                    case "quantity" -> clause.add("s.quantity " + d);
                    case "createdAt" -> clause.add("s.created_at " + d);
                }
            });
            sql.append(String.join(", ", clause));
        }

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- SET FILTER PARAMS ----------
        if (filters != null) {
            if (filters.containsKey("status")) {
                dataQuery.setParameter("status", filters.get("status"));
                countQuery.setParameter("status", filters.get("status"));
            }
            if (filters.containsKey("startDate")) {
                dataQuery.setParameter("startDate", Timestamp.valueOf(filters.get("startDate")));
                countQuery.setParameter("startDate", Timestamp.valueOf(filters.get("startDate")));
            }
            if (filters.containsKey("endDate")) {
                dataQuery.setParameter("endDate", Timestamp.valueOf(filters.get("endDate")));
                countQuery.setParameter("endDate", Timestamp.valueOf(filters.get("endDate")));
            }
            if (filters.containsKey("fromBranchId")) {
                dataQuery.setParameter("fromBranchId", Long.parseLong(filters.get("fromBranchId")));
                countQuery.setParameter("fromBranchId", Long.parseLong(filters.get("fromBranchId")));
            }
            if (filters.containsKey("toBranchId")) {
                dataQuery.setParameter("toBranchId", Long.parseLong(filters.get("toBranchId")));
                countQuery.setParameter("toBranchId", Long.parseLong(filters.get("toBranchId")));
            }

            if (filters.containsKey("itemId")) {
                dataQuery.setParameter("itemId", Long.parseLong(filters.get("itemId")));
                countQuery.setParameter("itemId", Long.parseLong(filters.get("itemId")));
            }
        }

        ResultDto<StockTransferResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(responses);

        // ---------- PAGINATION ----------
        if (filterRequest.getPaginationRequest() != null) {
            dataQuery.setFirstResult(filterRequest.getPaginationRequest().getPageNumber() *
                    filterRequest.getPaginationRequest().getPageSize());
            dataQuery.setMaxResults(filterRequest.getPaginationRequest().getPageSize());
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();
        List<StockTransferResponse> list = new ArrayList<>();

        for (Object[] row : rows) {
            StockTransferResponse dto = new StockTransferResponse();
            dto.setId(((Number) row[0]).longValue());
            dto.setFromBranchName((String) row[1]);
            dto.setToBranchName((String) row[2]);
            dto.setItemName((String) row[3]);
            dto.setBrandName((String) row[4]);
            dto.setQuantity(((Number) row[5]).doubleValue());
            dto.setStatus(StockTransferStatus.valueOf((String) row[6]));
            dto.setApprovedBy((String) row[7]);
            dto.setInitiatedBy((String) row[8]);
            dto.setReason((String) row[9]);
            dto.setCreatedAt(row[10] != null ? ((Timestamp) row[10]).toLocalDateTime() : null);
            list.add(dto);
        }

        ResultDto<StockTransferResponse> result = new ResultDto<>();
        result.setCount(totalCount);
        result.setResults(list);

        log.info("Exit [StockTransferCustomRepository] with count={}", totalCount);
        return result;
    }
}
