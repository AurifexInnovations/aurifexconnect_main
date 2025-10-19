package com.erp.Repository.Service.Entitymanager;

import com.erp.Dto.Request.ServiceTypeGetRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ServiceTypeResponse;
import com.erp.Exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ServiceTypeRepo {

    @PersistenceContext
    private final EntityManager entityManager;

    private static final Map<String, String> COLUMNS = Map.of(
            "serviceId", "s.service_id",
            "serviceName", "s.service_name",
            "serviceDescription", "s.service_description",
            "servicePrice", "s.service_price",
            "serviceStatus", "s.service_status",
            "serviceCategory", "s.service_category",
            "createdAt", "s.created_at",
            "lastModifiedAt", "s.last_modified_at"
    );


    public ResultDto<ServiceTypeResponse> getAllServices(ServiceTypeGetRequest request) {

        ResultDto<ServiceTypeResponse> resultDto = new ResultDto<>();

          try {
              String selectQuery = "SELECT s.service_id, s.service_name, s.service_description, s.service_price, " +
                      "s.service_status, s.service_category, s.created_at, s.last_modified_at ";


              String fromQuery = "FROM service s WHERE 1=1 ";


              StringBuilder where = new StringBuilder();
              if (request.getServiceName() != null && !request.getServiceName().isBlank()) {
                  where.append(" AND LOWER(s.service_name) LIKE '%")
                          .append(request.getServiceName().toLowerCase()).append("%'");
              }

              if (request.getServiceStatus() != null) {
                  where.append(" AND s.service_status = '").append(request.getServiceStatus().name()).append("'");
              }

              if (request.getServiceCategory() != null) {
                  where.append(" AND s.service_category = '").append(request.getServiceCategory().name()).append("'");
              }

              if (request.getServicePrice() != null) {
                  where.append(" AND s.service_price = ").append(request.getServicePrice());
              }

              if (request.getDateRequest() != null &&
                      request.getDateRequest().getStartDate() != null &&
                      request.getDateRequest().getEndDate() != null) {
                  where.append(" AND s.created_at BETWEEN '")
                          .append(request.getDateRequest().getStartDate()).append("' AND '")
                          .append(request.getDateRequest().getEndDate()).append("'");
              }

              Map<String, String> filters = request.getSearchFilters();

              if (filters != null && !filters.isEmpty()) {
                  for (Map.Entry<String, String> entry : filters.entrySet()) {
                      String field = entry.getKey();
                      String value = entry.getValue();

                      String column = COLUMNS.get(field); // get actual column name
                      if (column != null && value != null && !value.isEmpty()) {
                          where.append(" AND ").append(column).append(" ILIKE '%")
                                  .append(value.replace("'", "''")).append("%' ");
                      }
                  }
              }

              // --- Count Query ---
              String countQueryStr = "SELECT COUNT(*) " + fromQuery + where;
              log.info("Executing Count Query: {}", countQueryStr);
              Query countQuery = entityManager.createNativeQuery(countQueryStr);
              long totalCount = ((Number) countQuery.getSingleResult()).longValue();


              StringBuilder orderBy = new StringBuilder(" ORDER BY s.created_at ASC "); // default

              if (request.getOrderByRequest() != null &&
                      request.getOrderByRequest().getSortColumn() != null &&
                      request.getOrderByRequest().getSortCriteria() != null) {

                  String sortColumn = request.getOrderByRequest().getSortColumn();
                  String sortCriteria = request.getOrderByRequest().getSortCriteria().name();

                  String dbColumn = COLUMNS.getOrDefault(sortColumn, "s.created_at");

                  orderBy = new StringBuilder(" ORDER BY ")
                          .append(dbColumn)
                          .append(" ")
                          .append(sortCriteria);
              }

              int pageNumber = request.getPaginationRequest() != null && request.getPaginationRequest().getPageNumber() != null
                      ? request.getPaginationRequest().getPageNumber()
                      : 0;

              int pageSize = request.getPaginationRequest() != null && request.getPaginationRequest().getPageSize() != null
                      ? request.getPaginationRequest().getPageSize()
                      : 10;

              int offset = pageNumber * pageSize;
              int limit = pageSize;

              String pagination = " LIMIT " + limit + " OFFSET " + offset;

              String finalQuery = selectQuery + fromQuery + where + orderBy + pagination;
              log.info("Executing Data Query: {}", finalQuery);

              Query dataQuery = entityManager.createNativeQuery(finalQuery, "ServiceTypeResponseMapping");
              List<ServiceTypeResponse> results = dataQuery.getResultList();


              resultDto.setCount(totalCount);
              resultDto.setResults(results);
          } catch (Exception e) {
              throw new ResourceNotFoundException(e.getMessage());
          }
        return resultDto;
    }
}
