package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ContractResponse;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.ContractStatus;
import com.erp.Enum.ServiceFrequency;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ContractCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<ContractResponse> getFilteredContracts(FilterRequest filterRequest) {
        log.info("➡ [ContractCustomRepository] [getFilteredContracts] called");

        ResultDto<ContractResponse> resultDto = new ResultDto<>();
        List<ContractResponse> results = new ArrayList<>();
        long totalCount = 0L;

        try {
            // ✅ Added created_at in SELECT
            StringBuilder sql = new StringBuilder("""
                SELECT 
                    c.id,
                    c.contract_status,
                    c.start_date,
                    c.end_date,
                    c.total_value,
                    c.service_frequency,
                    cd.name AS customer_name,
                    c.created_at
                FROM contracts c
                LEFT JOIN tenant_1_palak_gmail_com.customer_details cd 
                    ON c.customer_id = cd.id
                WHERE 1=1
            """);

            StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*)
                FROM contracts c
                LEFT JOIN tenant_1_palak_gmail_com.customer_details cd 
                    ON c.customer_id = cd.id
                WHERE 1=1
            """);

            Map<String, String> filters = filterRequest.getFilterColumns();
            Map<String, String> orderBy = filterRequest.getOrderByColumns();

            // ---------- FILTERS ----------
            if (filters != null) {
                if (filters.containsKey("contractStatus") && !filters.get("contractStatus").isEmpty()) {
                    sql.append(" AND c.contract_status = :contractStatus");
                    countSql.append(" AND c.contract_status = :contractStatus");
                }
                if (filters.containsKey("serviceFrequency") && !filters.get("serviceFrequency").isEmpty()) {
                    sql.append(" AND c.service_frequency = :serviceFrequency");
                    countSql.append(" AND c.service_frequency = :serviceFrequency");
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
                        case "totalValue" -> orderClauses.add("c.total_value " + direction);
                        case "createdAt" -> orderClauses.add("c.created_at " + direction);
                        default -> orderClauses.add("c.id DESC");
                    }
                }
                sql.append(String.join(", ", orderClauses));
            } else {
                sql.append(" ORDER BY c.id DESC");
            }

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

            // ---------- PAGINATION ----------
            if (filterRequest.getPaginationRequest() != null) {
                int page = filterRequest.getPaginationRequest().getPageNumber();
                int size = filterRequest.getPaginationRequest().getPageSize();
                dataQuery.setFirstResult(page * size);
                dataQuery.setMaxResults(size);
            }

            // ---------- EXECUTE ----------
            totalCount = ((Number) countQuery.getSingleResult()).longValue();
            List<Object[]> rows = dataQuery.getResultList();

            // ---------- MAP TO DTO ----------
            for (Object[] row : rows) {
                ContractResponse response = new ContractResponse();

                response.setId(row[0] != null ? ((Number) row[0]).longValue() : null);

                // contract_status
                if (row[1] != null) {
                    try {
                        response.setContractStatus(ContractStatus.valueOf(row[1].toString().trim().toUpperCase()));
                    } catch (IllegalArgumentException ex) {
                        log.warn("Invalid contractStatus value: {}", row[1]);
                    }
                }

                response.setStartDate(convertToLocalDate(row[2]));
                response.setEndDate(convertToLocalDate(row[3]));

                if (row[4] != null) {
                    if (row[4] instanceof BigDecimal) {
                        response.setTotalValue((BigDecimal) row[4]);
                    } else {
                        response.setTotalValue(new BigDecimal(row[4].toString()));
                    }
                } else {
                    response.setTotalValue(BigDecimal.ZERO);
                }

                if (row[5] != null) {
                    try {
                        response.setServiceFrequency(ServiceFrequency.valueOf(row[5].toString().trim().toUpperCase()));
                    } catch (IllegalArgumentException ex) {
                        log.warn("Invalid serviceFrequency value: {}", row[5]);
                    }
                }

                response.setCustomerName(row[6] != null ? row[6].toString() : null);

                // ✅ created_at (index 7)
                response.setCreatedAt(convertToLocalDateTime(row[7]));

                results.add(response);
            }

        } catch (Exception e) {
            log.error("❌ Error in [ContractCustomRepository] [getFilteredContracts]: {}", e.getMessage(), e);
            resultDto.setCount(0);
            resultDto.setResults(new ArrayList<>());
            return resultDto;
        }

        resultDto.setCount(totalCount);
        resultDto.setResults(results);
        log.info("✅ Exit [getFilteredContracts] with totalCount = {}", totalCount);
        return resultDto;
    }




    // ---------- Helper for safe date conversion ----------
    private LocalDate convertToLocalDate(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Date) {
                return ((Date) value).toLocalDate();
            } else if (value instanceof Timestamp) {
                return ((Timestamp) value).toLocalDateTime().toLocalDate();
            } else if (value instanceof LocalDate) {
                return (LocalDate) value;
            } else {
                return LocalDate.parse(value.toString());
            }
        } catch (Exception ex) {
            log.warn("Date conversion failed for value: {}", value);
            return null;
        }
    }


    private LocalDateTime convertToLocalDateTime(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Timestamp) {
                return ((Timestamp) value).toLocalDateTime();
            } else if (value instanceof Date) {
                return ((Date) value).toLocalDate().atStartOfDay();
            } else if (value instanceof LocalDateTime) {
                return (LocalDateTime) value;
            } else if (value instanceof LocalDate) {
                return ((LocalDate) value).atStartOfDay();
            } else {
                return LocalDateTime.parse(value.toString());
            }
        } catch (Exception ex) {
            log.warn("DateTime conversion failed for value: {}", value);
            return null;
        }
    }

}
