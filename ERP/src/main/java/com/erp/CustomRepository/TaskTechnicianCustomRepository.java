package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.MaterialDtoResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.TechnicianResponseDTO;
import com.erp.Enum.TaskStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@Slf4j
public class TaskTechnicianCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<TechnicianResponseDTO> searchTasks(FilterRequest filterRequest) {

        log.info("Into [TaskTechnicianCustomRepository] [searchTasks]");

        // SELECT QUERY (unchanged except material fields already included)
        StringBuilder sql = new StringBuilder("""
            SELECT 
                u.id AS technicianId,
                t.task_category AS category,
                CONCAT(u.first_name, ' ', u.last_name) AS technicianName,
                u.email AS technicianEmail,
                u.phone_no AS technicianPhone,
                u.designation AS designation,
                t.status AS status,
                u.created_at AS createdAt,
                u.last_modified_at AS updatedAt,
                ts.service_location AS location,
                ts.assigned_date AS assignedDate,
                ts.assigned_time AS assignedTime,
                ts.google_location_link AS googleLocationLink,
                t.task_name AS taskName,

                c.customer_name AS customerName,
                CONCAT(
                    COALESCE(c.address_line_1, ''), ' ',
                    COALESCE(c.address_line_2, ''), ' ',
                    COALESCE(c.city, ''), ' ',
                    COALESCE(c.state, ''), ' ',
                    COALESCE(c.pincode, '')
                ) AS customerAddress,

                COALESCE(tser.service_name, 'No Service') AS serviceName,
                COALESCE(tser.service_id, 0) AS serviceId,
                t.latitude AS latitude,
                t.longitude AS longitude,
                c.phone AS customerPhone,
                t.task_id AS taskId,

                COALESCE(tm.material_id, 0) AS materialId,
                COALESCE(i.item_name, 'No Material') AS materialName,
                COALESCE(tm.quantity, 0) AS materialQuantity,
                COALESCE(tm.unit, '') AS materialUnit

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
            LEFT JOIN task_material tm ON tm.task_id = t.task_id
            LEFT JOIN inventoryv2 i ON i.item_id = tm.material_id

            WHERE 1=1
        """);

