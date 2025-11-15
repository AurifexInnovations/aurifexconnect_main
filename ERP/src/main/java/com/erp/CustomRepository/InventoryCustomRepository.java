package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.ProductCategories;
import com.erp.Enum.ProductStatus;
import com.erp.Enum.TaxName;
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

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<InventoryAndBranchProjection> getInventoryDetails(FilterRequest filterRequest) {
        log.info("Into [InventoryCustomRepository] [getInventoryDetails]");

        // =================== BASE QUERY ===================
        StringBuilder sql = new StringBuilder("""
                SELECT
                    i.item_id,
                    i.item_name,
                    i.item_quantity,
                    i.item_description,
                    i.item_cost,
                    i.categories,
                    i.product_categories,
                    i.brand_name,
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
                    b.branch_name,
                    f.file_url,
                    t.tax_name
                FROM inventory i
                INNER JOIN branch b ON i.branch_id = b.branch_id
                LEFT JOIN varients v ON i.item_id = v.item_id
                LEFT JOIN files f ON f.gen_id = i.item_id AND f.category = 'Inventory'
                LEFT JOIN tax t ON i.tax_id = t.id
                WHERE i.active = true
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(DISTINCT i.item_id)
                FROM inventory i
                INNER JOIN branch b ON i.branch_id = b.branch_id
                LEFT JOIN varients v ON i.item_id = v.item_id
                LEFT JOIN files f ON f.gen_id = i.item_id AND f.category = 'Inventory'
                LEFT JOIN tax t ON i.tax_id = t.id
                WHERE i.active = true
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // =================== APPLY FILTERS ===================
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.trim().isEmpty()) {
                    switch (key) {
                        case "itemName" -> {
                            sql.append(" AND i.item_name = :itemName");
                            countSql.append(" AND i.item_name = :itemName");
                        }
                        case "brandName" -> {
                            sql.append(" AND i.brand_name = :brandName");
                            countSql.append(" AND i.brand_name = :brandName");
                        }
                        case "productCategories" -> {
                            sql.append(" AND i.product_categories = :productCategories");
                            countSql.append(" AND i.product_categories = :productCategories");
                        }
                        case "branchId" -> {
                            sql.append(" AND i.branch_id = :branchId");
                            countSql.append(" AND i.branch_id = :branchId");
                        }
                        case "itemId" -> {
                            sql.append(" AND i.item_id = :itemId");
                            countSql.append(" AND i.item_id = :itemId");
                        }
                        case "startDate" -> sql.append(" AND i.created_at >= :startDate");
                        case "endDate" -> sql.append(" AND i.created_at <= :endDate");
                    }
                }
            });
        }

        // =================== APPLY SEARCH ===================
        if (search != null) {
            search.forEach((key, value) -> {
                if (value != null && !value.trim().isEmpty()) {
                    switch (key) {
                        case "itemName" -> {
                            sql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :searchItemName, '%'))");
                            countSql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :searchItemName, '%'))");
                        }
                        case "categories" -> {
                            sql.append(" AND LOWER(i.categories) LIKE LOWER(CONCAT('%', :categories, '%'))");
                            countSql.append(" AND LOWER(i.categories) LIKE LOWER(CONCAT('%', :categories, '%'))");
                        }
                        case "branchName" -> {
                            sql.append(" AND LOWER(b.branch_name) LIKE LOWER(CONCAT('%', :branchName, '%'))");
                            countSql.append(" AND LOWER(b.branch_name) LIKE LOWER(CONCAT('%', :branchName, '%'))");
                        }
                    }
                }
            });
        }

        // =================== GROUP BY ===================
        sql.append(" GROUP BY i.item_id, b.branch_name, t.tax_name,f.file_url");

        // =================== ORDER BY ===================
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            orderBy.forEach((column, dir) -> sql.append(" i.").append(column).append(" ").append(dir).append(","));
            sql.deleteCharAt(sql.length() - 1);
        } else {
            sql.append(" ORDER BY i.created_at DESC");
        }

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // =================== SET PARAMETERS ===================
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.trim().isEmpty()) {
                    switch (key) {
                        case "itemName", "brandName", "productCategories" -> {
                            dataQuery.setParameter(key, value);
                            countQuery.setParameter(key, value);
                        }
                        case "branchId", "itemId" -> {
                            dataQuery.setParameter(key, Long.parseLong(value));
                            countQuery.setParameter(key, Long.parseLong(value));
                        }
                        case "startDate" -> {
                            dataQuery.setParameter("startDate", Timestamp.valueOf(value + " 00:00:00"));
                            countQuery.setParameter("startDate", Timestamp.valueOf(value + " 00:00:00"));
                        }
                        case "endDate" -> {
                            dataQuery.setParameter("endDate", Timestamp.valueOf(value + " 23:59:59"));
                            countQuery.setParameter("endDate", Timestamp.valueOf(value + " 23:59:59"));
                        }
                    }
                }
            });
        }

        if (search != null) {
            search.forEach((key, value) -> {
                if (value != null && !value.trim().isEmpty()) {
                    dataQuery.setParameter(key.equals("itemName") ? "searchItemName" : key, value.trim());
                    countQuery.setParameter(key.equals("itemName") ? "searchItemName" : key, value.trim());
                }
            });
        }

        // =================== PAGINATION ===================
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        // =================== EXECUTE ===================
        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> resultRows = dataQuery.getResultList();
        List<InventoryAndBranchProjection> responseList = new ArrayList<>();

        for (Object[] r : resultRows) {
            InventoryAndBranchProjection obj = new InventoryAndBranchProjection();
            obj.setItemId(((Number) r[0]).longValue());
            obj.setItemName((String) r[1]);
            obj.setItemQuantity(r[2] != null ? ((Number) r[2]).doubleValue() : 0);
            obj.setItemDescription((String) r[3]);
            obj.setItemCost(r[4] != null ? ((Number) r[4]).doubleValue() : 0);
            obj.setCategories((String) r[5]);
            obj.setProductCategories(r[6] != null ? ProductCategories.valueOf((String) r[6]) : null);
            obj.setBrandName((String) r[7]);
            obj.setHsnCode((String) r[8]);
            obj.setSkuCode((String) r[9]);
            obj.setEan((String) r[10]);
            obj.setReturnable(r[11] != null ? (Boolean) r[11] : false);
            obj.setTaxId(r[12] != null ? ((Number) r[12]).longValue() : null);
            obj.setProductStatus(r[13] != null ? ProductStatus.valueOf((String) r[13]) : null);
            obj.setBranchId(r[14] != null ? ((Number) r[14]).longValue() : null);
            obj.setCreatedAt(r[15] != null ? ((Timestamp) r[15]).toLocalDateTime() : null);
            obj.setLastModifiedAt(r[16] != null ? ((Timestamp) r[16]).toLocalDateTime() : null);
            obj.setTotalStockQuantity(r[17] != null ? ((Number) r[17]).intValue() : 0);
            obj.setLatestExpiryDate(r[18] != null ? ((Timestamp) r[18]).toLocalDateTime() : null);
            obj.setBranchName((String) r[19]);
            obj.setFileUrl((String) r[20]);
            obj.setTaxName(r[21] != null ? TaxName.valueOf((String) r[21]) : null);
            responseList.add(obj);
        }

        ResultDto<InventoryAndBranchProjection> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(responseList);

        log.info("Exit [InventoryCustomRepository] [getInventoryDetails] count={}", totalCount);
        return resultDto;
    }
}
