package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Dto.Response.UserResponse;
import com.erp.Enum.AmountStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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
            "paymentStatus", "paymentStatus",
            "deductions", "deductions",
            "bonus", "bonus",
            "startPaymentDate", "startPaymentDate",
            "endPaymentDate", "endPaymentDate"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "userSearch", "userName",
            "monthSearch", "month",
            "paymentStatusSearch", "paymentStatus"
    );

    public List<SalaryResponse> getSalaryDetails(FilterRequest filterRequest) {
        log.info("Into [SalaryCustomRepository] [getSalaryDetails]");

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

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // 🔹 Apply filters
        if (filters != null && !filters.isEmpty()) {

            if (filters.containsKey("userId"))
                sql.append(" AND u.id = :userId");

            if (filters.containsKey("userName"))
                sql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :userName, '%'))");

            if (filters.containsKey("month"))
                sql.append(" AND s.month = :month");

            if (filters.containsKey("paymentStatus"))
                sql.append(" AND s.amount_status = :paymentStatus");

            if (filters.containsKey("deductions"))
                sql.append(" AND s.deductions >= :deductions");

            if (filters.containsKey("bonus"))
                sql.append(" AND s.bonus >= :bonus");

            // 🔹 Payment Date Range
            if (filters.containsKey("startPaymentDate") && filters.containsKey("endPaymentDate")) {
                sql.append(" AND s.payment_date BETWEEN :startPaymentDate AND :endPaymentDate");
            } else if (filters.containsKey("startPaymentDate")) {
                sql.append(" AND s.payment_date >= :startPaymentDate");
            } else if (filters.containsKey("endPaymentDate")) {
                sql.append(" AND s.payment_date <= :endPaymentDate");
            }
        }

        // 🔹 Apply search
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("userSearch"))
                sql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :userSearch, '%'))");

            if (search.containsKey("monthSearch"))
                sql.append(" AND s.month = :monthSearch");

            if (search.containsKey("paymentStatusSearch"))
                sql.append(" AND s.amount_status LIKE CONCAT('%', :paymentStatusSearch, '%')");
        }

        // 🔹 Order By
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            orderBy.forEach((column, direction) -> {
                sql.append("s.").append(column).append(" ").append(direction).append(", ");
            });
            sql.delete(sql.length() - 2, sql.length()); // remove trailing comma
        }

        log.info("[SalaryCustomRepository] [getSalaryDetails] :: Query = {}", sql);

        Query query = entityManager.createNativeQuery(sql.toString());

        // 🔹 Bind filter params
        if (filters != null) {
            filterParamMap.forEach((param, key) -> {
                String value = filters.get(key);
                if (value != null && !value.isEmpty()) {
                    switch (param) {
                        case "userId" -> query.setParameter(param, Long.parseLong(value));
                        case "deductions", "bonus" -> query.setParameter(param, Long.parseLong(value));
                        case "month" -> query.setParameter(param, value); // store as string YYYY-MM
                        case "startPaymentDate", "endPaymentDate" -> query.setParameter(param, Date.valueOf(value));
                        default -> query.setParameter(param, value);
                    }
                }
            });
        }

        // 🔹 Bind search params
        if (search != null) {
            searchParamMap.forEach((param, key) -> {
                String value = search.get(key);
                if (value != null && !value.isEmpty()) {
                    query.setParameter(param, value);
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

        List<Object[]> rows = query.getResultList();
        List<SalaryResponse> result = new ArrayList<>();

        for (Object[] row : rows) {
            SalaryResponse dto = new SalaryResponse();
            dto.setId(((Number) row[0]).longValue());
            dto.setBaseSalary(((Number) row[1]).longValue());
            dto.setWorkingDays(row[2] != null ? ((Number) row[2]).intValue() : null);
            dto.setPaidDays(row[3] != null ? ((Number) row[3]).intValue() : null);
            dto.setDeductions(((Number) row[4]).longValue());
            dto.setBonus(((Number) row[5]).longValue());
            dto.setNetSalary(((Number) row[6]).longValue());
            dto.setAmountStatus((AmountStatus) row[7]);
            dto.setMonth(row[8] != null ? YearMonth.parse(row[8].toString()) : null);
            dto.setPaymentDate(row[9] != null ? YearMonth.parse(row[9].toString()) : null);
            dto.setRemarks((String) row[10]);

            // User details
            UserResponse user = new UserResponse();
            user.setId(((Number) row[11]).longValue());
            user.setFullName(((String) row[12]));
            user.setEmail(((String) row[13]));
            user.setPhoneNo(((Number) row[14]).longValue());

            dto.setUser(user);

            result.add(dto);
        }


        log.info("Exit [SalaryCustomRepository] [getSalaryDetails]");
        return result;
    }
}
