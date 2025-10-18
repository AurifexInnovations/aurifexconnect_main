package com.erp.CustomRepository;


import com.erp.Dto.Request.FilterRequest;
import com.erp.Projection.VoucherProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class VoucherCustomRepository {

    private final Map<String, String> filterParamMap = Map.of(
            "voucherType", "voucherType",
            "startDate", "startDate",
            "endDate", "endDate"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "voucherTypeSearch", "voucherType",
            "voucherIndexSearch", "voucherIndex"
    );

    @PersistenceContext
    private EntityManager entityManager;

    public List<VoucherProjection> getVoucherDetails(FilterRequest filterRequest) {
        log.info("Into [VoucherCustomRepository] [getVoucherDetails]");

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    v.voucher_id,
                    v.voucher_type,
                    v.voucher_index,
                    v.start_date,
                    v.end_date
                FROM voucher v
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        //  Apply Filters
        if (filters != null && !filters.isEmpty()) {

            if (filters.containsKey("voucherType"))
                sql.append(" AND v.voucher_type = :voucherType");

            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
                sql.append(" AND v.start_date BETWEEN :startDate AND :endDate");
            else if (filters.containsKey("startDate"))
                sql.append(" AND v.start_date >= :startDate");
            else if (filters.containsKey("endDate"))
                sql.append(" AND v.end_date <= :endDate");
        }

        //  Apply Search (LIKE)
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("voucherTypeSearch"))
                sql.append(" AND LOWER(v.voucher_type) LIKE LOWER(CONCAT('%', :voucherTypeSearch, '%'))");
            if (search.containsKey("voucherIndexSearch"))
                sql.append(" AND LOWER(v.voucher_index) LIKE LOWER(CONCAT('%', :voucherIndexSearch, '%'))");
        }

        //  Sorting (Date or Name)
        if (orderBy != null && !orderBy.isEmpty()) {
            if (orderBy.containsKey("startDate")) {
                sql.append(" ORDER BY v.start_date ")
                        .append(orderBy.get("startDate").equalsIgnoreCase("desc") ? "DESC" : "ASC");
            } else if (orderBy.containsKey("voucherIndex")) {
                sql.append(" ORDER BY v.voucher_index ")
                        .append(orderBy.get("voucherIndex").equalsIgnoreCase("desc") ? "DESC" : "ASC");
            }
        }

        log.info("[VoucherCustomRepository] [getVoucherDetails] :: Final Query {}", sql);

        Query query = entityManager.createNativeQuery(sql.toString());

        //  Bind Filter Parameters
        if (filters != null) {
            filterParamMap.forEach((paramName, mapKey) -> {
                String value = filters.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    try {
                        switch (paramName) {
                            case "startDate", "endDate" ->
                                    query.setParameter(paramName, Date.valueOf(LocalDate.parse(value)));
                            default ->
                                    query.setParameter(paramName, value);
                        }
                    } catch (Exception e) {
                        log.warn("Skipping invalid filter param {} with value {}", paramName, value);
                    }
                }
            });
        }

        //  Bind Search Parameters
        if (search != null) {
            searchParamMap.forEach((paramName, mapKey) -> {
                String value = search.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    query.setParameter(paramName, value);
                }
            });
        }

        //  Pagination
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }

        //  Map results to Projection
        List<Object[]> rows = query.getResultList();
        List<VoucherProjection> result = new ArrayList<>();

        for (Object[] row : rows) {
            VoucherProjection dto = new VoucherProjection();
            dto.setVoucherId(((Number) row[0]).longValue());
            dto.setVoucherType((String) row[1]);
            dto.setVoucherIndex((String) row[2]);
            dto.setStartDate(row[3] != null ? ((Date) row[3]).toLocalDate() : null);
            dto.setEndDate(row[4] != null ? ((Date) row[4]).toLocalDate() : null);
            result.add(dto);
        }

        log.info("Exit [VoucherCustomRepository] [getVoucherDetails]");
        return result;
    }
}
