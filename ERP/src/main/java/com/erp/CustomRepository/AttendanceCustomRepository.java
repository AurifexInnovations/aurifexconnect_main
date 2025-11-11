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
import java.time.YearMonth;
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
                    u.first_name,
                    u.last_name,
                    a.status
                FROM attendance a
                JOIN users u ON a.user_id = u.id
                WHERE 1=1
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) 
                FROM attendance a
                JOIN users u ON a.user_id = u.id
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
            if (filters.containsKey("month") && !filters.get("month").isEmpty()) {
                sql.append(" AND EXTRACT(MONTH FROM a.date) = :month");
                countSql.append(" AND EXTRACT(MONTH FROM a.date) = :month");
            }
            if (filters.containsKey("year") && !filters.get("year").isEmpty()) {
                sql.append(" AND EXTRACT(YEAR FROM a.date) = :year");
                countSql.append(" AND EXTRACT(YEAR FROM a.date) = :year");
            }
            if (filters.containsKey("workingHours") && !filters.get("workingHours").isEmpty()) {
                sql.append(" AND a.working_hours = :workingHours");
                countSql.append(" AND a.working_hours = :workingHours");
            }
        }

        // ---------- SEARCH CONDITIONS ----------
        if (search != null) {
            if (search.containsKey("firstName") && !search.get("firstName").isEmpty()) {
                sql.append(" AND LOWER(u.first_name) LIKE :firstNameSearch");
                countSql.append(" AND LOWER(u.first_name) LIKE :firstNameSearch");
            }
            if (search.containsKey("lastName") && !search.get("lastName").isEmpty()) {
                sql.append(" AND LOWER(u.last_name) LIKE :lastNameSearch");
                countSql.append(" AND LOWER(u.last_name) LIKE :lastNameSearch");
            }
            if (search.containsKey("status") && !search.get("status").isEmpty()) {
                sql.append(" AND LOWER(a.status) LIKE :statusSearch");
                countSql.append(" AND LOWER(a.status) LIKE :statusSearch");
            }
            if (search.containsKey("month") && !search.get("month").isEmpty()) {
                sql.append(" AND EXTRACT(MONTH FROM a.date) = :monthSearch");
                countSql.append(" AND EXTRACT(MONTH FROM a.date) = :monthSearch");
            }
            if (search.containsKey("year") && !search.get("year").isEmpty()) {
                sql.append(" AND EXTRACT(YEAR FROM a.date) = :yearSearch");
                countSql.append(" AND EXTRACT(YEAR FROM a.date) = :yearSearch");
            }
            if (search.containsKey("workingHours") && !search.get("workingHours").isEmpty()) {
                sql.append(" AND a.working_hours = :workingHoursSearch");
                countSql.append(" AND a.working_hours = :workingHoursSearch");
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
                    case "firstName" -> orderClauses.add("u.first_name " + direction);
                    case "lastName" -> orderClauses.add("u.last_name " + direction);
                    case "status" -> orderClauses.add("a.status " + direction);
                    case "date" -> orderClauses.add("a.date " + direction);
                    case "workingHours" -> orderClauses.add("a.working_hours " + direction);
                    default -> orderClauses.add("a.id DESC");
                }
            }
            sql.append(String.join(", ", orderClauses));
        } else {
            sql.append(" ORDER BY a.id DESC");
        }

        log.info("[AttendanceCustomRepository] Query: {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- BIND PARAMETERS ----------
        bindFilterAndSearchParameters(filters, search, dataQuery, countQuery);

        // ---------- PAGINATION ----------
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        // ---------- MAP RESULTS ----------
        List<AttendanceResponse> results = new ArrayList<>();
        for (Object[] row : rows) {
            AttendanceResponse dto = new AttendanceResponse();
            dto.setId(((Number) row[0]).longValue());

            // date
            if (row[1] != null) {
                if (row[1] instanceof java.sql.Date sqlDate) {
                    dto.setDate(sqlDate.toLocalDate());
                } else if (row[1] instanceof LocalDate localDate) {
                    dto.setDate(localDate);
                }
            }

            // checkIn
            if (row[2] != null) {
                if (row[2] instanceof java.sql.Timestamp ts) {
                    dto.setCheckIn(ts.toLocalDateTime());
                } else if (row[2] instanceof LocalDateTime ldt) {
                    dto.setCheckIn(ldt);
                }
            }

            // checkOut
            if (row[3] != null) {
                if (row[3] instanceof java.sql.Timestamp ts) {
                    dto.setCheckOut(ts.toLocalDateTime());
                } else if (row[3] instanceof LocalDateTime ldt) {
                    dto.setCheckOut(ldt);
                }
            }

            dto.setWorkingHours(String.valueOf(row[4] != null ? ((Number) row[4]).doubleValue() : null));
            dto.setWorkingDays(String.valueOf(row[5] != null ? ((Number) row[5]).doubleValue() : null));
            dto.setUserId(((Number) row[6]).longValue());
            //dto.setFirstName((String) row[7]);
            //dto.setLastName((String) row[8]);
            if (row[9] != null) dto.setStatus(AttendanceStatus.valueOf(row[9].toString()));

            results.add(dto);
        }

        ResultDto<AttendanceResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(results);

        log.info("Exit [AttendanceCustomRepository] with count = {}", totalCount);
        return resultDto;
    }

    private void bindFilterAndSearchParameters(Map<String, String> filters, Map<String, String> search,
                                               Query dataQuery, Query countQuery) {
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "attendanceId", "userId" -> {
                            dataQuery.setParameter(key, Long.parseLong(value));
                            countQuery.setParameter(key, Long.parseLong(value));
                        }
                        case "status" -> {
                            dataQuery.setParameter(key, value);
                            countQuery.setParameter(key, value);
                        }
                        case "month", "year" -> {
                            dataQuery.setParameter(key, Integer.parseInt(value));
                            countQuery.setParameter(key, Integer.parseInt(value));
                        }
                        case "workingHours" -> {
                            dataQuery.setParameter(key, Double.parseDouble(value));
                            countQuery.setParameter(key, Double.parseDouble(value));
                        }
                    }
                }
            });
        }

        if (search != null) {
            search.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "firstName" -> {
                            dataQuery.setParameter("firstNameSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("firstNameSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "lastName" -> {
                            dataQuery.setParameter("lastNameSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("lastNameSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "status" -> {
                            dataQuery.setParameter("statusSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("statusSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "month" -> {
                            dataQuery.setParameter("monthSearch", Integer.parseInt(value));
                            countQuery.setParameter("monthSearch", Integer.parseInt(value));
                        }
                        case "year" -> {
                            dataQuery.setParameter("yearSearch", Integer.parseInt(value));
                            countQuery.setParameter("yearSearch", Integer.parseInt(value));
                        }
                        case "workingHours" -> {
                            dataQuery.setParameter("workingHoursSearch", Double.parseDouble(value));
                            countQuery.setParameter("workingHoursSearch", Double.parseDouble(value));
                        }
                    }
                }
            });
        }
    }
}
