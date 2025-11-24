package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.TechnicianResponseDTO;
import com.erp.Enum.TaskStatus;
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

        // ----------------- SELECT QUERY -----------------
        StringBuilder sql = new StringBuilder("""
            SELECT 
                u.id AS technicianId,                         -- 0
                t.task_category AS category,                  -- 1
                CONCAT(u.first_name, ' ', u.last_name) AS technicianName,  -- 2
                u.email AS technicianEmail,                   -- 3
                u.phone_no AS technicianPhone,                -- 4
                u.designation AS designation,                 -- 5
                t.status AS status,                           -- 6
                u.created_at AS createdAt,                    -- 7
                u.last_modified_at AS updatedAt,              -- 8
                ts.service_location AS location,              -- 9
                ts.assigned_date AS assignedDate,             -- 10
                ts.google_location_link AS googleLocationLink,-- 11
                t.task_name AS taskName,                      -- 12

                -- CUSTOMER DETAILS (ONLY NAME & ADDRESS)
                c.customer_name AS customerName,              -- 13
              
                CONCAT(
                    COALESCE(c.address_line_1, ''), ' ',
                    COALESCE(c.address_line_2, ''), ' ',
                    COALESCE(c.city, ''), ' ',
                    COALESCE(c.state, ''), ' ',
                    COALESCE(c.pincode, '')
                ) AS customerAddress,                        -- 14

                COALESCE(tser.service_name, 'No Service') AS serviceName,  -- 15
                COALESCE(tser.service_id, 0) AS serviceId,                 -- 16
                t.latitude AS latitude,                      -- 17
                t.longitude AS longitude ,                    -- 18
                 c.phone AS customerPhone ,
                 t.task_id as taskId 
                

            FROM task t
            LEFT JOIN customer c ON c.id = t.customer_id

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
            ) tser ON TRUE

            LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
            LEFT JOIN users u ON tt.technician_id = u.id

            WHERE 1=1
        """);

        // ----------------- COUNT QUERY -----------------
        StringBuilder countSql = new StringBuilder("""
            SELECT COUNT(DISTINCT ROW(u.id, t.task_id))
            FROM task t
            LEFT JOIN customer c ON c.id = t.customer_id
            LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
            LEFT JOIN users u ON tt.technician_id = u.id
            LEFT JOIN task_schedule ts ON t.task_id = ts.task_id
            WHERE 1=1
        """);

        Map<String, String> filters = filterRequest.getFilterColumns();

        if (filters != null) {

            if (filters.containsKey("status") && notEmpty(filters.get("status"))) {
                sql.append(" AND t.status = :status ");
                countSql.append(" AND t.status = :status ");
            }

            if (filters.containsKey("category") && notEmpty(filters.get("category"))) {
                sql.append(" AND t.task_category = :category ");
                countSql.append(" AND t.task_category = :category ");
            }

            if (filters.containsKey("day") && notEmpty(filters.get("day"))) {
                sql.append(" AND DATE(ts.assigned_date) = CAST(:day AS DATE) ");
                countSql.append(" AND DATE(ts.assigned_date) = CAST(:day AS DATE) ");
            }

            if (filters.containsKey("month") && notEmpty(filters.get("month"))) {
                sql.append(" AND EXTRACT(MONTH FROM ts.assigned_date) = :month ");
                countSql.append(" AND EXTRACT(MONTH FROM ts.assigned_date) = :month ");
            }

            if (filters.containsKey("taskId") && notEmpty(filters.get("taskId"))) {
                sql.append(" AND t.task_id = :taskId ");
                countSql.append(" AND t.task_id = :taskId ");
            }

            if (filters.containsKey("technicianId") && notEmpty(filters.get("technicianId"))) {
                sql.append(" AND tt.technician_id = :technicianId ");
                countSql.append(" AND tt.technician_id = :technicianId ");
            }

            if ((filters.containsKey("startDate") && notEmpty(filters.get("startDate")))
                    || (filters.containsKey("endDate") && notEmpty(filters.get("endDate")))) {

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

        sql.append("""
            GROUP BY u.id, t.task_id, ts.service_location, ts.assigned_date,
            ts.google_location_link, t.latitude, t.longitude, t.task_name,
            c.customer_name, c.address_line_1, c.address_line_2,
            c.city, c.state, c.pincode, tser.service_name, tser.service_id,c.phone,t.task_id
            ORDER BY t.task_id DESC
        """);

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
        List<TechnicianResponseDTO> results = new ArrayList<>();

        for (Object[] row : rows) {
            TechnicianResponseDTO r = new TechnicianResponseDTO();

            r.setId(getLong(row[0]));
            r.setCategory(getString(row[1]));
            r.setName(getString(row[2]));
            r.setEmail(getString(row[3]));
            r.setPhone(getString(row[4]));
            r.setDesignation(getString(row[5]));
            r.setStatus(getEnum(row[6]));
            r.setCreatedAt(getLocalDate(row[7]));
            r.setUpdatedAt(getLocalDate(row[8]));
            r.setLocation(getString(row[9]));
            r.setAssignedDate(getLocalDate(row[10]));
            r.setGoogleLocationLink(getString(row[11]));
            r.setTaskName(getString(row[12]));

            // CUSTOMER DETAILS
            r.setCustomerName(getString(row[13]));
            r.setCustomerAddress(getString(row[14]));

            r.setServiceName(getString(row[15]));
            r.setServiceId(getLong(row[16]));
            r.setLatitude(getDouble(row[17]));
            r.setLongitude(getDouble(row[18]));
            r.setCustomerPhone(getString(row[19]));
            r.setTaskId(getLong(row[20]));

            results.add(r);
        }

        ResultDto<TechnicianResponseDTO> dto = new ResultDto<>();
        dto.setResults(results);
        dto.setCount(total);

        return dto;
    }

    private boolean notEmpty(String v) {
        return v != null && !v.trim().isEmpty();
    }

    private void bindParameters(Map<String, String> filters, Query dataQuery, Query countQuery) {
        if (filters == null) return;

        filters.forEach((key, value) -> {
            if (!notEmpty(value)) return;

            switch (key) {
                case "status" ->
                        setParam(dataQuery, countQuery, "status", TaskStatus.valueOf(value));

                case "taskId", "technicianId" ->
                        setParam(dataQuery, countQuery, key, Long.valueOf(value));

                case "month" ->
                        setParam(dataQuery, countQuery, key, Integer.valueOf(value));

                case "category" ->
                        setParam(dataQuery, countQuery, key, value);

                case "day", "startDate", "endDate" ->
                        setParam(dataQuery, countQuery, key, LocalDate.parse(value));
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

    private TaskStatus getEnum(Object o) {
        return o != null ? TaskStatus.valueOf(o.toString()) : null;
    }

    private LocalDate getLocalDate(Object o) {
        if (o == null) return null;
        if (o instanceof java.sql.Date d) return d.toLocalDate();
        if (o instanceof java.sql.Timestamp ts) return ts.toLocalDateTime().toLocalDate();
        return null;
    }
}
