package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.CompanyDetailsResponseDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.SubscriptionsDto.SubscriptionDto;
import com.erp.Enum.IndustryType;
import com.erp.Enum.ReviewStatus;
import com.erp.Mapper.SubscriptionModule.SubscriptionMapper;
import com.erp.Model.CompanyDetails;
import com.erp.Model.SubscriptionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class SubscriptionCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<SubscriptionDto> getSubscriptionsFilter(FilterRequest filterRequest){

        log.info("Into [SubscriptionCustomRepository] [getSubscriptionsFilter]");

        StringBuilder jpql = new StringBuilder("SELECT s FROM SubscriptionEntity s WHERE 1=1");

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderby = filterRequest.getOrderByColumns();

        // Build filter JPQL
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("paymentStatus"))
                jpql.append(" AND s.paymentStatus = :paymentStatus");

            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                jpql.append(" AND s.createdAt BETWEEN :startDate AND :endDate");
            }
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("userId"))
            jpql.append(" AND LOWER(s.userId) LIKE LOWER(:userId)");

        // ---- ORDER BY ----
        if (orderby != null && !orderby.isEmpty()) {
            jpql.append(" ORDER BY ");
            List<String> orderClause = new ArrayList<>();
            orderby.forEach((column, direction) ->
            {
                switch (column) {
                    case "accountUser" ->
                            orderClause.add("s.accountUser " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                    case "planStartDate" ->
                            orderClause.add("s.planStartDate " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                    case "createdAt" ->
                            orderClause.add("s.createdAT " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                    case "planEndDate" ->
                            orderClause.add("s.planEndDate " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));                }
            });
            jpql.append(String.join(", ", orderClause));
        }


        // -------- CREATE REAL QUERY --------
        TypedQuery<SubscriptionEntity> query =
                entityManager.createQuery(jpql.toString(), SubscriptionEntity.class);

        // Apply filter parameters
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("paymentStatus"))
                query.setParameter("paymentStatus", filters.get("paymentStatus"));

            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                query.setParameter("startDate", LocalDateTime.parse(filters.get("startDate")));
                query.setParameter("endDate", LocalDateTime.parse(filters.get("endDate")));
            }
        }

        // ---- Set Search Params ----
        if (search != null && search.containsKey("userId"))
            query.setParameter("userId", "%" + search.get("userId").trim() + "%");

        // -------- Getting Data According To Filter --------
        List<SubscriptionEntity> subscriptions = query.getResultList();

        // Count query
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(s) FROM SubscriptionEntity s WHERE 1=1");

        // ---- FILTER ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("paymentStatus"))
                countJpql.append(" AND s.paymentStatus = :paymentStatus");

            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                countJpql.append(" AND s.createdAt BETWEEN :startDate AND :endDate");
            }
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("userId"))
            countJpql.append(" AND LOWER(s.userId) LIKE LOWER(:userId)");

        // -------- CREATING REAL QUERY FOR COUNT --------
        TypedQuery<Long> countQuery =
                entityManager.createQuery(countJpql.toString(), Long.class);

        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("paymentStatus"))
                countQuery.setParameter("paymentStatus", filters.get("paymentStatus"));

            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {
                countQuery.setParameter("startDate", LocalDateTime.parse(filters.get("startDate")));
                countQuery.setParameter("endDate", LocalDateTime.parse(filters.get("endDate")));
            }
        }

        // ---- Search ----
        if (search != null && search.containsKey("userId"))
            countQuery.setParameter("userId", "%" + search.get("userId").trim() + "%");

        long totalCount = countQuery.getSingleResult();

        ResultDto<SubscriptionDto> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(SubscriptionMapper.toSubscriptionDtoList(subscriptions));

        log.info("Exit [SubscriptionCustomRepository] with count = {}, pageResults = {}",
                totalCount, subscriptions.size());

        return resultDto;
    }

    private LocalDate parseToLocalDate(String value) {
        if (value == null) return null;

        if (value.contains("T")) {
            return LocalDateTime.parse(value).toLocalDate();
        }
        return LocalDate.parse(value);
    }
}
