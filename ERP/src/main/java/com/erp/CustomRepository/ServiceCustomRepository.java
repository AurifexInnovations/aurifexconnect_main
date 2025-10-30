package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ServiceResponse;
import com.erp.Enum.ServiceCategory;
import com.erp.Enum.ServiceStatus;
import com.erp.Enum.ShipmentReferenceType;
import com.erp.Enum.ShipmentStatus;
import com.erp.Mapper.Service.ServiceMapper;
import com.erp.Model.Service;
import com.erp.Model.ShipmentDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ServiceCustomRepository
{
    @Autowired
    private ServiceMapper serviceMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<ServiceResponse> getServiceDetailsFilter(FilterRequest filterRequest)
    {
        log.info("Into [ServiceCustomRepository] [getServiceDetailsFilter]");

        // -------- BASE QUERY --------
        StringBuilder jpql = new StringBuilder("SELECT s FROM Service s WHERE 1=1");

        // -------- Collecting Data --------
        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderby = filterRequest.getOrderByColumns();

        // -------- For Setting Base Query -------
        // ---- FILTERS ----
        if (filters != null && !filters.isEmpty())
        {
            if(filters.containsKey("serviceStatus"))
            {
                jpql.append(" AND s.serviceStatus = :serviceStatus");
            }
            if(filters.containsKey("serviceCategory"))
            {
                jpql.append(" AND s.serviceCategory = :serviceCategory");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
            {
                jpql.append(" AND s.createdAt BETWEEN :startDate AND :endDate");
            }
            if (filters.containsKey("startPrice") && filters.containsKey("endPrice"))
            {
                jpql.append(" AND s.servicePrice BETWEEN :startPrice AND :endPrice");
            }
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("serviceName"))
            jpql.append(" AND LOWER(s.serviceName) LIKE LOWER(:serviceName)");

        // ---- ORDER BY ----
        if (orderby != null && !orderby.isEmpty())
        {
            jpql.append(" ORDER BY ");
            List<String> orderClause = new ArrayList<>();
            orderby.forEach((column, direction) ->
            {
                switch (column)
                {
                    case "createdAt" -> orderClause.add("s.createdAt " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                    case "servicePrice" -> orderClause.add("s.servicePrice " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                }
            });
            jpql.append(String.join(", ", orderClause));
        }


        // -------- CREATE REAL QUERY --------
        TypedQuery<Service> query = entityManager.createQuery(jpql.toString(), Service.class);

        // -------- For Setting Real Query --------
        // ---- Set Filter Params ----
        if (filters != null && !filters.isEmpty())
        {
            if(filters.containsKey("serviceStatus"))
            {
                query.setParameter("serviceStatus", ServiceStatus.valueOf(filters.get("serviceStatus")));
            }
            if(filters.containsKey("serviceCategory"))
            {
                query.setParameter("serviceCategory", ServiceCategory.valueOf(filters.get("serviceCategory")));
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
            {
                query.setParameter("startDate", LocalDateTime.parse(filters.get("startDate")));
                query.setParameter("endDate", LocalDateTime.parse(filters.get("endDate")));
            }
            if (filters.containsKey("startPrice") && filters.containsKey("endPrice"))
            {
                query.setParameter("startPrice", Double.parseDouble(filters.get("startPrice")));
                query.setParameter("endPrice", Double.parseDouble(filters.get("endPrice")));
            }
        }

        // ---- Set Search Params ----
        if (search != null && search.containsKey("serviceName"))
            query.setParameter("serviceName", "%" + search.get("serviceName").trim() + "%");

        // ---- Pagination ----
        if (filterRequest.getPaginationRequest() != null)
        {
            int pageNumber = filterRequest.getPaginationRequest().getPageNumber();
            int pageSize = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
        }


        // -------- Getting Data According To Filter --------
        List<Service> services = query.getResultList();

//        List<ServiceResponse> servicesResponses = services.stream()
//                .map( ServiceMapper::mapToServiceResponse )
//                .collect( Collectors.toList() );

        List<ServiceResponse> serviceResponses = new ArrayList<>();
        for(Service s : services)
        {
            serviceResponses.add(serviceMapper.mapToServiceResponse(s));
        }

        // -------- COUNT QUERY --------
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(s) FROM Service s WHERE 1=1");

        // ---- FILTER ----
        if (filters != null && !filters.isEmpty())
        {
            if(filters.containsKey("serviceStatus"))
            {
                countJpql.append(" AND s.serviceStatus = :serviceStatus");
            }
            if(filters.containsKey("serviceCategory"))
            {
                countJpql.append(" AND s.serviceCategory = :serviceCategory");
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
            {
                countJpql.append(" AND s.createdAt BETWEEN :startDate AND :endDate");
            }
            if (filters.containsKey("startPrice") && filters.containsKey("endPrice"))
            {
                countJpql.append(" AND s.servicePrice BETWEEN :startPrice AND :endPrice");
            }
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("serviceName"))
            countJpql.append(" AND LOWER(s.serviceName) LIKE LOWER(:serviceName)");

        // -------- CREATING REAL QUERY FOR COUNT --------
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);

        // ---- Setting Real Query ----
        // ---- Filter ----
        if (filters != null && !filters.isEmpty())
        {
            if(filters.containsKey("serviceStatus"))
            {
                countQuery.setParameter("serviceStatus", ServiceStatus.valueOf(filters.get("serviceStatus")));
            }
            if(filters.containsKey("serviceCategory"))
            {
                countQuery.setParameter("serviceCategory", ServiceCategory.valueOf(filters.get("serviceCategory")));
            }
            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
            {
                countQuery.setParameter("startDate", LocalDateTime.parse(filters.get("startDate")));
                countQuery.setParameter("endDate", LocalDateTime.parse(filters.get("endDate")));
            }
            if (filters.containsKey("startPrice") && filters.containsKey("endPrice"))
            {
                countQuery.setParameter("startPrice", Double.parseDouble(filters.get("startPrice")));
                countQuery.setParameter("endPrice", Double.parseDouble(filters.get("endPrice")));
            }
        }

        // ---- Search ----
        if (search != null && search.containsKey("serviceName"))
            countQuery.setParameter("serviceName", "%" + search.get("serviceName").trim() + "%");

        // ---- Get Total Number of Records ----
        long totalCount = countQuery.getSingleResult();

        // ---- Setting ResultDto ----
        ResultDto<ServiceResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(serviceResponses);

        log.info("Exit [ShipmentCustomRepository] with count = {}, pageResults = {}", totalCount, serviceResponses.size());
        return resultDto;
    }
}
