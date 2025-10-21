package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.LeaveResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.LeaveType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class LeaveCustomRepository {

    private final Map<String, String> filterParamMap = Map.of(
            "leaveType", "leaveType",
            "status", "status",
            "leaveBalance", "leaveBalance",
            "startDate", "startDate",
            "endDate", "endDate",
            "reason", "reason"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "userIdSearch", "userId",
            "userNameSearch", "userName",
            "leaveTypeSearch", "leaveType",
            "reasonSearch", "reason"
    );

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<LeaveResponse> getFilteredLeaves(FilterRequest filterRequest) {
        log.info("Into [LeaveCustomRepository] [getFilteredLeaves]");

        // ---------- BASE DATA QUERY ----------
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    l.id,
                    l.start_date,
                    l.end_date,
                    l.leave_type,
                    l.reason,
                    l.status,
                    u.id AS user_id,
                    u.first_name,
                    u.last_name,
                    u.email,
                    u.phone_no
                
                FROM leave_request l
                INNER JOIN users u ON l.user_id = u.id
                WHERE 1=1
                """);

        // ---------- BASE COUNT QUERY ----------
        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) 
                FROM leave_request l
                INNER JOIN users u ON l.user_id = u.id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ---------- FILTER CONDITIONS ----------
        if (filters != null) {
            if (filters.containsKey("leaveType") && !filters.get("leaveType").isEmpty()) {
                sql.append(" AND l.leave_type = :leaveType");
                countSql.append(" AND l.leave_type = :leaveType");
            }
            if (filters.containsKey("status") && !filters.get("status").isEmpty()) {
                sql.append(" AND l.status = :status");
                countSql.append(" AND l.status = :status");
            }
            if (filters.containsKey("leaveBalance") && !filters.get("leaveBalance").isEmpty()) {
                sql.append(" AND l.leave_balance = :leaveBalance");
                countSql.append(" AND l.leave_balance = :leaveBalance");
            }
            if (filters.containsKey("reason") && !filters.get("reason").isEmpty()) {
                sql.append(" AND LOWER(l.reason) = LOWER(:reason)");
                countSql.append(" AND LOWER(l.reason) = LOWER(:reason)");
            }
            if (filters.containsKey("startDate") && !filters.get("startDate").isEmpty()
                    && filters.containsKey("endDate") && !filters.get("endDate").isEmpty()) {
                sql.append(" AND l.start_date BETWEEN :startDate AND :endDate");
                countSql.append(" AND l.start_date BETWEEN :startDate AND :endDate");
            } else if (filters.containsKey("startDate") && !filters.get("startDate").isEmpty()) {
                sql.append(" AND l.start_date >= :startDate");
                countSql.append(" AND l.start_date >= :startDate");
            } else if (filters.containsKey("endDate") && !filters.get("endDate").isEmpty()) {
                sql.append(" AND l.end_date <= :endDate");
                countSql.append(" AND l.end_date <= :endDate");
            }
        }

        // ---------- SEARCH CONDITIONS ----------
        if (search != null) {
            if (search.containsKey("leaveType") && !search.get("leaveType").isEmpty()) {
                sql.append(" AND l.leave_type::text LIKE :leaveTypeSearch");
                countSql.append(" AND l.leave_type::text LIKE :leaveTypeSearch");
            }
            if (search.containsKey("reason") && !search.get("reason").isEmpty()) {
                sql.append(" AND LOWER(l.reason) LIKE :reasonSearch");
                countSql.append(" AND LOWER(l.reason) LIKE :reasonSearch");
            }
            if (search.containsKey("userName") && !search.get("userName").isEmpty()) {
                sql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE :userNameSearch");
                countSql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE :userNameSearch");
            }
            if (search.containsKey("userId") && !search.get("userId").isEmpty()) {
                sql.append(" AND u.id = :userIdSearch");
                countSql.append(" AND u.id = :userIdSearch");
            }
        }

        // ---------- ORDER BY ----------
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderClauses = new ArrayList<>();
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String column = entry.getKey();
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";
                switch (column) {
                    case "startDate" -> orderClauses.add("l.start_date " + direction);
                    case "endDate" -> orderClauses.add("l.end_date " + direction);
                    case "leaveType" -> orderClauses.add("l.leave_type " + direction);
                    case "status" -> orderClauses.add("l.status " + direction);
                    default -> orderClauses.add("l.id DESC");
                }
            }
            sql.append(String.join(", ", orderClauses));
        } else {
            sql.append(" ORDER BY l.id DESC");
        }

        log.info("[LeaveCustomRepository] [getFilteredLeaves] :: Query {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- BIND FILTER PARAMETERS ----------
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "leaveType" -> {
                            dataQuery.setParameter("leaveType", value.toUpperCase());
                            countQuery.setParameter("leaveType", value.toUpperCase());
                        }
                        case "status" -> {
                            dataQuery.setParameter("status", value.toUpperCase());
                            countQuery.setParameter("status", value.toUpperCase());
                        }
                        case "leaveBalance" -> {
                            int balance = Integer.parseInt(value);
                            dataQuery.setParameter("leaveBalance", balance);
                            countQuery.setParameter("leaveBalance", balance);
                        }
                        case "reason" -> {
                            dataQuery.setParameter("reason", value.toLowerCase());
                            countQuery.setParameter("reason", value.toLowerCase());
                        }
                        case "startDate" -> {
                            Date start = Date.valueOf(value);
                            dataQuery.setParameter("startDate", start);
                            countQuery.setParameter("startDate", start);
                        }
                        case "endDate" -> {
                            Date end = Date.valueOf(value);
                            dataQuery.setParameter("endDate", end);
                            countQuery.setParameter("endDate", end);
                        }
                    }
                }
            });
        }

        // ---------- BIND SEARCH PARAMETERS ----------
        if (search != null) {
            search.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "leaveType" -> {
                            dataQuery.setParameter("leaveTypeSearch", "%" + value.toUpperCase() + "%");
                            countQuery.setParameter("leaveTypeSearch", "%" + value.toUpperCase() + "%");
                        }
                        case "reason" -> {
                            dataQuery.setParameter("reasonSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("reasonSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "userName" -> {
                            dataQuery.setParameter("userNameSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("userNameSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "userId" -> {
                            dataQuery.setParameter("userIdSearch", Long.parseLong(value));
                            countQuery.setParameter("userIdSearch", Long.parseLong(value));
                        }
                    }
                }
            });
        }

        // ---------- PAGINATION ----------
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        // ---------- EXECUTE QUERIES ----------
        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        // ---------- MAP RESULTS ----------
        List<LeaveResponse> results = new ArrayList<>();
        for (Object[] row : rows) {
            LeaveResponse response = new LeaveResponse();
            response.setId(((Number) row[0]).longValue());
            response.setStartDate(((Date) row[1]).toLocalDate());
            response.setEndDate(((Date) row[2]).toLocalDate());
            response.setLeaveType(row[3] != null ? LeaveType.valueOf(row[3].toString()) : null);
            response.setReason((String) row[4]);
            response.setStatus(row[5] != null ? row[5].toString() : null);

            // Map user info
            com.erp.Dto.Response.UserResponse user = new com.erp.Dto.Response.UserResponse();
            user.setId(((Number) row[6]).longValue());
            user.setFirstName((String) row[7]);
            user.setLastName((String) row[8]);
            user.setEmail((String) row[9]);
            user.setPhoneNo(row[10] != null ? ((Number) row[10]).longValue() : 0L);

            response.setUser(user);
            results.add(response);
        }

        // ---------- WRAP INTO ResultDto ----------
        ResultDto<LeaveResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(results);

        log.info("Exit [LeaveCustomRepository] [getFilteredLeaves] with count = {}", totalCount);
        return resultDto;
    }
}
