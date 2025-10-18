package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class BranchCustomRepository {
    private final Map<String, String> filterParamMap = Map.of(
            "branchId", "branchId",
            "branchName", "branchName",
            "location", "location",
            "branchType", "branchType",
            "branchStatus", "branchStatus"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "branchNameSearch", "branchName",
            "locationSearch", "location",
            "contactSearch", "contactInfo",
            "statusSearch", "branchStatus"
    );
    @PersistenceContext
    private EntityManager entityManager;

    public List getBranchDetails(FilterRequest filterRequest) {
        log.info("Into [BranchCustomRepository] [getBranchDetails]");

        StringBuilder sql = new StringBuilder("""
                    SELECT 
                      b.branch_name AS branchName,
                      b.contact_info AS contactInfo,
                      b.phone_number AS phoneNumber,
                      b.branch_status AS branchStatus,
                      b.branch_type AS branchType,
                      b.edited_by AS editedBy,
                      b.pincode AS pincode,
                      b.city AS city,
                      b.state AS state,
                      b.location AS location,
                      b.created_at AS createdAt
                    FROM branch b
                    WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // 🔹 Exact filters (optional)
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("branchId"))
                sql.append(" AND b.branch_id = :branchId");
            if (filters.containsKey("branchName"))
                sql.append(" AND b.branch_name = :branchName");
            if (filters.containsKey("location"))
                sql.append(" AND b.location = :location");
            if (filters.containsKey("branchType"))
                sql.append(" AND b.branch_type = :branchType");
            if (filters.containsKey("branchStatus"))
                sql.append(" AND b.branch_status = :branchStatus");

            // 🔹 Date range filter
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                sql.append(" AND b.created_at BETWEEN :startDate AND :endDate");
            } else if (filters.containsKey("startDate")) {
                sql.append(" AND b.created_at >= :startDate");
            } else if (filters.containsKey("endDate")) {
                sql.append(" AND b.created_at <= :endDate");
            }
        }

        // 🔹 LIKE filters (optional — user can pass one or many)
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("branchName"))
                sql.append(" AND LOWER(b.branch_name) LIKE LOWER(CONCAT('%', :branchNameSearch, '%'))");
            if (search.containsKey("location"))
                sql.append(" AND LOWER(b.location) LIKE LOWER(CONCAT('%', :locationSearch, '%'))");
            if (search.containsKey("contactInfo"))
                sql.append(" AND LOWER(b.contact_info) LIKE LOWER(CONCAT('%', :contactSearch, '%'))");
            if (search.containsKey("branchStatus"))
                sql.append(" AND LOWER(b.branch_status) LIKE LOWER(CONCAT('%', :statusSearch, '%'))");
        }

        // 🔹 ORDER BY dynamically (default: newest first)
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderClauses = new ArrayList<>();
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String column = entry.getKey();
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";

                switch (column) {
                    case "createdAt" -> orderClauses.add("b.created_at " + direction);
                    case "branchName" -> orderClauses.add("b.branch_name " + direction);
                    case "location" -> orderClauses.add("b.location " + direction);
                }
            }
            if (!orderClauses.isEmpty()) {
                sql.append(String.join(", ", orderClauses));
            }
        }


        log.info("[BranchCustomRepository] [getBranchDetails] :: Query {} " , sql.toString());
        // 🔹 Create query
        Query query = entityManager.createNativeQuery(sql.toString());

        // Bind exact filters dynamically
        if (filters != null) {
            filterParamMap.forEach((paramName, mapKey) -> {
                String value = filters.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    if (paramName.equals("branchId")) {
                        query.setParameter(paramName, Long.parseLong(value));
                    } else {
                        query.setParameter(paramName, value);
                    }
                }
            });
        }

        // Bind search (LIKE) dynamically
        if (search != null) {
            searchParamMap.forEach((paramName, mapKey) -> {
                String value = search.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    query.setParameter(paramName, value);
                }
            });
        }

        // 🔹 Pagination
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageSize();
            int size = filterRequest.getPaginationRequest().getPageNumber();
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }

        log.info("Exit [BranchCustomRepository] [getBranchDetails]");

        return query.getResultList();
    }
}
