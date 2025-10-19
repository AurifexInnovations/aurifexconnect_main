package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.AttendanceResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.AttendanceStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class AttendanceCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<AttendanceResponse> getFilteredAttendance(FilterRequest filterRequest) {
        log.info("Into [AttendanceCustomRepository] [getFilteredAttendance]");

        // ---------- BASE DATA QUERY ----------
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    a.id,
                    a.date,
                    a.check_in,
                    a.check_out,
                    a.working_hours,
                    a.working_days,
                    u.id AS user_id,
                    CONCAT(u.first_name, ' ', u.last_name) AS user_name,
                    a.status
                FROM attendance a
                JOIN users u ON a.userid = u.id
                WHERE 1=1
                """);

        // ---------- BASE COUNT QUERY ----------
        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) 
                FROM attendance a
                JOIN users u ON a.userid = u.id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ---------- FILTER CONDITIONS ----------
        if (filters != null) {
            if (filters.containsKey("attendanceId") && !filters.get("attendanceId").isEmpty()) {
                sql.append(" AND a.id = :attendanceId");
                countSql.append(" AND a.id = :attendanceId");
            }
            if (filters.containsKey("userId") && !filters.get("userId").isEmpty()) {
                sql.append(" AND u.id = :userId");
                countSql.append(" AND u.id = :userId");
            }
            if (filters.containsKey("status") && !filters.get("status").isEmpty()) {
                sql.append(" AND a.status = :status");
                countSql.append(" AND a.status = :status");
            }
            if (filters.containsKey("fromDate") && !filters.get("fromDate").isEmpty() &&
                    filters.containsKey("toDate") && !filters.get("toDate").isEmpty()) {
                sql.append(" AND a.date BETWEEN :fromDate AND :toDate");
                countSql.append(" AND a.date BETWEEN :fromDate AND :toDate");
            }
        }

        // ---------- SEARCH CONDITIONS ----------
        if (search != null) {
            if (search.containsKey("userName") && !search.get("userName").isEmpty()) {
                sql.append(" AND (LOWER(u.first_name) LIKE :userNameSearch OR LOWER(u.last_name) LIKE :userNameSearch)");
                countSql.append(" AND (LOWER(u.first_name) LIKE :userNameSearch OR LOWER(u.last_name) LIKE :userNameSearch)");
            }
            if (search.containsKey("status") && !search.get("status").isEmpty()) {
                sql.append(" AND LOWER(a.status) LIKE :statusSearch");
                countSql.append(" AND LOWER(a.status) LIKE :statusSearch");
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
                    case "userName" -> orderClauses.add("u.first_name " + direction + ", u.last_name " + direction);
                    case "status" -> orderClauses.add("a.status " + direction);
                    case "date" -> orderClauses.add("a.date " + direction);
                    default -> orderClauses.add("a.id DESC");
                }
            }
            sql.append(String.join(", ", orderClauses));
        } else {
            sql.append(" ORDER BY a.id DESC");
        }

        log.info("[AttendanceCustomRepository] [getFilteredAttendance] :: Query {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- BIND FILTER PARAMETERS ----------
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    try {
                        switch (key) {
                            case "attendanceId" -> {
                                dataQuery.setParameter("attendanceId", Long.parseLong(value));
                                countQuery.setParameter("attendanceId", Long.parseLong(value));
                            }
                            case "userId" -> {
                                dataQuery.setParameter("userId", Long.parseLong(value));
                                countQuery.setParameter("userId", Long.parseLong(value));
                            }
                            case "status" -> {
                                dataQuery.setParameter("status", value);
                                countQuery.setParameter("status", value);
                            }
                            case "fromDate" -> {
                                LocalDate from = LocalDate.parse(value);
                                dataQuery.setParameter("fromDate", from);
                                countQuery.setParameter("fromDate", from);
                            }
                            case "toDate" -> {
                                LocalDate to = LocalDate.parse(value);
                                dataQuery.setParameter("toDate", to);
                                countQuery.setParameter("toDate", to);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Invalid filter parameter [{}] with value [{}]", key, value);
                    }
                }
            });
        }

        // ---------- BIND SEARCH PARAMETERS ----------
        if (search != null) {
            search.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "userName" -> {
                            dataQuery.setParameter("userNameSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("userNameSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "status" -> {
                            dataQuery.setParameter("statusSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("statusSearch", "%" + value.toLowerCase() + "%");
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
        List<AttendanceResponse> results = new ArrayList<>();
        for (Object[] row : rows) {
            AttendanceResponse dto = new AttendanceResponse();
            dto.setId(((Number) row[0]).longValue());
            dto.setDate((LocalDate) row[1]);
            dto.setCheckIn((LocalDateTime) row[2]);
            dto.setCheckOut((LocalDateTime) row[3]);
            dto.setWorkingHours(row[4] != null ? row[4].toString() : null);
            dto.setWorkingDays(row[5] != null ? row[5].toString() : null);
            dto.setUserId(((Number) row[6]).longValue());
            dto.setUserName((String) row[7]);
            if (row[8] != null) dto.setStatus(AttendanceStatus.valueOf(row[8].toString()));
            results.add(dto);
        }

        // ---------- WRAP INTO ResultDto ----------
        ResultDto<AttendanceResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(results);

        log.info("Exit [AttendanceCustomRepository] [getFilteredAttendance] with count = {}", totalCount);
        return resultDto;
    }
}
