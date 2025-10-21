package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Projection.InventoryAndBranchProjection;
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
public class InventoryCustomRepository {

    private final Map<String, String> filterParamMap = Map.of(
            "itemName", "itemName",
            "categories", "categories",
            "branchId", "branchId",
            "startDate", "startDate",
            "endDate", "endDate"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "itemName", "itemName",
            "categories", "categories",
            "branchName", "branchName"
    );

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<InventoryAndBranchProjection> getInventoryDetails(FilterRequest filterRequest) {
        log.info("Into [InventoryCustomRepository] [getInventoryDetails]");

        // ---------- BASE DATA QUERY ----------
        StringBuilder sql = new StringBuilder("""
                SELECT
                        i.item_id,
                        i.item_name,
                        i.item_quantity,
                        i.item_description,
                        i.item_cost,
                        i.categories,
                        i.low_stock_threshold,
                        i.created_at,
                        b.branch_name AS branchName
                FROM inventory i
                INNER JOIN branch b ON i.branch_branch_id = b.branch_id
                WHERE 1=1
                """);

        // ---------- BASE COUNT QUERY ----------
        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) 
                FROM inventory i
                INNER JOIN branch b ON i.branch_branch_id = b.branch_id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ---------- FILTER CONDITIONS ----------
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("itemName")) {
                sql.append(" AND i.item_name = :itemName");
                countSql.append(" AND i.item_name = :itemName");
            }
            if (filters.containsKey("categories")) {
                sql.append(" AND i.categories = :categories");
                countSql.append(" AND i.categories = :categories");
            }
            if (filters.containsKey("branchId")) {
                sql.append(" AND i.branch_branch_id = :branchId");
                countSql.append(" AND i.branch_branch_id = :branchId");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                sql.append(" AND i.created_at BETWEEN :startDate AND :endDate");
                countSql.append(" AND i.created_at BETWEEN :startDate AND :endDate");
            } else if (filters.containsKey("startDate")) {
                sql.append(" AND i.created_at >= :startDate");
                countSql.append(" AND i.created_at >= :startDate");
            } else if (filters.containsKey("endDate")) {
                sql.append(" AND i.created_at <= :endDate");
                countSql.append(" AND i.created_at <= :endDate");
            }
        }

        // ---------- SEARCH CONDITIONS ----------
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("itemName")) {
                sql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :itemName, '%'))");
                countSql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :itemName, '%'))");
            }
            if (search.containsKey("categories")) {
                sql.append(" AND LOWER(i.categories) LIKE LOWER(CONCAT('%', :categories, '%'))");
                countSql.append(" AND LOWER(i.categories) LIKE LOWER(CONCAT('%', :categories, '%'))");
            }
            if (search.containsKey("branchName")) {
                sql.append(" AND LOWER(b.branch_name) LIKE LOWER(CONCAT('%', :branchName, '%'))");
                countSql.append(" AND LOWER(b.branch_name) LIKE LOWER(CONCAT('%', :branchName, '%'))");
            }
        }

        // ---------- ORDER BY ----------
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderClauses = new ArrayList<>();
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String column = entry.getKey();
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";

                switch (column) {
                    case "itemName" -> orderClauses.add("i.item_name " + direction);
                    case "categories" -> orderClauses.add("i.categories " + direction);
                    case "createdAt" -> orderClauses.add("i.created_at " + direction);
                    case "branchName" -> orderClauses.add("b.branch_name " + direction);
                }
            }
            if (!orderClauses.isEmpty()) {
                sql.append(String.join(", ", orderClauses));
            }
        }

        log.info("[InventoryCustomRepository] [getInventoryDetails] :: Query = {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- SET FILTER PARAMETERS ----------
        if (filters != null) {
            filterParamMap.forEach((paramName, mapKey) -> {
                String value = filters.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    switch (paramName) {
                        case "branchId" -> {
                            dataQuery.setParameter(paramName, Long.parseLong(value));
                            countQuery.setParameter(paramName, Long.parseLong(value));
                        }
                        case "startDate", "endDate" -> {
                            dataQuery.setParameter(paramName, Timestamp.valueOf(value));
                            countQuery.setParameter(paramName, Timestamp.valueOf(value));
                        }
                        default -> {
                            dataQuery.setParameter(paramName, value);
                            countQuery.setParameter(paramName, value);
                        }
                    }
                }
            });
        }

        // ---------- SET SEARCH PARAMETERS ----------
        if (search != null) {
            searchParamMap.forEach((paramName, mapKey) -> {
                String value = search.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    dataQuery.setParameter(paramName, value);
                    countQuery.setParameter(paramName, value);
                }
            });
        }

        // ---------- PAGINATION ----------
        if (filterRequest.getPaginationRequest() != null) {
            int pageNumber = filterRequest.getPaginationRequest().getPageNumber();
            int pageSize = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(pageNumber * pageSize);
            dataQuery.setMaxResults(pageSize);
        }

        // ---------- EXECUTE QUERIES ----------
        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        // ---------- MAP RESULTS ----------
        List<InventoryAndBranchProjection> resultList = new ArrayList<>();
        for (Object[] row : rows) {
            InventoryAndBranchProjection obj = new InventoryAndBranchProjection();
            obj.setItemId(((Number) row[0]).longValue());
            obj.setItemName((String) row[1]);
            obj.setItemQuantity(((Number) row[2]).doubleValue());
            obj.setItemDescription((String) row[3]);
            obj.setItemCost(((Number) row[4]).doubleValue());
            obj.setCategories((String) row[5]);
            obj.setLowStockThreshold(((Number) row[6]).doubleValue());
            obj.setCreatedAt(row[7] != null ? ((Timestamp) row[7]).toLocalDateTime() : null);
            obj.setBranchName((String) row[8]);
            resultList.add(obj);
        }

        // ---------- WRAP INTO ResultDto ----------
        ResultDto<InventoryAndBranchProjection> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(resultList);

        log.info("Exit [InventoryCustomRepository] [getInventoryDetails] with count = {}", totalCount);
        return resultDto;
    }
}
