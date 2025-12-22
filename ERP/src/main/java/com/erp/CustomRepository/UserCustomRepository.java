package com.erp.CustomRepository;

import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.UserResponse;
import com.erp.Enum.Designation;
import com.erp.Mapper.User.UserMapper;
import com.erp.Model.User;
import com.erp.Repository.Inventory.InventoryRepositoryV2;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired private UserMapper userMapper;
    @Autowired private InventoryRepositoryV2 inventoryRepositoryV2;

    public ResultDto<UserResponse> filterUsers(FilterRequest filterRequest) {

        log.info("Into [UserCustomRepository] [filterUsers]");

        // -------- BASE QUERY --------
        StringBuilder jpql = new StringBuilder("SELECT u FROM User u WHERE 1=1");

        // -------- Collecting Data --------
        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> search = filterRequest.getSearchColumns();
        Map<String, String> orderby = filterRequest.getOrderByColumns();

        // -------- For Setting Base Query -------
        // ---- FILTERS ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("designation"))
                jpql.append(" AND u.designation = :designation");
        }

        // ---- ORDER BY ----
        if (orderby != null && !orderby.isEmpty())
        {
            jpql.append(" ORDER BY ");
            List<String> orderClause = new ArrayList<>();
            orderby.forEach((column, direction) ->
            {
                switch (column)
                {
                    case "createdAt" -> orderClause.add("u.createdAt " + ("desc".equalsIgnoreCase(direction) ? "DESC" : "ASC"));
                }
            });
            jpql.append(String.join(", ", orderClause));
        }


        // -------- CREATE REAL QUERY --------
        TypedQuery<User> query = entityManager.createQuery(jpql.toString(), User.class);

        // -------- For Setting Real Query --------
        // ---- Set Filter Params ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("designation"))
                query.setParameter("designation", Designation.valueOf(filters.get("designation")));
        }

        // -------- Getting Data According To Filter --------
        List<User> users = query.getResultList();

        // -------- COUNT QUERY --------
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(u) FROM User u WHERE 1=1");


        // ---- FILTER ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("designation"))
                countJpql.append(" AND u.designation = :designation");
        }

        // -------- CREATING REAL QUERY FOR COUNT --------
        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);

        // ---- Setting Real Query ----
        // ---- Filter ----
        if (filters != null && !filters.isEmpty()) {
            if (filters.containsKey("designation"))
                countQuery.setParameter("designation", Designation.valueOf(filters.get("designation")));
        }

        // ---- Get Total Number of Records ----
        long totalCount = countQuery.getSingleResult();

        // ---- Setting ResultDto ----
        List<UserResponse> res = new ArrayList<>();

        for(User user : users){
            UserResponse userResponse = userMapper.mapToUserResponse(user);
            userResponse.setBranchId(user.getBranch().getBranchId());
            userResponse.setFullName(user.getFirstName()+" "+user.getLastName());
            res.add(userResponse);
        }

        ResultDto<UserResponse> resultDto = new ResultDto<>();

        resultDto.setResults(res);
        resultDto.setCount(totalCount);

        return resultDto;
    }

}
