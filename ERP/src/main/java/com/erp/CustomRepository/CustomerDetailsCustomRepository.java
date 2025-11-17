package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.CustomerResponse;
import com.erp.Dto.Response.ProductDetailDto;
import com.erp.Dto.Response.ResultDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class CustomerDetailsCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<CustomerResponse> getFilteredCustomers(FilterRequest filterRequest) {
        log.info("Inside [CustomerCustomRepository] getFilteredCustomers");

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    id,
                    customer_name,
                    company_name,
                    email,
                    phone,
                    address_line_1,
                    address_line_2,
                    landmark,
                    city,
                    state,
                    country,
                    pincode,
                    tags,
                    customer_status,
                    joined_date,
                    created_at,
                    updated_at
                FROM customer
                WHERE 1=1
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*) FROM customer WHERE 1=1
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ---------------- FILTERS ----------------
        if (filters != null) {

            if (filters.containsKey("id") && !filters.get("id").isEmpty()) {
                sql.append(" AND id = :id");
                countSql.append(" AND id = :id");
            }

            if (filters.containsKey("customerName") && !filters.get("customerName").isEmpty()) {
                sql.append(" AND LOWER(customer_name) LIKE :customerName");
                countSql.append(" AND LOWER(customer_name) LIKE :customerName");
            }

            if (filters.containsKey("email") && !filters.get("email").isEmpty()) {
                sql.append(" AND email = :email");
                countSql.append(" AND email = :email");
            }

            if (filters.containsKey("phone") && !filters.get("phone").isEmpty()) {
                sql.append(" AND phone = :phone");
                countSql.append(" AND phone = :phone");
            }

            if (filters.containsKey("companyName") && !filters.get("companyName").isEmpty()) {
                sql.append(" AND LOWER(company_name) LIKE :companyName");
                countSql.append(" AND LOWER(company_name) LIKE :companyName");
            }

            if (filters.containsKey("customerStatus") && !filters.get("customerStatus").isEmpty()) {
                sql.append(" AND customer_status = :customerStatus");
                countSql.append(" AND customer_status = :customerStatus");
            }
        }

        // ---------------- SEARCH ----------------
        if (search != null) {

            if (search.containsKey("customerName") && !search.get("customerName").isEmpty()) {
                sql.append(" AND LOWER(customer_name) LIKE :customerNameSearch");
                countSql.append(" AND LOWER(customer_name) LIKE :customerNameSearch");
            }

            if (search.containsKey("email") && !search.get("email").isEmpty()) {
                sql.append(" AND LOWER(email) LIKE :emailSearch");
                countSql.append(" AND LOWER(email) LIKE :emailSearch");
            }

            if (search.containsKey("phone") && !search.get("phone").isEmpty()) {
                sql.append(" AND phone LIKE :phoneSearch");
                countSql.append(" AND phone LIKE :phoneSearch");
            }
        }

        // ---------------- ORDER BY ----------------
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ");
            List<String> clauses = new ArrayList<>();
            for (Map.Entry<String, String> entry : orderBy.entrySet()) {
                String direction = entry.getValue().equalsIgnoreCase("desc") ? "DESC" : "ASC";
                clauses.add(entry.getKey() + " " + direction);
            }
            sql.append(String.join(", ", clauses));
        } else {
            sql.append(" ORDER BY id DESC");
        }

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        bindParams(filters, search, dataQuery, countQuery);

        // ---------------- PAGINATION ----------------
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        List<CustomerResponse> results = new ArrayList<>();

        for (Object[] row : rows) {
            CustomerResponse dto = new CustomerResponse();

            dto.setId(((Number) row[0]).longValue());
            dto.setCustomerName((String) row[1]);
            dto.setCompanyName((String) row[2]);
            dto.setEmail((String) row[3]);
            dto.setPhone((String) row[4]);
            dto.setAddressLine1((String) row[5]);
            dto.setAddressLine2((String) row[6]);
            dto.setLandmark((String) row[7]);
            dto.setCity((String) row[8]);
            dto.setState((String) row[9]);
            dto.setCountry((String) row[10]);
            dto.setPincode((String) row[11]);
            dto.setTags((String) row[12]);
            dto.setCustomerStatus((String) row[13]);

            if (row[14] instanceof java.sql.Date d) dto.setJoinedDate(d.toLocalDate());
            if (row[15] instanceof java.sql.Timestamp t1) dto.setCreatedAt(t1.toLocalDateTime());
            if (row[16] instanceof java.sql.Timestamp t2) dto.setUpdatedAt(t2.toLocalDateTime());

            Long customerId = ((Number) row[0]).longValue();
            dto.setProductDetailDto( getProductsByCustomerId(customerId));

            results.add(dto);
        }

        ResultDto<CustomerResponse> result = new ResultDto<>();
        result.setCount(totalCount);
        result.setResults(results);

        return result;
    }

    private void bindParams(Map<String, String> filters, Map<String, String> search,
                            Query dataQuery, Query countQuery) {

        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "id" -> {
                            dataQuery.setParameter("id", Long.parseLong(value));
                            countQuery.setParameter("id", Long.parseLong(value));
                        }
                        case "customerName" -> {
                            dataQuery.setParameter("customerName", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("customerName", "%" + value.toLowerCase() + "%");
                        }
                        case "companyName" -> {
                            dataQuery.setParameter("companyName", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("companyName", "%" + value.toLowerCase() + "%");
                        }
                        case "email", "phone", "customerStatus" -> {
                            dataQuery.setParameter(key, value);
                            countQuery.setParameter(key, value);
                        }
                    }
                }
            });
        }

        if (search != null) {
            search.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "customerName" -> {
                            dataQuery.setParameter("customerNameSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("customerNameSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "email" -> {
                            dataQuery.setParameter("emailSearch", "%" + value.toLowerCase() + "%");
                            countQuery.setParameter("emailSearch", "%" + value.toLowerCase() + "%");
                        }
                        case "phone" -> {
                            dataQuery.setParameter("phoneSearch", "%" + value + "%");
                            countQuery.setParameter("phoneSearch", "%" + value + "%");
                        }
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    private List<ProductDetailDto> getProductsByCustomerId(Long customerId) {

        String sql = """
            SELECT product_id, quantity 
            FROM customer_details_mapper 
            WHERE customer_id = :customerId
            """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("customerId", customerId);

        List<Object[]> rows = query.getResultList();
        List<ProductDetailDto> products = new ArrayList<>();

        for (Object[] row : rows) {
            ProductDetailDto dto = new ProductDetailDto();
            dto.setProductId(((Number) row[0]).longValue());
            dto.setQuantity(row[1] != null ? ((Number) row[1]).intValue() : null);
            products.add(dto);
        }

        return products;
    }

}
