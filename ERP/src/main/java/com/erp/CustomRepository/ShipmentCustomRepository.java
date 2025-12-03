package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.ShipmentDetailsResponseDTO;
import com.erp.Enum.ShipmentReferenceType;
import com.erp.Enum.ShipmentStatus;
import com.erp.Mapper.shipment.ShipmentMapper;
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

@Repository
@Slf4j
public class ShipmentCustomRepository
{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ShipmentMapper shipmentMapper;

    public ResultDto<ShipmentDetailsResponseDTO> getShipmentsPagination(FilterRequest filterRequest)
    {
        log.info("Into [ShipmentCustomRepository] [getShipmentsPagination]");

        // -------- BASE QUERY --------
        StringBuilder jpql = new StringBuilder("SELECT s FROM ShipmentDetails s WHERE 1=1");

        // -------- Collecting Data --------
        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderby = filterRequest.getOrderByColumns();

        // -------- For Setting Base Query -------
        // ---- FILTERS ----
        if (filters != null && !filters.isEmpty())
        {
            if (filters.containsKey("shipmentStatus"))
                jpql.append(" AND s.shipmentStatus = :shipmentStatus");

            if (filters.containsKey("referenceType"))
                jpql.append(" AND s.referenceType = :referenceType");

            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
                jpql.append(" AND s.shipmentDate BETWEEN :startDate AND :endDate");
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("cName"))
            jpql.append(" AND LOWER(s.carrierName) LIKE LOWER(:cName)");

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
                }
            });
            jpql.append(String.join(", ", orderClause));
        }


        // -------- CREATE REAL QUERY --------
        TypedQuery<ShipmentDetails> query = entityManager.createQuery(jpql.toString(), ShipmentDetails.class);

        // -------- For Setting Real Query --------
        // ---- Set Filter Params ----
        if (filters != null && !filters.isEmpty())
        {
            if (filters.containsKey("shipmentStatus"))
                query.setParameter("shipmentStatus", ShipmentStatus.valueOf(filters.get("shipmentStatus")));

            if (filters.containsKey("referenceType"))
                query.setParameter("referenceType", ShipmentReferenceType.valueOf(filters.get("referenceType")));

            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
            {
                query.setParameter("startDate", LocalDateTime.parse(filters.get("startDate")));
                query.setParameter("endDate", LocalDateTime.parse(filters.get("endDate")));
            }
        }

        // ---- Set Search Params ----
        if (search != null && search.containsKey("cName"))
            query.setParameter("cName", "%" + search.get("cName").trim() + "%");

        // ---- Pagination ----
        if (filterRequest.getPaginationRequest() != null)
        {
            int pageNumber = filterRequest.getPaginationRequest().getPageNumber();
            int pageSize = filterRequest.getPaginationRequest().getPageSize();
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
        }


        // -------- Getting Data According To Filter --------
        List<ShipmentDetails> shipments = query.getResultList();

        // -------- COUNT QUERY --------
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(s) FROM ShipmentDetails s WHERE 1=1");

        // ---- FILTER ----
        if (filters != null && !filters.isEmpty())
        {
            if (filters.containsKey("shipmentStatus"))
                countJpql.append(" AND s.shipmentStatus = :shipmentStatus");

            if (filters.containsKey("referenceType"))
                countJpql.append(" AND s.referenceType = :referenceType");

            if (filters.containsKey("startDate") && filters.containsKey("endDate"))
                countJpql.append(" AND s.shipmentDate BETWEEN :startDate AND :endDate");
        }

        // ---- SEARCH ----
        if (search != null && search.containsKey("cName"))
            countJpql.append(" AND LOWER(s.carrierName) LIKE LOWER(:cName)");


        // -------- CREATING REAL QUERY FOR COUNT --------
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);

        // ---- Setting Real Query ----
        // ---- Filter ----
        if (filters != null && !filters.isEmpty())
        {
            if (filters.containsKey("shipmentStatus"))
                countQuery.setParameter("shipmentStatus", ShipmentStatus.valueOf(filters.get("shipmentStatus")));

            if (filters.containsKey("referenceType"))
                countQuery.setParameter("referenceType", ShipmentReferenceType.valueOf(filters.get("referenceType")));

            if (filters.containsKey("startDate") && filters.containsKey("endDate")) {

                countQuery.setParameter("startDate", LocalDateTime.parse(filters.get("startDate")));
                countQuery.setParameter("endDate", LocalDateTime.parse(filters.get("endDate")));
            }
        }

        // ---- Search ----
        if (search != null && search.containsKey("cName"))
            countQuery.setParameter("cName", "%" + search.get("cName").trim() + "%");


        // ---- Get Total Number of Records ----
        long totalCount = countQuery.getSingleResult();

        // ---- Setting ResultDto ----
        List<ShipmentDetailsResponseDTO> res = shipmentMapper.toList(shipments);

        ResultDto<ShipmentDetailsResponseDTO> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(res);

        log.info("Exit [ShipmentCustomRepository] with count = {}, pageResults = {}", totalCount, shipments.size());
        return resultDto;
    }
}
