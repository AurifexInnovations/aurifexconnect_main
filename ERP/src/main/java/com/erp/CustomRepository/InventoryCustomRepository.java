package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
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
            "itemNameSearch", "itemName",
            "categorySearch", "categories",
            "branchSearch", "branchName"
    );

    @PersistenceContext
    private EntityManager entityManager;

    public List<InventoryAndBranchProjection> getInventoryDetails(FilterRequest filterRequest) {
        log.info("Into [InventoryCustomRepository] [getInventoryDetails]");

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

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // 🔹 Exact filters
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("categories"))
                sql.append(" AND i.categories = :categories");
            if (filters.containsKey("branchId"))
                sql.append(" AND i.branch_branch_id = :branchId");

            // Date filter
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                sql.append(" AND i.created_at BETWEEN :startDate AND :endDate");
            } else if (filters.containsKey("startDate")) {
                sql.append(" AND i.created_at >= :startDate");
            } else if (filters.containsKey("endDate")) {
                sql.append(" AND i.created_at <= :endDate");
            }
        }

        // 🔹 Search filters (LIKE)
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("itemNameSearch"))
                sql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :itemNameSearch, '%'))");
            if (search.containsKey("categorySearch"))
                sql.append(" AND LOWER(i.categories) LIKE LOWER(CONCAT('%', :categorySearch, '%'))");
            if (search.containsKey("branchSearch"))
                sql.append(" AND LOWER(b.branch_name) LIKE LOWER(CONCAT('%', :branchSearch, '%'))");
        }

        log.info("[InventoryCustomRepository] [getInventoryDetails] :: Query {} ", sql.toString());

        Query query = entityManager.createNativeQuery(sql.toString());

        // 🔹 Bind filters dynamically
        if (filters != null) {
            filterParamMap.forEach((paramName, mapKey) -> {
                String value = filters.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    switch (paramName) {
                        case "branchId" -> query.setParameter(paramName, Long.parseLong(value));
                        case "startDate", "endDate" -> query.setParameter(paramName, Timestamp.valueOf(value));
                        default -> query.setParameter(paramName, value);
                    }
                }
            });
        }

        // 🔹 Bind search parameters dynamically
        if (search != null) {
            searchParamMap.forEach((paramName, mapKey) -> {
                String value = search.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    query.setParameter(paramName, value);
                }
            });
        }

        // 🔹 Pagination
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }


        // Map results manually into InventoryAndBranchProjection
        List<Object[]> rows = query.getResultList();
        List<InventoryAndBranchProjection> result = new ArrayList<>();

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

            result.add(obj);
        }

        log.info("Exit [InventoryCustomRepository] [getInventoryDetails]");
        return result;
    }
}
