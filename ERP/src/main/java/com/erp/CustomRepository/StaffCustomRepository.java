package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.StaffResponse;
import com.erp.Enum.Designation;
import com.erp.Enum.StaffStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class StaffCustomRepository {

    private final Map<String, String> filterParamMap = Map.of(
            "staffId", "id",
            "staffName", "staffName",
            "designation", "designation",
            "staffStatus", "staffStatus",
            "branchName", "branchName",
            "fromDate", "fromDate",
            "toDate", "toDate"
    );

    private final Map<String, String> searchParamMap = Map.of(
            "staffNameSearch", "staffName",
            "designationSearch", "designation",
            "staffStatusSearch", "staffStatus",
            "branchNameSearch", "branchName"
    );

    @PersistenceContext
    private EntityManager entityManager;

    public List<StaffResponse> getFilteredStaff(FilterRequest filterRequest) {
        log.info("Into [StaffCustomRepository] [getFilteredStaff]");

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    s.id,
                    s.staff_name,
                    s.email,
                    s.contact_no,
                    s.designation,
                    s.staff_status,
                    b.branch_name
                FROM staff s
                JOIN branch b ON s.branch_branch_id = b.branch_id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // 🔹 Exact filters
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("staffId"))
                sql.append(" AND s.id = :staffId");
            if (filters.containsKey("staffName"))
                sql.append(" AND s.staff_name = :staffName");
            if (filters.containsKey("designation"))
                sql.append(" AND s.designation = :designation");
            if (filters.containsKey("staffStatus"))
                sql.append(" AND s.staff_status = :staffStatus");
            if (filters.containsKey("branchName"))
                sql.append(" AND LOWER(b.branch_name) = LOWER(:branchName)");
        }

        // 🔹 Date range filter (BETWEEN)
        if (filters != null && filters.containsKey("fromDate") && filters.containsKey("toDate")) {
            sql.append(" AND s.created_at BETWEEN :fromDate AND :toDate");
        }

        // 🔹 Search (LIKE)
        if (search != null && !search.isEmpty()) {
            if (search.containsKey("staffName"))
                sql.append(" AND LOWER(s.staff_name) LIKE LOWER(CONCAT('%', :staffNameSearch, '%'))");
            if (search.containsKey("designation"))
                sql.append(" AND LOWER(s.designation) LIKE LOWER(CONCAT('%', :designationSearch, '%'))");
            if (search.containsKey("staffStatus"))
                sql.append(" AND LOWER(s.staff_status) LIKE LOWER(CONCAT('%', :staffStatusSearch, '%'))");
            if (search.containsKey("branchName"))
                sql.append(" AND LOWER(b.branch_name) LIKE LOWER(CONCAT('%', :branchNameSearch, '%'))");
        }

        // 🔹 ORDER BY
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderClauses = new ArrayList<>();
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String column = entry.getKey();
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";
                switch (column) {
                    case "staffName" -> orderClauses.add("s.staff_name " + direction);
                    case "designation" -> orderClauses.add("s.designation " + direction);
                    case "staffStatus" -> orderClauses.add("s.staff_status " + direction);
                    case "branchName" -> orderClauses.add("b.branch_name " + direction);
                }
            }
            if (!orderClauses.isEmpty()) {
                sql.append(String.join(", ", orderClauses));
            }
        }

        log.info("[StaffCustomRepository] [getFilteredStaff] :: Query {}", sql);

        Query query = entityManager.createNativeQuery(sql.toString());

        // 🔹 Bind exact filters
        if (filters != null) {
            filterParamMap.forEach((paramName, mapKey) -> {
                String value = filters.get(mapKey);
                if (value != null && !value.isEmpty()) {
                    try {
                        switch (paramName) {
                            case "staffId" -> query.setParameter(paramName, Long.parseLong(value));
                            case "fromDate", "toDate" -> {
                                LocalDateTime dateValue = LocalDateTime.parse(value);
                                query.setParameter(paramName, dateValue);
                            }
                            default -> query.setParameter(paramName, value);
                        }
                    } catch (Exception e) {
                        log.warn("Invalid parameter [{}] with value [{}]", paramName, value);
                    }
                }
            });
        }

        // 🔹 Bind search parameters
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
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(page * size);
            query.setMaxResults(size);
        }

        // 🔹 Execute & map results manually to DTO
        List<Object[]> results = query.getResultList();
        List<StaffResponse> responseList = new ArrayList<>();

        for (Object[] row : results) {
            StaffResponse dto = new StaffResponse();
            dto.setId(((Number) row[0]).longValue());
            dto.setStaffName((String) row[1]);
            dto.setEmail((String) row[2]);
            dto.setContactNo((String) row[3]);

            // handle enum safely
            if (row[4] != null)
                dto.setDesignation(Designation.valueOf(row[4].toString()));
            if (row[5] != null)
                dto.setStaffStatus(StaffStatus.valueOf(row[5].toString()));

            dto.setBranchName((String) row[6]);
            responseList.add(dto);
        }

        log.info("Exit [StaffCustomRepository] [getFilteredStaff]");
        return responseList;
    }
}
