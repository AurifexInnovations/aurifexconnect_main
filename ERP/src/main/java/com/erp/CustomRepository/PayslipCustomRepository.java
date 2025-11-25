package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.PayslipResponseDTO;
import com.erp.Dto.Response.ResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class PayslipCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // =====================================================================
    // FILTER PAYSLIPS
    // =====================================================================
    public ResultDto<PayslipResponseDTO> filterPayslips(FilterRequest filterRequest) {

        log.info("START :: filterPayslips");

        // ==============================
        // BASE MAIN QUERY
        // ==============================
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    p.payslip_id,
                    p.employee_id,
                    p.pay_period_start,
                    p.pay_period_end,
                    p.gross_salary,
                    p.total_deductions,
                    p.net_pay,
                    p.status,
                    p.generation_date,
                    p.payslip_number
                FROM payslips p
                WHERE p.is_active = TRUE
                """);

        // ==============================
        // BASE COUNT QUERY
        // ==============================
        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*)
                FROM payslips p
                WHERE p.is_active = TRUE
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // =====================================================================
        // FILTER CONDITIONS
        // =====================================================================
        if (filters != null) {

            if (filters.containsKey("payslipId")) {
                sql.append(" AND p.payslip_id = :payslipId");
                countSql.append(" AND p.payslip_id = :payslipId");
            }

            if (filters.containsKey("employeeId")) {
                sql.append(" AND p.employee_id = :employeeId");
                countSql.append(" AND p.employee_id = :employeeId");
            }

            if (filters.containsKey("payPeriodStart")) {
                sql.append(" AND p.pay_period_start = :payPeriodStart");
                countSql.append(" AND p.pay_period_start = :payPeriodStart");
            }

            if (filters.containsKey("payPeriodEnd")) {
                sql.append(" AND p.pay_period_end = :payPeriodEnd");
                countSql.append(" AND p.pay_period_end = :payPeriodEnd");
            }

            if (filters.containsKey("status")) {
                sql.append(" AND p.status = :status");
                countSql.append(" AND p.status = :status");
            }

            if (filters.containsKey("issueDate")) {
                sql.append(" AND p.generation_date = :issueDate");
                countSql.append(" AND p.generation_date = :issueDate");
            }
        }

        // =====================================================================
        // SEARCH CONDITIONS
        // =====================================================================
        if (search != null) {

            if (search.containsKey("payPeriod")) {
                sql.append("""
                        AND (
                            CAST(p.pay_period_start AS TEXT) LIKE :payPeriod 
                            OR CAST(p.pay_period_end AS TEXT) LIKE :payPeriod
                        )
                        """);

                countSql.append("""
                        AND (
                            CAST(p.pay_period_start AS TEXT) LIKE :payPeriod 
                            OR CAST(p.pay_period_end AS TEXT) LIKE :payPeriod
                        )
                        """);
            }

            if (search.containsKey("status")) {
                sql.append(" AND LOWER(p.status) LIKE :statusSearch");
                countSql.append(" AND LOWER(p.status) LIKE :statusSearch");
            }
        }

        // =====================================================================
        // ORDER BY
        // =====================================================================
        sql.append(" ORDER BY ");

        if (orderBy != null && !orderBy.isEmpty()) {

            List<String> orderList = new ArrayList<>();

            for (var entry : orderBy.entrySet()) {

                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";

                switch (entry.getKey()) {

                    case "issueDate" -> orderList.add("p.generation_date " + direction);

                    case "netPay" -> orderList.add("p.net_pay " + direction);

                    case "grossSalary" -> orderList.add("p.gross_salary " + direction);

                    default -> orderList.add("p.payslip_id DESC");
                }
            }

            sql.append(String.join(", ", orderList));

        } else {
            sql.append(" p.payslip_id DESC");
        }

        log.info("FINAL SQL :: {}", sql);

        // =====================================================================
        // EXECUTE QUERY
        // =====================================================================
        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        bindParameters(filters, search, dataQuery, countQuery);

        // Pagination
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();

            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();

        List<Object[]> rows = dataQuery.getResultList();
        List<PayslipResponseDTO> results = new ArrayList<>();

        for (Object[] row : rows) {

            PayslipResponseDTO dto = new PayslipResponseDTO();
            dto.setPayslipId(getLong(row[0]));
            dto.setEmployeeId(getLong(row[1]));
            dto.setPayPeriodStart(getLocalDate(row[2]));
            dto.setPayPeriodEnd(getLocalDate(row[3]));
            dto.setGrossSalary(getBigDecimal(row[4]));
            dto.setTotalDeductions(getBigDecimal(row[5]));
            dto.setNetPay(getBigDecimal(row[6]));
            dto.setStatus(getString(row[7]));
            dto.setGenerationDate(getLocalDate(row[8]));
            dto.setPayslipNumber(getString(row[9]));

            results.add(dto);
        }

        ResultDto<PayslipResponseDTO> result = new ResultDto<>();
        result.setCount(totalCount);
        result.setResults(results);

        return result;
    }

    // =====================================================================
    // PARAMETER BINDER
    // =====================================================================
    private void bindParameters(Map<String, String> filters, Map<String, String> search,
                                Query dataQuery, Query countQuery) {

        if (filters != null) {

            filters.forEach((key, value) -> {
                if (value == null || value.trim().isEmpty()) return;

                switch (key) {

                    case "payslipId" -> {
                        Long id = Long.parseLong(value);
                        dataQuery.setParameter("payslipId", id);
                        countQuery.setParameter("payslipId", id);
                    }

                    case "employeeId" -> {
                        Long eid = Long.parseLong(value);
                        dataQuery.setParameter("employeeId", eid);
                        countQuery.setParameter("employeeId", eid);
                    }

                    case "payPeriodStart" -> {
                        LocalDate d = LocalDate.parse(value);
                        dataQuery.setParameter("payPeriodStart", java.sql.Date.valueOf(d));
                        countQuery.setParameter("payPeriodStart", java.sql.Date.valueOf(d));
                    }

                    case "payPeriodEnd" -> {
                        LocalDate d2 = LocalDate.parse(value);
                        dataQuery.setParameter("payPeriodEnd", java.sql.Date.valueOf(d2));
                        countQuery.setParameter("payPeriodEnd", java.sql.Date.valueOf(d2));
                    }

                    case "status" -> {
                        dataQuery.setParameter("status", value);
                        countQuery.setParameter("status", value);
                    }

                    case "issueDate" -> {
                        LocalDate d3 = LocalDate.parse(value);
                        dataQuery.setParameter("issueDate", java.sql.Date.valueOf(d3));
                        countQuery.setParameter("issueDate", java.sql.Date.valueOf(d3));
                    }
                }
            });
        }

        if (search != null) {

            search.forEach((key, value) -> {
                if (value == null || value.trim().isEmpty()) return;

                String pattern = "%" + value.toLowerCase() + "%";

                switch (key) {

                    case "payPeriod" -> {
                        dataQuery.setParameter("payPeriod", pattern);
                        countQuery.setParameter("payPeriod", pattern);
                    }

                    case "status" -> {
                        dataQuery.setParameter("statusSearch", pattern);
                        countQuery.setParameter("statusSearch", pattern);
                    }
                }
            });
        }
    }

    // =====================================================================
    // SAFE TYPE CONVERSIONS
    // =====================================================================
    private Long getLong(Object o) {
        return o == null ? null : ((Number) o).longValue();
    }

    private String getString(Object o) {
        return o == null ? null : o.toString();
    }

    private LocalDate getLocalDate(Object o) {
        return o == null ? null : ((java.sql.Date) o).toLocalDate();
    }

    private BigDecimal getBigDecimal(Object o) {
        return o == null ? null : new BigDecimal(o.toString());
    }
}
