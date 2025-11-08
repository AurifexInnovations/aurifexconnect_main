package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Model.Activity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ActivityCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<Activity> getFilteredActivities(FilterRequest filterRequest) {
        log.info("Into [ActivityCustomRepository] [getFilteredActivities]");

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ✅ Require inventoryId from UI
        if (filters == null || !filters.containsKey("inventoryId") || filters.get("inventoryId").isEmpty()) {
            throw new IllegalArgumentException("inventoryId is required to fetch activities.");
        }

        // ---------- BASE QUERY ----------
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    id,
                    inventory_id,
                    action,
                    quantity,
                    performed_by,
                    timestamp
                FROM activity_logs
                WHERE inventory_id = :inventoryId
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*)
                FROM activity_logs
                WHERE inventory_id = :inventoryId
                """);

        // ---------- FILTER CONDITIONS ----------
        if (filters.containsKey("performedBy") && !filters.get("performedBy").isEmpty()) {
            sql.append(" AND performed_by ILIKE :performedBy");
            countSql.append(" AND performed_by ILIKE :performedBy");
        }
        if (filters.containsKey("startDate") && filters.containsKey("endDate")
                && !filters.get("startDate").isEmpty() && !filters.get("endDate").isEmpty()) {
            sql.append(" AND timestamp BETWEEN :startDate AND :endDate");
            countSql.append(" AND timestamp BETWEEN :startDate AND :endDate");
        }

        // ---------- ORDER BY ----------
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderClauses = new ArrayList<>();
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String column = entry.getKey();
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";
                switch (column) {
                    case "performedBy" -> orderClauses.add("performed_by " + direction);
                    case "timestamp" -> orderClauses.add("timestamp " + direction);
                    default -> orderClauses.add("id " + direction);
                }
            }
            sql.append(String.join(", ", orderClauses));
        } else {
            sql.append(" ORDER BY timestamp DESC");
        }

        log.info("[ActivityCustomRepository] Final Query: {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- BIND PARAMETERS ----------
        bindParameters(filters, dataQuery, countQuery);

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
        List<Activity> results = new ArrayList<>();

        for (Object[] row : rows) {
            Activity activity = new Activity();
            activity.setId(((Number) row[0]).longValue());
            activity.setInventoryId(((Number) row[1]).longValue());
            activity.setAction((String) row[2]);
            activity.setQuantity(row[3] != null ? ((Number) row[3]).doubleValue() : 0);
            activity.setPerformedBy((String) row[4]);
            activity.setTimeStamp(((java.sql.Timestamp) row[5]).toLocalDateTime());
            results.add(activity);
        }

        // ---------- FINAL RESULT ----------
        ResultDto<Activity> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(results);

        log.info("Exit [ActivityCustomRepository] with count = {}", totalCount);
        return resultDto;
    }

    private void bindParameters(Map<String, String> filters,
                                Query dataQuery, Query countQuery) {

        // ✅ inventoryId is mandatory
        long inventoryId = Long.parseLong(filters.get("inventoryId"));
        dataQuery.setParameter("inventoryId", inventoryId);
        countQuery.setParameter("inventoryId", inventoryId);

        if (filters.containsKey("performedBy") && !filters.get("performedBy").isEmpty()) {
            String likeValue = "%" + filters.get("performedBy") + "%";
            dataQuery.setParameter("performedBy", likeValue);
            countQuery.setParameter("performedBy", likeValue);
        }

        if (filters.containsKey("startDate") && filters.containsKey("endDate")
                && !filters.get("startDate").isEmpty() && !filters.get("endDate").isEmpty()) {
            dataQuery.setParameter("startDate", java.sql.Timestamp.valueOf(filters.get("startDate")));
            dataQuery.setParameter("endDate", java.sql.Timestamp.valueOf(filters.get("endDate")));
            countQuery.setParameter("startDate", java.sql.Timestamp.valueOf(filters.get("startDate")));
            countQuery.setParameter("endDate", java.sql.Timestamp.valueOf(filters.get("endDate")));
        }
    }
}