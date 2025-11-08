package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.ProductCategories;
import com.erp.Enum.ProductStatus;
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

        // ---------- DATA QUERY ----------
        StringBuilder sql = new StringBuilder("""
                SELECT
                    i.item_id,
                    i.item_name,
                    i.brand_name,
                    i.categories,
                    i.product_categories,
                    i.hsn_code,
                    i.sku_code,
                    i.ean,
                    i.is_returnable,
                    i.tax_id,
                    i.product_status,
                    i.branch_id,
                    i.created_at,
                    i.last_modified_at,
                    
                    COALESCE(SUM(v.stock_quantity), 0) AS totalStockQuantity,
                    MAX(v.expiry_date) AS latestExpiryDate,
                    
                    b.branch_name AS branchName
                FROM inventory i
                INNER JOIN branch b ON i.branch_id = b.branch_id
                LEFT JOIN varients v ON i.item_id = v.item_id
                WHERE 1=1
                """);

        // ---------- COUNT QUERY ----------
        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(DISTINCT i.item_id)
                FROM inventory i
                INNER JOIN branch b ON i.branch_id = b.branch_id
                LEFT JOIN varients v ON i.item_id = v.item_id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ---------- FILTERS ----------
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
                sql.append(" AND i.branch_id = :branchId");
                countSql.append(" AND i.branch_id = :branchId");
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

        // ---------- SEARCH ----------
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

        // ---------- GROUP BY ----------
        sql.append(" GROUP BY i.item_id, b.branch_name, i.brand_name, i.categories, i.product_categories, i.hsn_code, i.sku_code, i.ean, i.is_returnable, i.tax_id, i.product_status, i.branch_id, i.created_at, i.last_modified_at");

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
            sql.append(String.join(", ", orderClauses));
        }

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- Set Filter Params ----------
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

        // ---------- Set Search Params ----------
        if (search != null) {
            searchParamMap.forEach((paramName, mapKey) -> {
                String value = search.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    dataQuery.setParameter(paramName, value);
                    countQuery.setParameter(paramName, value);
                }
            });
        }

        // ---------- Pagination ----------
        if (filterRequest.getPaginationRequest() != null) {
            int pageNumber = filterRequest.getPaginationRequest().getPageNumber();
            int pageSize = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(pageNumber * pageSize);
            dataQuery.setMaxResults(pageSize);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        List<InventoryAndBranchProjection> resultList = new ArrayList<>();

        for (Object[] row : rows) {
            InventoryAndBranchProjection obj = new InventoryAndBranchProjection();

            obj.setItemId(((Number) row[0]).longValue());
            obj.setItemName((String) row[1]);
            obj.setBrandName((String) row[2]);
            obj.setCategories((String) row[3]);

            obj.setProductCategories(row[4] != null ? ProductCategories.valueOf((String) row[4]) : null);

            obj.setHsnCode((String) row[5]);
            obj.setSkuCode((String) row[6]);
            obj.setEan((String) row[7]);
            obj.setReturnable(row[8] != null && ((Boolean) row[8]));
            obj.setTaxId(((Number) row[9]).longValue());

            obj.setProductStatus(row[10] != null ? ProductStatus.valueOf((String) row[10]) : null);

            obj.setBranchId(((Number) row[11]).longValue());
            obj.setCreatedAt(row[12] != null ? ((Timestamp) row[12]).toLocalDateTime() : null);
            obj.setLastModifiedAt(row[13] != null ? ((Timestamp) row[13]).toLocalDateTime() : null);

            obj.setTotalStockQuantity(((Number) row[14]).intValue());
            obj.setLatestExpiryDate(row[15] != null ? ((Timestamp) row[15]).toLocalDateTime() : null);

            obj.setBranchName((String) row[16]);

            resultList.add(obj);
        }


        ResultDto<InventoryAndBranchProjection> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(resultList);

        log.info("Exit [InventoryCustomRepository] [getInventoryDetails] with count = {}", totalCount);
        return resultDto;
    }
}
