package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ContractResponse;
import com.erp.Enum.ContractStatus;
import com.erp.Enum.ServiceFrequency;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ContractCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<ContractResponse> getFilteredContracts(FilterRequest filterRequest) {
        log.info("Into [ContractCustomRepository] [getFilteredContracts]");

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    c.id,
                    c.created_at,
                    c.start_date,
                    c.end_date,
                    c.service_frequency,
                    c.total_value,
                    c.contract_status,
                    cd.customer_name
                FROM contracts c
                INNER JOIN customer cd ON c.customer_id = cd.id
                WHERE 1=1
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*)
                FROM contracts c
                INNER JOIN customer cd ON c.customer_id = cd.id
                WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ---------- FILTER CONDITIONS ----------
        if (filters != null) {
            if (filters.containsKey("contractStatus") && !filters.get("contractStatus").isEmpty()) {
                sql.append(" AND c.contract_status = :contractStatus");
                countSql.append(" AND c.contract_status = :contractStatus");
            }
            if (filters.containsKey("serviceFrequency") && !filters.get("serviceFrequency").isEmpty()) {
                sql.append(" AND c.service_frequency = :serviceFrequency");
                countSql.append(" AND c.service_frequency = :serviceFrequency");
            }
            if (filters.containsKey("customerName") && !filters.get("customerName").isEmpty()) {
                sql.append(" AND LOWER(cd.customer_name) = LOWER(:customerName)");
                countSql.append(" AND LOWER(cd.customer_name) = LOWER(:customerName)");
            }
            if (filters.containsKey("startDate") && !filters.get("startDate").isEmpty()) {
                sql.append(" AND c.start_date >= :startDate");
                countSql.append(" AND c.start_date >= :startDate");
            }
            if (filters.containsKey("endDate") && !filters.get("endDate").isEmpty()) {
                sql.append(" AND c.end_date <= :endDate");
                countSql.append(" AND c.end_date <= :endDate");
            }
        }

        // ---------- SEARCH CONDITIONS ----------
        if (search != null) {
            if (search.containsKey("customerName") && !search.get("customerName").isEmpty()) {
                sql.append(" AND LOWER(cd.customer_name) LIKE :customerNameSearch");
                countSql.append(" AND LOWER(cd.customer_name) LIKE :customerNameSearch");
            }
            if (search.containsKey("contractStatus") && !search.get("contractStatus").isEmpty()) {
                sql.append(" AND LOWER(c.contract_status::text) LIKE :contractStatusSearch");
                countSql.append(" AND LOWER(c.contract_status::text) LIKE :contractStatusSearch");
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
                    case "startDate" -> orderClauses.add("c.start_date " + direction);
                    case "endDate" -> orderClauses.add("c.end_date " + direction);
                    case "contractStatus" -> orderClauses.add("c.contract_status " + direction);
                    case "customerName" -> orderClauses.add("cd.customer_name " + direction);
                    default -> orderClauses.add("c.id DESC");
                }
            }
            sql.append(String.join(", ", orderClauses));
        } else {
            sql.append(" ORDER BY c.id DESC");
        }

        log.info("[ContractCustomRepository] [getFilteredContracts] :: Query {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ---------- BIND FILTER PARAMETERS ----------
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "contractStatus" -> {
                            dataQuery.setParameter("contractStatus", value.toUpperCase());
                            countQuery.setParameter("contractStatus", value.toUpperCase());
                        }
                        case "serviceFrequency" -> {
                            dataQuery.setParameter("serviceFrequency", value.toUpperCase());
                            countQuery.setParameter("serviceFrequency", value.toUpperCase());
                        }
                        case "customerName" -> {
                            dataQuery.setParameter("customerName", value.toLowerCase());
                            countQuery.setParameter("customerName", value.toLowerCase());
                        }
                        case "startDate" -> {
                            Date start = Date.valueOf(value);
                            dataQuery.setParameter("startDate", start);
                            countQuery.setParameter("startDate", start);
                        }
                        case "endDate" -> {
                            Date end = Date.valueOf(value);
                            dataQuery.setParameter("endDate", end);
                            countQuery.setParameter("endDate", end);
                        }
                    }
                }
            });
        }

        // ---------- BIND SEARCH PARAMETERS ----------
        if (search != null) {
            search.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "customerName" -> {
                            dataQuery.setParameter("customerNameSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("customerNameSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "contractStatus" -> {
                            dataQuery.setParameter("contractStatusSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("contractStatusSearch", "%" + value.toLowerCase() + "%");
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
        List<ContractResponse> results = new ArrayList<>();
        for (Object[] row : rows) {
            ContractResponse response = new ContractResponse();
            response.setId(((Number) row[0]).longValue());
            response.setCreatedAt(((java.sql.Timestamp) row[1]).toLocalDateTime());
            response.setStartDate(((Date) row[2]).toLocalDate());
            response.setEndDate(((Date) row[3]).toLocalDate());
            response.setServiceFrequency(row[4] != null ? ServiceFrequency.valueOf(row[4].toString()) : null);
            response.setTotalValue(row[5] != null ? (BigDecimal) row[5] : BigDecimal.ZERO);
            response.setContractStatus(row[6] != null ? ContractStatus.valueOf(row[6].toString()) : null);
            response.setCustomerName((String) row[7]);
            results.add(response);
        }

        ResultDto<ContractResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(results);

        log.info("Exit [ContractCustomRepository] [getFilteredContracts] with count = {}", totalCount);
        return resultDto;
    }
}
