package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.CompanyDetailsResponse;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Enum.IndustryType;
import com.erp.Enum.ReviewStatus;
import com.erp.Enum.ShipmentReferenceType;
import com.erp.Enum.ShipmentStatus;
import com.erp.Mapper.companyDetails.CompanyDetailsMapper;
import com.erp.Model.CompanyDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class CompanyDetailsCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CompanyDetailsMapper companyDetailsMapper;

    public ResultDto<CompanyDetailsResponseDto> getFilterData(FilterRequest filterRequest) {

        log.info("Into [CompanyDetailsCustomRepository] [getFilterData]");

        // -------- BASE QUERY --------
        StringBuilder jpql = new StringBuilder("SELECT c FROM CompanyDetails c WHERE 1=1");

        // -------- Collecting Data --------
        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderby = filterRequest.getOrderByColumns();

        // -------- For Setting Base Query -------
        // ---- FILTERS ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("reviewStatus"))
                jpql.append(" AND c.reviewStatus = :reviewStatus");
            if (filters.containsKey("industryType"))
                jpql.append(" AND c.industryType = :industryType");
            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
                jpql.append(" AND c.createdDate BETWEEN :startDate AND :endDate");
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("name"))
            jpql.append(" AND LOWER(c.name) LIKE LOWER(:name)");

        // ---- ORDER BY ----
        if (orderby != null && !orderby.isEmpty()) {
            jpql.append(" ORDER BY ");
            List<String> orderClause = new ArrayList<>();
            orderby.forEach((column, direction) ->
            {
                switch (column) {
                    case "createdDate" ->
                            orderClause.add("c.createdDate " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                }
            });
            jpql.append(String.join(", ", orderClause));
        }


        // -------- CREATE REAL QUERY --------
        TypedQuery<CompanyDetails> query = entityManager.createQuery(jpql.toString(), CompanyDetails.class);

        // -------- For Setting Real Query --------
        // ---- Set Filter Params ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("reviewStatus"))
                query.setParameter("reviewStatus", ReviewStatus.valueOf(filters.get("reviewStatus")));
            if (filters.containsKey("industryType"))
                query.setParameter("industryType", IndustryType.valueOf(filters.get("industryType")));
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                query.setParameter("startDate", parseInstant(filters.get("startDate")));
                query.setParameter("endDate", parseInstant(filters.get("endDate")));
            }
        }

        // ---- Set Search Params ----
        if (search != null && search.containsKey("name"))
            query.setParameter("name", "%" + search.get("name").trim() + "%");

        // ---- Pagination ----
        if (filterRequest.getPaginationRequest() != null) {
            int pageNumber = filterRequest.getPaginationRequest().getPageNumber();
            int pageSize = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
        }


        // -------- Getting Data According To Filter --------
        List<CompanyDetails> companies = query.getResultList();

        // -------- COUNT QUERY -------
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(c) FROM CompanyDetails c WHERE 1=1");

        // ---- FILTER ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("reviewStatus"))
                countJpql.append(" AND c.reviewStatus = :reviewStatus");
            if (filters.containsKey("industryType"))
                countJpql.append(" AND c.industryType = :industryType");
            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
                countJpql.append(" AND c.createdDate BETWEEN :startDate AND :endDate");
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("name"))
            countJpql.append(" AND LOWER(c.name) LIKE LOWER(:name)");


        // -------- CREATING REAL QUERY FOR COUNT --------
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);

        // ---- Setting Real Query ----
        // ---- Filter ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("reviewStatus"))
                countQuery.setParameter("reviewStatus", ReviewStatus.valueOf(filters.get("reviewStatus")));
            if (filters.containsKey("industryType"))
                countQuery.setParameter("industryType", IndustryType.valueOf(filters.get("industryType")));
            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                countQuery.setParameter("startDate", parseInstant(filters.get("startDate")));
                countQuery.setParameter("endDate", parseInstant(filters.get("endDate")));
            }
        }

        // ---- Search ----
        if (search != null && search.containsKey("name"))
            countQuery.setParameter("name", "%" + search.get("name").trim() + "%");


        // ---- Get Total Number of Records ----
        long totalCount = countQuery.getSingleResult();

        // ---- Setting ResultDto ----
        ResultDto<CompanyDetailsResponseDto> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);

        // ---- Convert to Response
        List<CompanyDetailsResponseDto> res = companyDetailsMapper.toResponseDtoList(companies);

        resultDto.setResults(res);

        log.info("Exit [ShipmentCustomRepository] with count = {}, pageResults = {}", totalCount, companies.size());
        return resultDto;

    }

    private Instant parseInstant(String value) {

        DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        LocalDateTime ldt;

        if (value.contains("T")) {
            ldt = LocalDateTime.parse(value, f2);
        } else {
            ldt = LocalDateTime.parse(value, f1);
        }

        // Convert using system default timezone (IST)
        return ldt.atZone(ZoneId.systemDefault()).toInstant();
    }

}
