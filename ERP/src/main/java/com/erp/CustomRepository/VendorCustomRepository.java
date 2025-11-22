package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.VendorResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class VendorCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<VendorResponseDTO> getFilteredVendors(FilterRequest filterRequest) {

        log.info("Into [VendorCustomRepository] [getFilteredVendors]");

        // ========== BASE QUERY (ONLY ACTIVE VENDORS) ==========
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    v.vendor_id,
                    v.vendor_name,
                    v.contact_person,
                    v.phone_number,
                    v.email_address,
                    v.billing_address,
                    v.payment_terms,
                    v.credit_limit,
                    v.is_active,
                    v.created_date,
                    v.updated_date
                FROM vendors v
                WHERE 1=1
                  AND v.is_active = TRUE
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) 
                FROM vendors v
                WHERE 1=1
                  AND v.is_active = TRUE
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ========== FILTERS ==========
        if (filters != null) {

            if (filters.containsKey("vendorId")) {
                sql.append(" AND v.vendor_id = :vendorId");
                countSql.append(" AND v.vendor_id = :vendorId");
            }

            if (filters.containsKey("emailAddress")) {
                sql.append(" AND v.email_address = :emailAddress");
                countSql.append(" AND v.email_address = :emailAddress");
            }

            if (filters.containsKey("vendorName")) {
                sql.append(" AND LOWER(v.vendor_name) LIKE :vendorNameFilter");
                countSql.append(" AND LOWER(v.vendor_name) LIKE :vendorNameFilter");
            }
        }

        // ========== SEARCH ==========
        if (search != null) {

            if (search.containsKey("vendorName")) {
                sql.append(" AND LOWER(v.vendor_name) LIKE :vendorNameSearch");
                countSql.append(" AND LOWER(v.vendor_name) LIKE :vendorNameSearch");
            }

            if (search.containsKey("contactPerson")) {
                sql.append(" AND LOWER(v.contact_person) LIKE :contactPersonSearch");
                countSql.append(" AND LOWER(v.contact_person) LIKE :contactPersonSearch");
            }

            if (search.containsKey("emailAddress")) {
                sql.append(" AND LOWER(v.email_address) LIKE :emailAddressSearch");
                countSql.append(" AND LOWER(v.email_address) LIKE :emailAddressSearch");
            }
        }

        // ========== ORDER BY ==========
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> orderList = new ArrayList<>();

            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";

                switch (entry.getKey()) {
                    case "vendorName" -> orderList.add("v.vendor_name " + direction);
                    case "emailAddress" -> orderList.add("v.email_address " + direction);
                    case "createdDate" -> orderList.add("v.created_date " + direction);
                    default -> orderList.add("v.vendor_id DESC");
                }
            }
            sql.append(String.join(", ", orderList));
        } else {
            sql.append(" ORDER BY v.vendor_id DESC");
        }

        log.info("[VendorCustomRepository] FINAL QUERY: {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        // ========== BIND PARAMETERS ==========
        bindParameters(filters, search, dataQuery, countQuery);

        // ========== PAGINATION ==========
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        // ========== MAP RESULT ==========
        List<VendorResponseDTO> resultList = new ArrayList<>();

        for (Object[] row : rows) {
            VendorResponseDTO dto = new VendorResponseDTO();

            dto.setVendorId(((Number) row[0]).longValue());
            dto.setVendorName((String) row[1]);
            dto.setContactPerson((String) row[2]);
            dto.setPhoneNumber((String) row[3]);
            dto.setEmailAddress((String) row[4]);
            dto.setBillingAddress((String) row[5]);
            dto.setPaymentTerms((String) row[6]);

            // FIXED BigDecimal handling
            dto.setCreditLimit(
                    row[7] != null ? new BigDecimal(row[7].toString()) : null
            );

            dto.setIsActive((Boolean) row[8]);

            dto.setCreatedDate(
                    row[9] != null ? ((java.sql.Timestamp) row[9]).toLocalDateTime() : null
            );

            dto.setUpdatedDate(
                    row[10] != null ? ((java.sql.Timestamp) row[10]).toLocalDateTime() : null
            );

            resultList.add(dto);
        }

        ResultDto<VendorResponseDTO> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(resultList);

        return resultDto;
    }

    private void bindParameters(Map<String, String> filters, Map<String, String> search,
                                Query dataQuery, Query countQuery) {

        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value == null || value.isEmpty()) return;

                switch (key) {
                    case "vendorId" -> {
                        Long id = Long.parseLong(value);
                        dataQuery.setParameter("vendorId", id);
                        countQuery.setParameter("vendorId", id);
                    }
                    case "emailAddress" -> {
                        dataQuery.setParameter("emailAddress", value);
                        countQuery.setParameter("emailAddress", value);
                    }
                    case "vendorName" -> {
                        String pattern = "%" + value.toLowerCase() + "%";
                        dataQuery.setParameter("vendorNameFilter", pattern);
                        countQuery.setParameter("vendorNameFilter", pattern);
                    }
                }
            });
        }

        if (search != null) {
            search.forEach((key, value) -> {
                if (value == null || value.isEmpty()) return;

                switch (key) {
                    case "vendorName" -> {
                        String pattern = "%" + value.toLowerCase() + "%";
                        dataQuery.setParameter("vendorNameSearch", pattern);
                        countQuery.setParameter("vendorNameSearch", pattern);
                    }
                    case "contactPerson" -> {
                        String pattern = "%" + value.toLowerCase() + "%";
                        dataQuery.setParameter("contactPersonSearch", pattern);
                        countQuery.setParameter("contactPersonSearch", pattern);
                    }
                    case "emailAddress" -> {
                        String pattern = "%" + value.toLowerCase() + "%";
                        dataQuery.setParameter("emailAddressSearch", pattern);
                        countQuery.setParameter("emailAddressSearch", pattern);
                    }
                }
            });
        }
    }
}
