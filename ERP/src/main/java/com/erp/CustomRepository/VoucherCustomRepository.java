package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Projection.VoucherProjection;
import com.erp.Dto.Response.ResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class VoucherCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<VoucherProjection> getVoucherDetails(FilterRequest filterRequest) {
        log.info("Into [VoucherCustomRepository] [getVoucherDetails]");

        try {
            // ----- Validate columns -----
            if ((filterRequest.getFilterColumns() == null || filterRequest.getFilterColumns().isEmpty()) &&
                    (filterRequest.getSearchColumns() == null || filterRequest.getSearchColumns().isEmpty())) {
                throw new RuntimeException("No filter or search columns provided!");
            }

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

            StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM voucher v WHERE 1=1");

            Map<String, String> filters = filterRequest.getFilterColumns();
            Map<String, String> search = filterRequest.getSearchColumns();
            Map<String, String> orderBy = filterRequest.getOrderByColumns();

            // ----- FILTER CONDITIONS (Exact Match) -----
            if (filters != null) {
                filters.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        switch (key) {
                            case "voucher_type" -> {
                                sql.append(" AND v.voucher_type = :voucherType");
                                countSql.append(" AND v.voucher_type = :voucherType");
                            }
                            case "startDate" -> {
                                sql.append(" AND v.start_date >= :startDate");
                                countSql.append(" AND v.start_date >= :startDate");
                            }
                            case "endDate" -> {
                                sql.append(" AND v.end_date <= :endDate");
                                countSql.append(" AND v.end_date <= :endDate");
                            }
                        }
                    }
                });
            }

            // ----- SEARCH CONDITIONS (LIKE) -----
            if (search != null) {
                search.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        switch (key) {
                            case "voucher_type" -> {
                                sql.append(" AND LOWER(v.voucher_type) LIKE LOWER(CONCAT('%', :voucherTypeSearch, '%'))");
                                countSql.append(" AND LOWER(v.voucher_type) LIKE LOWER(CONCAT('%', :voucherTypeSearch, '%'))");
                            }
                            case "voucher_index" -> {
                                sql.append(" AND LOWER(v.voucher_index) LIKE LOWER(CONCAT('%', :voucherIndexSearch, '%'))");
                                countSql.append(" AND LOWER(v.voucher_index) LIKE LOWER(CONCAT('%', :voucherIndexSearch, '%'))");
                            }
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
                        case "startDate" -> orderClauses.add("v.start_date " + dir);
                        case "voucherIndex" -> orderClauses.add("v.voucher_index " + dir);
                        case "voucherType" -> orderClauses.add("v.voucher_type " + dir);
                    }
                });
                if (!orderClauses.isEmpty()) {
                    sql.append(String.join(", ", orderClauses));
                }
            }

            Query dataQuery = entityManager.createNativeQuery(sql.toString());
            Query countQuery = entityManager.createNativeQuery(countSql.toString());

            // ----- SET FILTER PARAMETERS -----
            if (filters != null) {
                filters.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        switch (key) {
                            case "voucher_type" -> {
                                dataQuery.setParameter("voucherType", value);
                                countQuery.setParameter("voucherType", value);
                            }
                            case "startDate", "endDate" -> {
                                dataQuery.setParameter(key, Date.valueOf(LocalDate.parse(value)));
                                countQuery.setParameter(key, Date.valueOf(LocalDate.parse(value)));
                            }
                        }
                    }
                });
            }

            // ----- SET SEARCH PARAMETERS -----
            if (search != null) {
                search.forEach((key, value) -> {
                    if (value != null && !value.isEmpty()) {
                        switch (key) {
                            case "voucher_type" -> {
                                dataQuery.setParameter("voucherTypeSearch", value);
                                countQuery.setParameter("voucherTypeSearch", value);
                            }
                            case "voucher_index" -> {
                                dataQuery.setParameter("voucherIndexSearch", value);
                                countQuery.setParameter("voucherIndexSearch", value);
                            }
                        }
                    }
                });
            }

            // ----- PAGINATION -----
            if (filterRequest.getPaginationRequest() != null) {
                Integer pageNumber = filterRequest.getPaginationRequest().getPageNumber();
                Integer pageSize = filterRequest.getPaginationRequest().getPageSize();

                if (pageNumber == null) pageNumber = 0;
                if (pageSize == null) pageSize = 10;

                dataQuery.setFirstResult(pageNumber * pageSize);
                dataQuery.setMaxResults(pageSize);
            }

            // ----- EXECUTE QUERIES -----
            long totalCount = ((Number) countQuery.getSingleResult()).longValue();
            @SuppressWarnings("unchecked")
            List<Object[]> rows = dataQuery.getResultList();

            if (rows.isEmpty()) {
                throw new RuntimeException("No vouchers found for the given filter and search criteria!");
            }

            // ----- MAP RESULTS -----
            List<VoucherProjection> resultList = new ArrayList<>();
            for (Object[] row : rows) {
                VoucherProjection vp = new VoucherProjection();
                vp.setVoucherId(row[0] != null ? ((Number) row[0]).longValue() : null);
                vp.setVoucherType((String) row[1]);
                vp.setVoucherIndex((String) row[2]);
                vp.setStartDate(row[3] != null ? ((Date) row[3]).toLocalDate() : null);
                vp.setEndDate(row[4] != null ? ((Date) row[4]).toLocalDate() : null);
                resultList.add(vp);
            }

            ResultDto<VoucherProjection> resultDto = new ResultDto<>();
            resultDto.setCount(totalCount);
            resultDto.setResults(resultList);

            log.info("Exit [VoucherCustomRepository] [getVoucherDetails] with count = {}", totalCount);

            return resultDto;

        } catch (Exception e) {
            log.error("Exception in [VoucherCustomRepository] [getVoucherDetails]: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch voucher details", e);
        }
    }
}
