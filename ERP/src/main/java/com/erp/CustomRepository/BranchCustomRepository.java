package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.BranchResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.BranchStatus;
import com.erp.Enum.BranchType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class BranchCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<BranchResponse> getBranchDetails(FilterRequest filterRequest) {
        log.info("Into [BranchCustomRepository] [getBranchDetails]");

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    b.branch_id AS branchId,
                    b.branch_name AS branchName,
                    b.location AS location,
                    b.contact_info AS contactInfo,
                    b.phone_number AS phoneNumber,
                    b.created_at AS createdAt,
                    b.branch_status AS branchStatus,
                    b.branch_type AS branchType,
                    b.edited_by AS editedBy,
                    b.pincode AS pincode,
                    b.city AS city,
                    b.state AS state
                FROM branch b
                WHERE 1=1
        """);

        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM branch b WHERE 1=1");

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ----- FILTER CONDITIONS -----
        if (filters != null) {
            if (filters.containsKey("branchId")) {
                sql.append(" AND b.branch_id = :branchId");
                countSql.append(" AND b.branch_id = :branchId");
            }
            if (filters.containsKey("branchName")) {
                sql.append(" AND b.branch_name = :branchName");
                countSql.append(" AND b.branch_name = :branchName");
            }
            if (filters.containsKey("location")) {
                sql.append(" AND b.location = :location");
                countSql.append(" AND b.location = :location");
            }
            if (filters.containsKey("branchType")) {
                sql.append(" AND b.branch_type = :branchType");
                countSql.append(" AND b.branch_type = :branchType");
            }
            if (filters.containsKey("branchStatus")) {
                sql.append(" AND b.branch_status = :branchStatus");
                countSql.append(" AND b.branch_status = :branchStatus");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                sql.append(" AND b.created_at BETWEEN :startDate AND :endDate");
                countSql.append(" AND b.created_at BETWEEN :startDate AND :endDate");
            } else if (filters.containsKey("startDate")) {
                sql.append(" AND b.created_at >= :startDate");
                countSql.append(" AND b.created_at >= :startDate");
            } else if (filters.containsKey("endDate")) {
                sql.append(" AND b.created_at <= :endDate");
                countSql.append(" AND b.created_at <= :endDate");
            }
        }

        // ----- SEARCH CONDITIONS -----
        if (search != null) {
            if (search.containsKey("branchName")) {
                sql.append(" AND b.branch_name ILIKE '%' || :branchNameSearch || '%'");
                countSql.append(" AND b.branch_name ILIKE '%' || :branchNameSearch || '%'");
            }
            if (search.containsKey("location")) {
                sql.append(" AND b.location ILIKE '%' || :locationSearch || '%'");
                countSql.append(" AND b.location ILIKE '%' || :locationSearch || '%'");
            }
            if (search.containsKey("contactInfo")) {
                sql.append(" AND b.contact_info ILIKE '%' || :contactSearch || '%'");
                countSql.append(" AND b.contact_info ILIKE '%' || :contactSearch || '%'");
            }
            if (search.containsKey("branchStatus")) {
                sql.append(" AND b.branch_status ILIKE '%' || :statusSearch || '%'");
                countSql.append(" AND b.branch_status ILIKE '%' || :statusSearch || '%'");
            }
        }

        // ----- ORDER BY -----
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderClauses = new ArrayList<>();
            orderBy.forEach((column, direction) -> {
                String dir = direction.equalsIgnoreCase("desc") ? "DESC" : "ASC";
                switch (column) {
                    case "createdAt" -> orderClauses.add("b.created_at " + dir);
                    case "branchName" -> orderClauses.add("b.branch_name " + dir);
                    case "location" -> orderClauses.add("b.location " + dir);
                    case "branchId" -> orderClauses.add("b.branch_id " + dir);
                }
            });
            if (!orderClauses.isEmpty()) {
                sql.append(String.join(", ", orderClauses));
            }
        }

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ----- SET FILTER PARAMETERS -----
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "branchId" -> {
                            dataQuery.setParameter("branchId", Long.parseLong(value));
                            countQuery.setParameter("branchId", Long.parseLong(value));
                        }
                        case "branchName" -> {
                            dataQuery.setParameter("branchName", value);
                            countQuery.setParameter("branchName", value);
                        }
                        case "location" -> {
                            dataQuery.setParameter("location", value);
                            countQuery.setParameter("location", value);
                        }
                        case "branchType" -> {
                            dataQuery.setParameter("branchType", value);
                            countQuery.setParameter("branchType", value);
                        }
                        case "branchStatus" -> {
                            dataQuery.setParameter("branchStatus", value);
                            countQuery.setParameter("branchStatus", value);
                        }
                        case "startDate", "endDate" -> {
                            dataQuery.setParameter(key, value);
                            countQuery.setParameter(key, value);
                        }
                    }
                }
            });
        }

        // ----- SET SEARCH PARAMETERS -----
        if (search != null) {
            if (search.containsKey("branchName")) {
                dataQuery.setParameter("branchNameSearch", search.get("branchName"));
                countQuery.setParameter("branchNameSearch", search.get("branchName"));
            }
            if (search.containsKey("location")) {
                dataQuery.setParameter("locationSearch", search.get("location"));
                countQuery.setParameter("locationSearch", search.get("location"));
            }
            if (search.containsKey("contactInfo")) {
                dataQuery.setParameter("contactSearch", search.get("contactInfo"));
                countQuery.setParameter("contactSearch", search.get("contactInfo"));
            }
            if (search.containsKey("branchStatus")) {
                dataQuery.setParameter("statusSearch", search.get("branchStatus"));
                countQuery.setParameter("statusSearch", search.get("branchStatus"));
            }
        }

        // ----- PAGINATION -----
        if (filterRequest.getPaginationRequest() != null) {
            int pageNumber = filterRequest.getPaginationRequest().getPageNumber();
            int pageSize = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(pageNumber * pageSize);
            dataQuery.setMaxResults(pageSize);
        }

        // ----- EXECUTE QUERIES -----
        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        @SuppressWarnings("unchecked")
        List<Object[]> rows = dataQuery.getResultList();

        // ----- MAP RESULTS -----
        List<BranchResponse> resultList = new ArrayList<>();
        for (Object[] row : rows) {
            BranchResponse br = new BranchResponse();
            br.setBranchId(row[0] != null ? ((Number) row[0]).longValue() : null);
            br.setBranchName((String) row[1]);
            br.setLocation((String) row[2]);
            br.setContactInfo((String) row[3]);
            br.setPhoneNumber((String) row[4]);
            br.setCreatedAt(row[5] != null ? ((Timestamp) row[5]).toLocalDateTime() : null);
            br.setBranchStatus(row[6] != null ? BranchStatus.valueOf((String) row[6]) : null);
            br.setBranchType(row[7] != null ? BranchType.valueOf((String) row[7]) : null);
            br.setEditedBy((String) row[8]);
            br.setPincode((String) row[9]);
            br.setCity((String) row[10]);
            br.setState((String) row[11]);
            resultList.add(br);
        }

        // ----- WRAP INTO RESULT -----
        ResultDto<BranchResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(resultList);

        log.info("Exit [BranchCustomRepository] [getBranchDetails] with count = {}", totalCount);
        return resultDto;
    }
}
