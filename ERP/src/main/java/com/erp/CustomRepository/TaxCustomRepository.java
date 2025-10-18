package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Projection.TaxProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class TaxCustomRepository {

    private final Map<String, String> filterParamMap = Map.of(
            "taxName", "taxName",
            "taxType", "taxType",
            "startDate", "startDate",
            "endDate", "endDate",
            "minRate", "minRate",
            "maxRate", "maxRate"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "taxIdSearch", "taxId",
            "taxNameSearch", "taxName",
            "taxRateSearch", "taxRate"
    );

    @PersistenceContext
    private EntityManager entityManager;

    public List<TaxProjection> getTaxDetails(FilterRequest filterRequest) {
        log.info("Into [TaxCustomRepository] [getTaxDetails]");

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    t.id,
                    t.tax_name,
                    t.tax_type,
                    t.tax_rate,
                    t.created_at
                FROM tax t
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // Filters
        if (filters != null && !filters.isEmpty()) {

            if (filters.containsKey("taxName"))
                sql.append(" AND t.tax_name = :taxName");

            if (filters.containsKey("taxType"))
                sql.append(" AND t.tax_type = :taxType");

            if (filters.containsKey("minRate") && filters.containsKey("maxRate"))
                sql.append(" AND t.tax_rate BETWEEN :minRate AND :maxRate");
            else if (filters.containsKey("minRate"))
                sql.append(" AND t.tax_rate >= :minRate");
            else if (filters.containsKey("maxRate"))
                sql.append(" AND t.tax_rate <= :maxRate");

            // Date filter
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                sql.append(" AND t.created_at BETWEEN :startDate AND :endDate");
            } else if (filters.containsKey("startDate")) {
                sql.append(" AND t.created_at >= :startDate");
            } else if (filters.containsKey("endDate")) {
                sql.append(" AND t.created_at <= :endDate");
            }
        }

        // Search (LIKE)
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("taxIdSearch"))
                sql.append(" AND CAST(t.id AS CHAR) LIKE CONCAT('%', :taxIdSearch, '%')");
            if (search.containsKey("taxNameSearch"))
                sql.append(" AND LOWER(t.tax_name) LIKE LOWER(CONCAT('%', :taxNameSearch, '%'))");
            if (search.containsKey("taxRateSearch"))
                sql.append(" AND CAST(t.tax_rate AS CHAR) LIKE CONCAT('%', :taxRateSearch, '%')");
        }

        // Sorting by created date
        if (orderBy != null && orderBy.containsKey("createdAt")) {
            sql.append(" ORDER BY t.created_at ")
                    .append(orderBy.get("createdAt").equalsIgnoreCase("desc") ? "DESC" : "ASC");
        }

        log.info("[TaxCustomRepository] [getTaxDetails] :: Final Query {}", sql);

        Query query = entityManager.createNativeQuery(sql.toString());

        // Bind filter parameters
        if (filters != null) {
            filterParamMap.forEach((paramName, mapKey) -> {
                String value = filters.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    try {
                        switch (paramName) {
                            case "startDate", "endDate" ->
                                    query.setParameter(paramName, Timestamp.valueOf(value));
                            case "minRate", "maxRate" ->
                                    query.setParameter(paramName, new BigDecimal(value));
                            default ->
                                    query.setParameter(paramName, value);
                        }
                    } catch (Exception e) {
                        log.warn("Skipping invalid filter param {} with value {}", paramName, value);
                    }
                }
            });
        }

        // Bind search parameters
        if (search != null) {
            searchParamMap.forEach((paramName, mapKey) -> {
                String value = search.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    query.setParameter(paramName, value);
                }
            });
        }

        // Pagination
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }

        // Map results
        List<Object[]> rows = query.getResultList();
        List<TaxProjection> result = new ArrayList<>();

        for (Object[] row : rows) {
            TaxProjection dto = new TaxProjection();
            dto.setId(((Number) row[0]).longValue());
            dto.setTaxName((String) row[1]);
            dto.setTaxType((String) row[2]);
            dto.setTaxRate((BigDecimal) row[3]);
            dto.setCreatedAt(row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null);
            result.add(dto);
        }

        log.info("Exit [TaxCustomRepository] [getTaxDetails]");
        return result;
    }
}
