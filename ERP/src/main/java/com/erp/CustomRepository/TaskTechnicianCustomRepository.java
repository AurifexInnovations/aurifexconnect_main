package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.TechnicianResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
@Slf4j
public class TaskTechnicianCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<TechnicianResponseDTO> searchTasks(FilterRequest filterRequest) {

        log.info("Into [TaskTechnicianCustomRepository] [searchTasks]");

        // Fixed SQL: reference tser only, not s
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    u.id AS id,
                    t.task_category AS category,
                    CONCAT(u.first_name, ' ', u.last_name) AS name,
                    u.email AS email,
                    u.phone_no AS phone,
                    u.designation AS designation,
                    u.is_active AS status,
                    u.created_at AS createdAt,
                    u.last_modified_at AS updatedAt,
                    ts.service_location AS location,
                    ts.assigned_date AS assignedDate,
                    ts.google_location_link AS googleLocationLink,
                    t.task_name AS taskName,
                    COALESCE(tser.service_name, 'No Service') AS serviceName,
                    COALESCE(tser.service_id, 0) AS serviceId,
                    t.latitude AS latitude,
                    t.longitude AS longitude
                FROM task t
                LEFT JOIN (
                    SELECT DISTINCT ON (task_id) * 
                    FROM task_schedule 
                    ORDER BY task_id, assigned_date
                ) ts ON ts.task_id = t.task_id
                LEFT JOIN LATERAL (
                    SELECT tser.service_id, s.service_name
                    FROM task_services tser
                    LEFT JOIN service s ON s.service_id = tser.service_id
                    WHERE tser.task_id = t.task_id
                    ORDER BY tser.service_id ASC
                    LIMIT 1
                ) tser ON true
                LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
                LEFT JOIN users u ON tt.technician_id = u.id
                WHERE 1=1
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(DISTINCT ROW(u.id, t.task_id))
                FROM task t
                LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
                LEFT JOIN users u ON tt.technician_id = u.id
                LEFT JOIN task_schedule ts ON t.task_id = ts.task_id
                LEFT JOIN task_services tser ON tser.task_id = t.task_id
                LEFT JOIN service s ON s.service_id = tser.service_id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();

        // Apply filters
        if (filters != null) {
            if (filters.containsKey("status") && filters.get("status") != null && !filters.get("status").isEmpty()) {
                sql.append(" AND u.is_active = :status ");
                countSql.append(" AND u.is_active = :status ");
            }
            if (filters.containsKey("category") && filters.get("category") != null && !filters.get("category").isEmpty()) {
                sql.append(" AND t.task_category = :category ");
                countSql.append(" AND t.task_category = :category ");
            }
            if (filters.containsKey("day") && filters.get("day") != null && !filters.get("day").isEmpty()) {
                sql.append(" AND DATE(ts.assigned_date) = CAST(:day AS DATE) ");
                countSql.append(" AND DATE(ts.assigned_date) = CAST(:day AS DATE) ");
            }
            if (filters.containsKey("month") && filters.get("month") != null && !filters.get("month").isEmpty()) {
                sql.append(" AND EXTRACT(MONTH FROM ts.assigned_date) = CAST(:month AS INTEGER) ");
                countSql.append(" AND EXTRACT(MONTH FROM ts.assigned_date) = CAST(:month AS INTEGER) ");
            }
            if (filters.containsKey("taskId") && filters.get("taskId") != null && !filters.get("taskId").isEmpty()) {
                sql.append(" AND t.task_id = :taskId ");
                countSql.append(" AND t.task_id = :taskId ");
            }
            if (filters.containsKey("technicianId") && filters.get("technicianId") != null && !filters.get("technicianId").isEmpty()) {
                sql.append(" AND tt.technician_id = :technicianId ");
                countSql.append(" AND tt.technician_id = :technicianId ");
            }
            if ((filters.containsKey("startDate") && filters.get("startDate") != null && !filters.get("startDate").isEmpty()) ||
                    (filters.containsKey("endDate") && filters.get("endDate") != null && !filters.get("endDate").isEmpty())) {
                sql.append("""
                        AND ts.assigned_date BETWEEN 
                            COALESCE(:startDate, ts.assigned_date) 
                            AND COALESCE(:endDate, ts.assigned_date)
                        """);
                countSql.append("""
                        AND ts.assigned_date BETWEEN 
                            COALESCE(:startDate, ts.assigned_date) 
                            AND COALESCE(:endDate, ts.assigned_date)
                        """);
            }
        }

        sql.append(" GROUP BY u.id, t.task_id, ts.service_location, ts.assigned_date, ts.google_location_link, t.latitude, t.longitude, t.task_name, tser.service_name, tser.service_id ORDER BY t.task_id DESC ");

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        bindParameters(filters, dataQuery, countQuery);

        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long total = ((Number) countQuery.getSingleResult()).longValue();

        List<Object[]> rows = dataQuery.getResultList();
        List<TechnicianResponseDTO> result = new ArrayList<>();

        for (Object[] row : rows) {
            TechnicianResponseDTO resp = new TechnicianResponseDTO();

            resp.setId(getLong(row[0]));
            resp.setCategory(getString(row[1]));
            resp.setName(getString(row[2]));
            resp.setEmail(getString(row[3]));
            resp.setPhone(getString(row[4]));
            resp.setDesignation(getString(row[5]));
            resp.setStatus(getBoolean(row[6]));
            resp.setCreatedAt(getLocalDate(row[7]));
            resp.setUpdatedAt(getLocalDate(row[8]));
            resp.setLocation(getString(row[9]));
            resp.setAssignedDate(getLocalDate(row[10]));
            resp.setGoogleLocationLink(getString(row[11]));
            resp.setTaskName(getString(row[12]));
            resp.setServiceName(getString(row[13]));
            resp.setServiceId(getLong(row[14]));
            resp.setLatitude(getDouble(row[15]));
            resp.setLongitude(getDouble(row[16]));

            result.add(resp);
        }

        ResultDto<TechnicianResponseDTO> dto = new ResultDto<>();
        dto.setResults(result);
        dto.setCount(total);

        return dto;
    }

    private void bindParameters(Map<String, String> filters, Query dataQuery, Query countQuery) {
        if (filters == null) return;

        filters.forEach((key, value) -> {
            if (value == null || value.isEmpty()) return;

            switch (key) {
                case "status" -> setParam(dataQuery, countQuery, "status", Boolean.valueOf(value));
                case "taskId", "technicianId" -> setParam(dataQuery, countQuery, key, Long.parseLong(value));
                case "month" -> setParam(dataQuery, countQuery, key, Integer.valueOf(value));
                case "category" -> setParam(dataQuery, countQuery, key, value);
                case "day", "startDate", "endDate" -> setParam(dataQuery, countQuery, key, LocalDate.parse(value));
            }
        });
    }

    private void setParam(Query dataQuery, Query countQuery, String key, Object val) {
        dataQuery.setParameter(key, val);
        countQuery.setParameter(key, val);
    }

    private String getString(Object o) { return o != null ? o.toString() : null; }
    private Long getLong(Object o) { return o != null ? ((Number) o).longValue() : null; }
    private Double getDouble(Object o) { return o != null ? ((Number) o).doubleValue() : null; }
    private Boolean getBoolean(Object o) { return o != null ? (Boolean) o : null; }
    private LocalDate getLocalDate(Object o) {
        if (o == null) return null;
        if (o instanceof java.sql.Date d) return d.toLocalDate();
        if (o instanceof java.sql.Timestamp ts) return ts.toLocalDateTime().toLocalDate();
        return null;
    }
}
