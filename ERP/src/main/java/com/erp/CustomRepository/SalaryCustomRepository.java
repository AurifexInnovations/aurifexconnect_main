package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Dto.Response.UserResponse;
import com.erp.Enum.AmountStatus;
import com.erp.Exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class SalaryCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final Map<String, String> filterParamMap = Map.of(
            "userId", "userId",
            "userName", "userName",
            "month", "month",
            "amountStatus", "amountStatus",
            "deductions", "deductions",
            "bonus", "bonus",
            "startPaymentDate", "startPaymentDate",
            "endPaymentDate", "endPaymentDate"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "userSearch", "userName",
            "monthSearch", "month",
            "amountStatusSearch", "amountStatus",
            "bonusSearch", "bonus",
            "deductionsSearch", "deductions"
    );

    public ResultDto<SalaryResponse> getSalaryDetails(FilterRequest filterRequest) {
        log.info("Into [SalaryCustomRepository] [getSalaryDetails]");

        ResultDto<SalaryResponse> resultDto = new ResultDto<>();
        List<SalaryResponse> results = new ArrayList<>();
        long totalCount = 0;

        try {
            // ---------- DATA QUERY ----------
            StringBuilder sql = new StringBuilder("""
                    SELECT 
                        s.id,
                        s.base_salary,
                        s.working_days,
                        s.paid_days,
                        s.deductions,
                        s.bonus,
                        s.net_salary,
                        s.amount_status,
                        s.month,
                        s.payment_date,
                        s.remarks,
                        u.id AS user_id,
                        CONCAT(u.first_name, ' ', u.last_name) AS full_name,
                        u.email,
                        u.phone_no
                    FROM salaries s
                    INNER JOIN users u ON s.user_id = u.id
                    WHERE 1=1
                    """);

            StringBuilder countSql = new StringBuilder("""
                    SELECT COUNT(*)
                    FROM salaries s
                    INNER JOIN users u ON s.user_id = u.id
                    WHERE 1=1
                    """);

            Map<String, String> filters = filterRequest.getFilterColumns();
            Map<String, String> search = filterRequest.getSearchColumns();
            Map<String, String> orderBy = filterRequest.getOrderByColumns();

            // ---------- FILTER CONDITIONS ----------
            if (filters != null && !filters.isEmpty()) {
                if (filters.containsKey("userId")) {
                    sql.append(" AND u.id = :userId");
                    countSql.append(" AND u.id = :userId");
                }
                if (filters.containsKey("userName")) {
                    sql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :userName, '%'))");
                    countSql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :userName, '%'))");
                }
                if (filters.containsKey("month")) {
                    sql.append(" AND s.month = :month");
                    countSql.append(" AND s.month = :month");
                }
                if (filters.containsKey("amountStatus")) {
                    sql.append(" AND s.amount_status = :amountStatus");
                    countSql.append(" AND s.amount_status = :amountStatus");
                }
                if (filters.containsKey("deductions")) {
                    sql.append(" AND s.deductions >= :deductions");
                    countSql.append(" AND s.deductions >= :deductions");
                }
                if (filters.containsKey("bonus")) {
                    // Filter by exact bonus value
                    sql.append(" AND s.bonus = :bonus");
                    countSql.append(" AND s.bonus = :bonus");
                }
                if (filters.containsKey("startPaymentDate") && filters.containsKey("endPaymentDate")) {
                    sql.append(" AND s.payment_date BETWEEN :startPaymentDate AND :endPaymentDate");
                    countSql.append(" AND s.payment_date BETWEEN :startPaymentDate AND :endPaymentDate");
                } else if (filters.containsKey("startPaymentDate")) {
                    sql.append(" AND s.payment_date >= :startPaymentDate");
                    countSql.append(" AND s.payment_date >= :startPaymentDate");
                } else if (filters.containsKey("endPaymentDate")) {
                    sql.append(" AND s.payment_date <= :endPaymentDate");
                    countSql.append(" AND s.payment_date <= :endPaymentDate");
                }
            }

            // ---------- SEARCH ----------
            if (search != null && !search.isEmpty()) {
                if (search.containsKey("userSearch")) {
                    sql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :userSearch, '%'))");
                    countSql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :userSearch, '%'))");
                }
                if (search.containsKey("monthSearch")) {
                    sql.append(" AND s.month = :monthSearch");
                    countSql.append(" AND s.month = :monthSearch");
                }
                if (search.containsKey("amountStatusSearch")) {
                    sql.append(" AND s.amount_status LIKE CONCAT('%', :amountStatusSearch, '%')");
                    countSql.append(" AND s.amount_status LIKE CONCAT('%', :amountStatusSearch, '%')");
                }
                if (search.containsKey("bonusSearch")) {
                    sql.append(" AND s.bonus >= :bonusSearch");
                    countSql.append(" AND s.bonus >= :bonusSearch");
                }
                if (search.containsKey("deductionsSearch")) {
                    sql.append(" AND s.deductions >= :deductionsSearch");
                    countSql.append(" AND s.deductions >= :deductionsSearch");
                }
            }

            // ---------- ORDER BY ----------
            if (orderBy != null && !orderBy.isEmpty()) {
                sql.append(" ORDER BY ");
                orderBy.forEach((column, direction) ->
                        sql.append("s.").append(column).append(" ").append(direction).append(", "));
                sql.delete(sql.length() - 2, sql.length());
            } else {
                sql.append(" ORDER BY s.id DESC");
            }

            Query dataQuery = entityManager.createNativeQuery(sql.toString());
            Query countQuery = entityManager.createNativeQuery(countSql.toString());

            // ---------- SET PARAMETERS ----------
            if (filters != null) {
                filterParamMap.forEach((param, key) -> {
                    String value = filters.get(key);
                    if (value != null && !value.isEmpty()) {
                        try {
                            switch (param) {
                                case "userId", "deductions" -> {
                                    long v = Long.parseLong(value);
                                    dataQuery.setParameter(param, v);
                                    countQuery.setParameter(param, v);
                                }
                                case "bonus" -> {
                                    double v = Double.parseDouble(value);
                                    dataQuery.setParameter(param, v);
                                    countQuery.setParameter(param, v);
                                }
                                case "startPaymentDate", "endPaymentDate" -> {
                                    dataQuery.setParameter(param, Date.valueOf(value));
                                    countQuery.setParameter(param, Date.valueOf(value));
                                }
                                default -> {
                                    dataQuery.setParameter(param, value);
                                    countQuery.setParameter(param, value);
                                }
                            }
                        } catch (Exception e) {
                            throw new ResourceNotFoundException(e.getMessage());
                        }
                    }
                });
            }

            if (search != null) {
                searchParamMap.forEach((param, key) -> {
                    String value = search.get(key);
                    if (value != null && !value.isEmpty()) {
                        try {
                            switch (param) {
                                case "deductionsSearch", "bonusSearch" -> {
                                    double v = Double.parseDouble(value);
                                    dataQuery.setParameter(param, v);
                                    countQuery.setParameter(param, v);
                                }
                                default -> {
                                    dataQuery.setParameter(param, value);
                                    countQuery.setParameter(param, value);
                                }
                            }
                        } catch (Exception e) {
                            throw new ResourceNotFoundException(e.getMessage());
                        }
                    }
                });
            }

            // ---------- PAGINATION ----------
            int page = 0, size = 10;
            if (filterRequest.getPaginationRequest() != null) {
                page = Math.max(0, filterRequest.getPaginationRequest().getPageNumber());
                size = Math.max(1, filterRequest.getPaginationRequest().getPageSize());
            }
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);

            // ---------- COUNT ----------
            totalCount = ((Number) countQuery.getSingleResult()).longValue();

            // ---------- MAP RESULTS ----------
            List<Object[]> rows = dataQuery.getResultList();
            for (Object[] row : rows) {
                SalaryResponse dto = new SalaryResponse();
                dto.setId(((Number) row[0]).longValue());
                dto.setBaseSalary(((Number) row[1]).longValue());
                dto.setWorkingDays(row[2] != null ? ((Number) row[2]).intValue() : null);
                dto.setPaidDays(row[3] != null ? ((Number) row[3]).intValue() : null);
                dto.setDeductions(((Number) row[4]).longValue());
                dto.setBonus(((Number) row[5]).longValue());
                dto.setNetSalary(((Number) row[6]).longValue());
                dto.setAmountStatus(row[7] != null ? AmountStatus.valueOf(row[7].toString()) : null);

                dto.setMonth(row[8] != null ? (row[8].toString().contains("-") ?
                        YearMonth.parse(row[8].toString().substring(0, 7)) : YearMonth.of(YearMonth.now().getYear(), Integer.parseInt(row[8].toString()))) : null);
                dto.setPaymentDate((convertToYearMonth(row[9])));
                dto.setRemarks((String) row[10]);

                UserResponse user = new UserResponse();
                user.setId(((Number) row[11]).longValue());
                user.setFullName((String) row[12]);
                user.setEmail((String) row[13]);
                user.setPhoneNo(row[14] != null ? ((Number) row[14]).longValue() : null);
                dto.setUser(user);

                results.add(dto);
            }

        } catch (Exception e) {
            throw new ResourceNotFoundException("Error fetching salary details: " + e.getMessage());
        }

        resultDto.setCount(totalCount);
        resultDto.setResults(results);
        return resultDto;
    }

    private YearMonth convertToYearMonth(Object obj) {
        if (obj == null) return null;
        try {
            String s = obj.toString().trim();
            if (s.matches("\\d{4}-\\d{2}")) {
                return YearMonth.parse(s);
            } else if (s.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return YearMonth.parse(s.substring(0, 7));
            } else if (obj instanceof Timestamp ts) {
                return YearMonth.from(ts.toLocalDateTime());
            } else if (obj instanceof Date d) {
                return YearMonth.from(d.toLocalDate());
            } else if (obj instanceof LocalDate ld) {
                return YearMonth.from(ld);
            }
        } catch (Exception e) {
            log.warn("Failed to parse YearMonth from: {}", obj, e);
        }
        return null;
    }
}
