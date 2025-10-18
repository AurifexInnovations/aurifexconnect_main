package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.LeaveResponse;
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
            "endDate", "endDate"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "userIdSearch", "userId",
            "userNameSearch", "userName",
            "leaveTypeSearch", "leaveType"
    );

    @PersistenceContext
    private EntityManager entityManager;

    public List<LeaveResponse> getFilteredLeaves(FilterRequest filterRequest) {
        log.info("Into [LeaveCustomRepository] [getFilteredLeaves]");

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

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // 🔹 Exact Filters
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("leaveType"))
                sql.append(" AND l.leave_type = :leaveType");
            if (filters.containsKey("status"))
                sql.append(" AND l.status = :status");
            if (filters.containsKey("leaveBalance"))
                sql.append(" AND l.leave_balance = :leaveBalance");

            // Date range filters
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                sql.append(" AND l.start_date BETWEEN :startDate AND :endDate");
            } else if (filters.containsKey("startDate")) {
                sql.append(" AND l.start_date >= :startDate");
            } else if (filters.containsKey("endDate")) {
                sql.append(" AND l.end_date <= :endDate");
            }
        }

        // 🔹 Search Filters (LIKE or exact)
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("userName"))
                sql.append(" AND LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(CONCAT('%', :userNameSearch, '%'))");
            if (search.containsKey("userId"))
                sql.append(" AND u.id = :userIdSearch");
            if (search.containsKey("leaveType"))
                sql.append(" AND l.leave_type = :leaveTypeSearch");
        }

        // 🔹 ORDER BY
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

        Query query = entityManager.createNativeQuery(sql.toString());

        // 🔹 Bind Filter Parameters
        if (filters != null) {
            filterParamMap.forEach((paramName, mapKey) -> {
                String value = filters.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    switch (paramName) {
                        case "leaveBalance" -> query.setParameter(paramName, Integer.parseInt(value));
                        case "startDate", "endDate" -> query.setParameter(paramName, Date.valueOf(value));
                        default -> query.setParameter(paramName, value);
                    }
                }
            });
        }

        // 🔹 Bind Search Parameters
        if (search != null) {
            searchParamMap.forEach((paramName, mapKey) -> {
                String value = search.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    if (paramName.equals("userIdSearch"))
                        query.setParameter(paramName, Long.parseLong(value));
                    else
                        query.setParameter(paramName, value);
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

        // 🔹 Map results to LeaveResponse manually
        List<Object[]> rows = query.getResultList();
        List<LeaveResponse> results = new ArrayList<>();

        for (Object[] row : rows) {
            LeaveResponse response = new LeaveResponse();
            response.setId(((Number) row[0]).longValue());
            response.setStartDate(((Date) row[1]).toLocalDate());
            response.setEndDate(((Date) row[2]).toLocalDate());
            response.setLeaveType(row[3] != null ? Enum.valueOf(com.erp.Enum.LeaveType.class, row[3].toString()) : null);
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

        log.info("Exit [LeaveCustomRepository] [getFilteredLeaves]");
        return results;
    }
}
