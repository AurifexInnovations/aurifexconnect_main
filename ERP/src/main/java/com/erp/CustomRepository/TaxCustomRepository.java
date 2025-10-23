package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.PaginationRequest;
import com.erp.Projection.TaxProjection;
import com.erp.Enum.TaxName;
import com.erp.Enum.TaxType;
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

    @PersistenceContext
    private EntityManager entityManager;

    public List<TaxProjection> getTaxDetails(FilterRequest filterRequest) {
        log.info("Into [TaxCustomRepository] [getTaxDetails]");

        try {
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

            // ----- FILTER CONDITIONS (exact match & ranges) -----
            if (filters != null) {
                filters.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        switch (key) {
                            case "id" -> sql.append(" AND t.id = :id");
                            case "taxName" -> sql.append(" AND t.tax_name = :taxName");
                            case "taxType" -> sql.append(" AND t.tax_type = :taxType");
                            case "minRate" -> sql.append(" AND t.tax_rate >= :minRate");
                            case "maxRate" -> sql.append(" AND t.tax_rate <= :maxRate");
                            case "startDate" -> sql.append(" AND t.created_at >= :startDate");
                            case "endDate" -> sql.append(" AND t.created_at <= :endDate");
                        }
                    }
                });
            }

            // ----- SEARCH CONDITIONS (partial match) -----
            if (search != null) {
                search.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        switch (key) {
                            case "taxId", "taxIdSearch" ->
                                    sql.append(" AND CAST(t.id AS TEXT) LIKE :taxIdSearch");
                            case "taxName", "taxNameSearch" ->
                                    sql.append(" AND LOWER(t.tax_name) LIKE :taxNameSearch");
                            case "taxType", "taxTypeSearch" ->
                                    sql.append(" AND LOWER(t.tax_type) LIKE :taxTypeSearch");
                            case "taxRate", "taxRateSearch" ->
                                    sql.append(" AND CAST(t.tax_rate AS TEXT) LIKE :taxRateSearch");
                        }
                    }
                });
            }

            // ----- ORDER BY -----
            if (orderBy != null && !orderBy.isEmpty()) {
                sql.append(" ORDER BY ");
                List<String> orderClauses = new ArrayList<>();
                orderBy.forEach((column, direction) -> {
                    String dir = direction.equalsIgnoreCase("desc") ? "DESC" : "ASC";
                    switch (column) {
                        case "createdAt" -> orderClauses.add("t.created_at " + dir);
                        case "taxName" -> orderClauses.add("t.tax_name " + dir);
                        case "taxRate" -> orderClauses.add("t.tax_rate " + dir);
                    }
                });
                if (!orderClauses.isEmpty()) sql.append(String.join(", ", orderClauses));
            }

            Query query = entityManager.createNativeQuery(sql.toString());

            // ----- SET FILTER PARAMETERS -----
            if (filters != null) {
                filters.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        try {
                            switch (key) {
                                case "id" -> query.setParameter("id", Long.parseLong(value));
                                case "taxName" -> {
                                    try {
                                        query.setParameter("taxName", TaxName.valueOf(value.toUpperCase()).name());
                                    } catch (IllegalArgumentException ignored) {
                                        log.warn("Invalid TaxName ignored: {}", value);
                                    }
                                }
                                case "taxType" -> {
                                    try {
                                        query.setParameter("taxType", TaxType.valueOf(value.toUpperCase()).name());
                                    } catch (IllegalArgumentException ignored) {
                                        log.warn("Invalid TaxType ignored: {}", value);
                                    }
                                }
                                case "minRate" -> query.setParameter("minRate", new BigDecimal(value));
                                case "maxRate" -> query.setParameter("maxRate", new BigDecimal(value));
                                case "startDate" -> query.setParameter("startDate", Timestamp.valueOf(value));
                                case "endDate" -> query.setParameter("endDate", Timestamp.valueOf(value));
                            }
                        } catch (Exception e) {
                            log.warn("Skipping invalid filter param {} with value {}: {}", key, value, e.getMessage());
                        }
                    }
                });
            }

            // ----- SET SEARCH PARAMETERS -----
            if (search != null) {
                search.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        switch (key) {
                            case "taxId", "taxIdSearch" -> query.setParameter("taxIdSearch", "%" + value + "%");
                            case "taxName", "taxNameSearch" -> query.setParameter("taxNameSearch", "%" + value.toLowerCase() + "%");
                            case "taxType", "taxTypeSearch" -> query.setParameter("taxTypeSearch", "%" + value.toLowerCase() + "%");
                            case "taxRate", "taxRateSearch" -> query.setParameter("taxRateSearch", "%" + value + "%");
                        }
                    }
                });
            }

            // ----- PAGINATION -----
            PaginationRequest pageRequest = filterRequest.getPaginationRequest();
            if (pageRequest != null) {
                int page = pageRequest.getPageNumber() != null ? pageRequest.getPageNumber() : 0;
                int size = pageRequest.getPageSize() != null ? pageRequest.getPageSize() : 10;
                query.setFirstResult(page * size);
                query.setMaxResults(size);
            }

            // ----- EXECUTE QUERY -----
            @SuppressWarnings("unchecked")
            List<Object[]> rows = query.getResultList();

            List<TaxProjection> result = new ArrayList<>();
            for (Object[] row : rows) {
                TaxProjection dto = new TaxProjection();
                dto.setId(((Number) row[0]).longValue());
                dto.setTaxName(row[1].toString());
                dto.setTaxType(row[2].toString());
                dto.setTaxRate((BigDecimal) row[3]);
                dto.setCreatedAt(row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null);
                result.add(dto);
            }

            log.info("Exit [TaxCustomRepository] [getTaxDetails] with count = {}", result.size());
            return result;

        } catch (Exception e) {
            log.error("Exception in [TaxCustomRepository] [getTaxDetails]: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch tax details", e);
        }
    }
}
