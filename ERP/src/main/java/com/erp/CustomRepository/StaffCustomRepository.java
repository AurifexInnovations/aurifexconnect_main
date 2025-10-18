package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
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

    public ResultDto<StaffResponse> getFilteredStaff(FilterRequest filterRequest) {
        log.info("Into [StaffCustomRepository] [getFilteredStaff]");

        // ---------- BASE DATA QUERY ----------
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

        // ---------- BASE COUNT QUERY ----------
        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) 
                FROM staff s
                JOIN branch b ON s.branch_branch_id = b.branch_id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ---------- FILTER CONDITIONS ----------
        if (filters != null) {
            if (filters.containsKey("staffId") && !filters.get("staffId").isEmpty()) {
                sql.append(" AND s.id = :staffId");
                countSql.append(" AND s.id = :staffId");
            }
            if (filters.containsKey("staffName") && !filters.get("staffName").isEmpty()) {
                sql.append(" AND LOWER(s.staff_name) = LOWER(:staffName)");
                countSql.append(" AND LOWER(s.staff_name) = LOWER(:staffName)");
            }
            if (filters.containsKey("designation") && !filters.get("designation").isEmpty()) {
                sql.append(" AND s.designation = :designation");
                countSql.append(" AND s.designation = :designation");
            }
            if (filters.containsKey("staffStatus") && !filters.get("staffStatus").isEmpty()) {
                sql.append(" AND s.staff_status = :staffStatus");
                countSql.append(" AND s.staff_status = :staffStatus");
            }
            if (filters.containsKey("branchName") && !filters.get("branchName").isEmpty()) {
                sql.append(" AND LOWER(b.branch_name) = LOWER(:branchName)");
                countSql.append(" AND LOWER(b.branch_name) = LOWER(:branchName)");
            }
            if (filters.containsKey("fromDate") && !filters.get("fromDate").isEmpty() &&
                    filters.containsKey("toDate") && !filters.get("toDate").isEmpty()) {
                sql.append(" AND s.created_at BETWEEN :fromDate AND :toDate");
                countSql.append(" AND s.created_at BETWEEN :fromDate AND :toDate");
            }
        }

        // ---------- SEARCH CONDITIONS ----------
        if (search != null) {
            if (search.containsKey("staffName") && !search.get("staffName").isEmpty()) {
                sql.append(" AND LOWER(s.staff_name) LIKE :staffNameSearch");
                countSql.append(" AND LOWER(s.staff_name) LIKE :staffNameSearch");
            }
            if (search.containsKey("designation") && !search.get("designation").isEmpty()) {
                sql.append(" AND LOWER(s.designation) LIKE :designationSearch");
                countSql.append(" AND LOWER(s.designation) LIKE :designationSearch");
            }
            if (search.containsKey("staffStatus") && !search.get("staffStatus").isEmpty()) {
                sql.append(" AND LOWER(s.staff_status) LIKE :staffStatusSearch");
                countSql.append(" AND LOWER(s.staff_status) LIKE :staffStatusSearch");
            }
            if (search.containsKey("branchName") && !search.get("branchName").isEmpty()) {
                sql.append(" AND LOWER(b.branch_name) LIKE :branchNameSearch");
                countSql.append(" AND LOWER(b.branch_name) LIKE :branchNameSearch");
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
                    case "staffName" -> orderClauses.add("s.staff_name " + direction);
                    case "designation" -> orderClauses.add("s.designation " + direction);
                    case "staffStatus" -> orderClauses.add("s.staff_status " + direction);
                    case "branchName" -> orderClauses.add("b.branch_name " + direction);
                    default -> orderClauses.add("s.id DESC");
                }
            }
            sql.append(String.join(", ", orderClauses));
        } else {
            sql.append(" ORDER BY s.id DESC");
        }

        log.info("[StaffCustomRepository] [getFilteredStaff] :: Query {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- BIND FILTER PARAMETERS ----------
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    try {
                        switch (key) {
                            case "staffId" -> {
                                Long id = Long.parseLong(value);
                                dataQuery.setParameter("staffId", id);
                                countQuery.setParameter("staffId", id);
                            }
                            case "staffName" -> {
                                dataQuery.setParameter("staffName", value.toLowerCase());
                                countQuery.setParameter("staffName", value.toLowerCase());
                            }
                            case "designation" -> {
                                dataQuery.setParameter("designation", value);
                                countQuery.setParameter("designation", value);
                            }
                            case "staffStatus" -> {
                                dataQuery.setParameter("staffStatus", value);
                                countQuery.setParameter("staffStatus", value);
                            }
                            case "branchName" -> {
                                dataQuery.setParameter("branchName", value.toLowerCase());
                                countQuery.setParameter("branchName", value.toLowerCase());
                            }
                            case "fromDate" -> {
                                LocalDateTime from = LocalDateTime.parse(value);
                                dataQuery.setParameter("fromDate", from);
                                countQuery.setParameter("fromDate", from);
                            }
                            case "toDate" -> {
                                LocalDateTime to = LocalDateTime.parse(value);
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
                        case "staffName" -> {
                            dataQuery.setParameter("staffNameSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("staffNameSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "designation" -> {
                            dataQuery.setParameter("designationSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("designationSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "staffStatus" -> {
                            dataQuery.setParameter("staffStatusSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("staffStatusSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "branchName" -> {
                            dataQuery.setParameter("branchNameSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("branchNameSearch", "%" + value.toLowerCase() + "%");
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
        List<StaffResponse> results = new ArrayList<>();
        for (Object[] row : rows) {
            StaffResponse dto = new StaffResponse();
            dto.setId(((Number) row[0]).longValue());
            dto.setStaffName((String) row[1]);
            dto.setEmail((String) row[2]);
            dto.setContactNo((String) row[3]);

            if (row[4] != null) dto.setDesignation(Designation.valueOf(row[4].toString()));
            if (row[5] != null) dto.setStaffStatus(StaffStatus.valueOf(row[5].toString()));

            dto.setBranchName((String) row[6]);
            results.add(dto);
        }

        // ---------- WRAP INTO ResultDto ----------
        ResultDto<StaffResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(results);

        log.info("Exit [StaffCustomRepository] [getFilteredStaff] with count = {}", totalCount);
        return resultDto;
    }
}