        // COUNT QUERY (unchanged)
        StringBuilder countSql = new StringBuilder("""
            SELECT COUNT(DISTINCT t.task_id)
            FROM task t
            LEFT JOIN customer c ON c.id = t.customer_id
            LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
            LEFT JOIN users u ON tt.technician_id = u.id
            LEFT JOIN task_schedule ts ON t.task_id = ts.task_id
            WHERE 1=1
        """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        bindFilterConditions(filters, sql, countSql);

        sql.append("""
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

        // ========================
        // GROUPING LOGIC HERE
        // ========================
        Map<Long, TechnicianResponseDTO> taskMap = new LinkedHashMap<>();

        for (Object[] row : rows) {

            Long taskId = getLong(row[21]);

            TechnicianResponseDTO r = taskMap.get(taskId);
            if (r == null) {

                r = new TechnicianResponseDTO();
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
                r.setAssignedTime(getLocalTime(row[11]));
                r.setGoogleLocationLink(getString(row[12]));
                r.setTaskName(getString(row[13]));
                r.setCustomerName(getString(row[14]));
                r.setCustomerAddress(getString(row[15]));
                r.setServiceName(getString(row[16]));
                r.setServiceId(getLong(row[17]));
                r.setLatitude(getDouble(row[18]));
                r.setLongitude(getDouble(row[19]));
                r.setCustomerPhone(getString(row[20]));
                r.setTaskId(taskId);

                r.setMaterials(new ArrayList<>());
                taskMap.put(taskId, r);
            }

            Long materialId = getLong(row[22]);
            if (materialId != null && materialId > 0) {
                MaterialDtoResponse material = new MaterialDtoResponse();
                material.setMaterialId(materialId);
                material.setMaterialName(getString(row[23]));
                material.setMaterialQuantity(getDouble(row[24]));
                material.setMaterialUnit(getString(row[25]));
                r.getMaterials().add(material);
            }
        }

        ResultDto<TechnicianResponseDTO> dto = new ResultDto<>();
        dto.setResults(new ArrayList<>(taskMap.values()));
        dto.setCount(total);

        return dto;
    }

    private void bindFilterConditions(Map<String, String> filters, StringBuilder sql, StringBuilder countSql) {
        if (filters.containsKey("technicianId")) {
            sql.append(" AND u.id = :technicianId ");
            countSql.append(" AND u.id = :technicianId ");
        }
        if(filters.containsKey("customerId")){
            sql.append(" AND c.id = :customerId");
            countSql.append(" AND c.id = :customerId");
        }
        if(filters.containsKey("date")){
            sql.append(" AND ts.assigned_date = :date");
            countSql.append(" AND ts.assigned_date = :date");
        }
    }

    private boolean notEmpty(String v) {
        return v != null && !v.trim().isEmpty();
    }

    private void bindParameters(Map<String, String> filters, Query dataQuery, Query countQuery) {
        if (filters.containsKey("technicianId")) {
            Long technicianId = Long.valueOf(filters.get("technicianId"));
            dataQuery.setParameter("technicianId", technicianId);
            countQuery.setParameter("technicianId", technicianId);
        }

        if (filters.containsKey("customerId")) {
            Long customerId = Long.valueOf(filters.get("customerId"));
            dataQuery.setParameter("customerId", customerId);
            countQuery.setParameter("customerId", customerId);
        }

        if(filters.containsKey("date")){
            LocalDate date = LocalDate.parse(filters.get("date"));
            dataQuery.setParameter("date", date);
            countQuery.setParameter("date", date);
        }
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

    private String getLocalTime(Object o) {
        if (o == null) return null;

        LocalTime time;
        if (o instanceof java.sql.Time t) {
            time = t.toLocalTime();
        } else if (o instanceof java.sql.Timestamp ts) {
            time = ts.toLocalDateTime().toLocalTime();
        } else if (o instanceof LocalTime lt) {
            time = lt;
        } else {
            return null;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

        return time.format(formatter);
    }

    public ResultDto<TechnicianResponseDTO> searchTasks() {
        log.info("Into [TaskTechnicianCustomRepository] [searchTasks]");

        // SELECT QUERY (unchanged except material fields already included)
        StringBuilder sql = new StringBuilder("""
            SELECT 
                u.id AS technicianId,
                t.task_category AS category,
                CONCAT(u.first_name, ' ', u.last_name) AS technicianName,
                u.email AS technicianEmail,
                u.phone_no AS technicianPhone,
                u.designation AS designation,
                t.status AS status,
                u.created_at AS createdAt,
                u.last_modified_at AS updatedAt,
                ts.service_location AS location,
                ts.assigned_date AS assignedDate,
                ts.google_location_link AS googleLocationLink,
                t.task_name AS taskName,

                c.customer_name AS customerName,
                CONCAT(
                    COALESCE(c.address_line_1, ''), ' ',
                    COALESCE(c.address_line_2, ''), ' ',
                    COALESCE(c.city, ''), ' ',
                    COALESCE(c.state, ''), ' ',
                    COALESCE(c.pincode, '')
                ) AS customerAddress,

                COALESCE(tser.service_name, 'No Service') AS serviceName,
                COALESCE(tser.service_id, 0) AS serviceId,
                t.latitude AS latitude,
                t.longitude AS longitude,
                c.phone AS customerPhone,
                t.task_id AS taskId,

                COALESCE(tm.material_id, 0) AS materialId,
                COALESCE(i.item_name, 'No Material') AS materialName,
                COALESCE(tm.quantity, 0) AS materialQuantity,
                COALESCE(tm.unit, '') AS materialUnit

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
            LEFT JOIN task_material tm ON tm.task_id = t.task_id
            LEFT JOIN inventoryv2 i ON i.item_id = tm.material_id

            WHERE 1=1
        """);

        // COUNT QUERY (unchanged)
        StringBuilder countSql = new StringBuilder("""
            SELECT COUNT(DISTINCT t.task_id)
            FROM task t
            LEFT JOIN customer c ON c.id = t.customer_id
            LEFT JOIN task_technicians tt ON t.task_id = tt.task_id
            LEFT JOIN users u ON tt.technician_id = u.id
            LEFT JOIN task_schedule ts ON t.task_id = ts.task_id
            WHERE 1=1
        """);

        sql.append("""
            ORDER BY t.task_id DESC
        """);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        long total = ((Number) countQuery.getSingleResult()).longValue();

        List<Object[]> rows = dataQuery.getResultList();

        // ========================
        // GROUPING LOGIC HERE
        // ========================
        Map<Long, TechnicianResponseDTO> taskMap = new LinkedHashMap<>();

        for (Object[] row : rows) {

            Long taskId = getLong(row[20]);

            TechnicianResponseDTO r = taskMap.get(taskId);
            if (r == null) {

                r = new TechnicianResponseDTO();
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
                r.setCustomerName(getString(row[13]));
                r.setCustomerAddress(getString(row[14]));
                r.setServiceName(getString(row[15]));
                r.setServiceId(getLong(row[16]));
                r.setLatitude(getDouble(row[17]));
                r.setLongitude(getDouble(row[18]));
                r.setCustomerPhone(getString(row[19]));
                r.setTaskId(taskId);

                r.setMaterials(new ArrayList<>());
                taskMap.put(taskId, r);
            }

            Long materialId = getLong(row[21]);
            if (materialId != null && materialId > 0) {
                MaterialDtoResponse material = new MaterialDtoResponse();
                material.setMaterialId(materialId);
                material.setMaterialName(getString(row[22]));
                material.setMaterialQuantity(getDouble(row[23]));
                material.setMaterialUnit(getString(row[24]));
                r.getMaterials().add(material);
            }
        }

        ResultDto<TechnicianResponseDTO> dto = new ResultDto<>();
        dto.setResults(new ArrayList<>(taskMap.values()));
        dto.setCount(total);

        return dto;
    }
}
