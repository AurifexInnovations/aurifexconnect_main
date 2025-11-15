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
                    b.branch_name,
                    t.tax_name
                FROM inventory i
                INNER JOIN branch b ON i.branch_id = b.branch_id
                LEFT JOIN varients v ON i.item_id = v.item_id
                LEFT JOIN tax t ON i.tax_id = t.id
                WHERE i.active = true
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(DISTINCT i.item_id)
                FROM inventory i
                INNER JOIN branch b ON i.branch_id = b.branch_id
                LEFT JOIN varients v ON i.item_id = v.item_id
                LEFT JOIN tax t ON i.tax_id = t.id
                WHERE i.active = true
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // Filters
        if (filters != null) {
            if (filters.containsKey("itemName") && !filters.get("itemName").isEmpty()) {
                sql.append(" AND i.item_name = :itemName");
                countSql.append(" AND i.item_name = :itemName");
            }
            if (filters.containsKey("brandName") && !filters.get("brandName").isEmpty()) {
                sql.append(" AND i.brand_name = :brandName");
                countSql.append(" AND i.brand_name = :brandName");
            }
            if (filters.containsKey("productCategories") && !filters.get("productCategories").isEmpty()) {
                sql.append(" AND i.product_categories = :productCategories");
                countSql.append(" AND i.product_categories = :productCategories");
            }
            if (filters.containsKey("branchId") && !filters.get("branchId").isEmpty()) {
                sql.append(" AND i.branch_id = :branchId");
                countSql.append(" AND i.branch_id = :branchId");
            }
            if (filters.containsKey("itemId") && !filters.get("itemId").isEmpty()) {
                sql.append(" AND i.item_id = :itemId ");
                countSql.append(" AND i.item_id = :itemId ");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                sql.append(" AND i.created_at BETWEEN :startDate AND :endDate");
                countSql.append(" AND i.created_at BETWEEN :startDate AND :endDate");
            }
        }

        // Search Condition
        if (search != null && search.containsKey("itemName")) {
            sql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :searchItemName, '%'))");
            countSql.append(" AND LOWER(i.item_name) LIKE LOWER(CONCAT('%', :searchItemName, '%'))");
        }

        // Group By
        sql.append(" GROUP BY i.item_id, b.branch_name, t.tax_name");

        // Order By
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            orderBy.forEach((column, dir) -> sql.append(" i.").append(column).append(" ").append(dir).append(","));
            sql.deleteCharAt(sql.length() - 1);
        } else {
            sql.append(" ORDER BY i.created_at DESC");
        }

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // Set parameters
        if (filters != null) {
            if (filters.containsKey("itemName")) {
                dataQuery.setParameter("itemName", filters.get("itemName"));
                countQuery.setParameter("itemName", filters.get("itemName"));
            }
            if (filters.containsKey("brandName")) {
                dataQuery.setParameter("brandName", filters.get("brandName"));
                countQuery.setParameter("brandName", filters.get("brandName"));
            }
            if (filters.containsKey("productCategories")) {
                dataQuery.setParameter("productCategories", filters.get("productCategories"));
                countQuery.setParameter("productCategories", filters.get("productCategories"));
            }
            if (filters.containsKey("branchId") && !filters.get("branchId").isEmpty()) {
                dataQuery.setParameter("branchId", Long.parseLong(filters.get("branchId")));
                countQuery.setParameter("branchId", Long.parseLong(filters.get("branchId")));
            }
            if (filters.containsKey("itemId") && !filters.get("itemId").isEmpty()) {
                dataQuery.setParameter("itemId", Long.parseLong(filters.get("itemId")));
                countQuery.setParameter("itemId", Long.parseLong(filters.get("itemId")));
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                dataQuery.setParameter("startDate", Timestamp.valueOf(filters.get("startDate") + " 00:00:00"));
                dataQuery.setParameter("endDate", Timestamp.valueOf(filters.get("endDate") + " 23:59:59"));
                countQuery.setParameter("startDate", Timestamp.valueOf(filters.get("startDate") + " 00:00:00"));
                countQuery.setParameter("endDate", Timestamp.valueOf(filters.get("endDate") + " 23:59:59"));
            }
        }

        if (search != null && search.containsKey("itemName")) {
            dataQuery.setParameter("searchItemName", search.get("itemName"));
            countQuery.setParameter("searchItemName", search.get("itemName"));
        }

        // Pagination
        int page = filterRequest.getPaginationRequest().getPageNumber();
        int size = filterRequest.getPaginationRequest().getPageSize();
        dataQuery.setFirstResult(page * size);
        dataQuery.setMaxResults(size);

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> resultRows = dataQuery.getResultList();

        List<InventoryAndBranchProjection> responseList = new ArrayList<>();
        for (Object[] r : resultRows) {
            InventoryAndBranchProjection obj = new InventoryAndBranchProjection();
            obj.setItemId(((Number) r[0]).longValue());
            obj.setItemName((String) r[1]);
            obj.setBrandName((String) r[2]);
            obj.setCategories((String) r[3]);
            obj.setProductCategories(r[4] != null ? ProductCategories.valueOf((String) r[4]) : null);
            obj.setHsnCode((String) r[5]);
            obj.setSkuCode((String) r[6]);
            obj.setEan((String) r[7]);
            obj.setReturnable((Boolean) r[8]);
            obj.setTaxId(((Number) r[9]).longValue());
            obj.setProductStatus(r[10] != null ? ProductStatus.valueOf((String) r[10]) : null);
            obj.setBranchId(((Number) r[11]).longValue());
            obj.setCreatedAt(((Timestamp) r[12]).toLocalDateTime());
            obj.setLastModifiedAt(((Timestamp) r[13]).toLocalDateTime());
            obj.setTotalStockQuantity(((Number) r[14]).intValue());
            obj.setLatestExpiryDate(r[15] != null ? ((Timestamp) r[15]).toLocalDateTime() : null);
            obj.setBranchName((String) r[16]);
            obj.setTaxName(r[17] != null ? TaxName.valueOf((String) r[17]) : null);
            responseList.add(obj);
        }

        ResultDto<InventoryAndBranchProjection> result = new ResultDto<>();
        result.setCount(totalCount);
        result.setResults(responseList);

        return result;
    }
}

