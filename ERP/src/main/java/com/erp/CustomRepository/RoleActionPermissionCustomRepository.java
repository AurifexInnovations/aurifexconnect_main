package com.erp.CustomRepository;

import com.erp.Dto.Request.ActionDto;
import com.erp.Dto.Request.FilterRequest;
import com.erp.Dto.Request.ModuleDto;
import com.erp.Dto.Response.ResultDto;
import com.erp.Dto.Response.RoleModleActionPermisisonResponse;
import com.erp.Dto.Response.RoleResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class RoleActionPermissionCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultDto<RoleModleActionPermisisonResponse> getFilteredRoleModuleAction(FilterRequest filterRequest) {
        log.info("Into [RoleActionPermissionCustomRepository] [getFilteredRoleModuleAction]");

        // ---------- BASE DATA QUERY ----------
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    rap.id,
                    rap.role_id,
                    r.role_name,
                    rap.module_id,
                    m.name AS module_name,
                    rap.action_id,
                    a.name AS action_name,
                    rap.active
                FROM roles_action_permissions rap
                INNER JOIN roles r ON rap.role_id = r.role_id
                INNER JOIN modules m ON rap.module_id = m.id
                INNER JOIN actions a ON rap.action_id = a.id
                WHERE rap.active = TRUE
                """);

        StringBuilder countSql = new StringBuilder("""
                SELECT COUNT(*)
                FROM roles_action_permissions rap
                INNER JOIN roles r ON rap.role_id = r.role_id
                INNER JOIN modules m ON rap.module_id = m.id
                INNER JOIN actions a ON rap.action_id = a.id
                WHERE rap.active = TRUE
                """);

        Map<String, String> filters = filterRequest.getFilterColumns();
        Map<String, String> orderBy = filterRequest.getOrderByColumns();

        // ---------- FILTER CONDITIONS ----------
        if (filters != null) {
            if (filters.containsKey("roleId") && !filters.get("roleId").isEmpty()) {
                sql.append(" AND rap.role_id = :roleId");
                countSql.append(" AND rap.role_id = :roleId");
            }
            if (filters.containsKey("moduleId") && !filters.get("moduleId").isEmpty()) {
                sql.append(" AND rap.module_id = :moduleId");
                countSql.append(" AND rap.module_id = :moduleId");
            }
            if (filters.containsKey("actionId") && !filters.get("actionId").isEmpty()) {
                sql.append(" AND rap.action_id = :actionId");
                countSql.append(" AND rap.action_id = :actionId");
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
                    case "roleName" -> orderClauses.add("r.role_name " + direction);
                    case "moduleName" -> orderClauses.add("m.name " + direction);
                    case "actionName" -> orderClauses.add("a.name " + direction);
                }
            }
            sql.append(String.join(", ", orderClauses));
        } else {
            sql.append(" ORDER BY rap.id DESC");
        }

        log.info("[RoleActionPermissionCustomRepository] Query: {}", sql);

        Query dataQuery = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        bindParameters(filters, dataQuery, countQuery);

        // ---------- PAGINATION ----------
        if (filterRequest.getPaginationRequest() != null) {
            int page = filterRequest.getPaginationRequest().getPageNumber();
            int size = filterRequest.getPaginationRequest().getPageSize();
            dataQuery.setFirstResult(page * size);
            dataQuery.setMaxResults(size);
        }

        long totalCount = ((Number) countQuery.getSingleResult()).longValue();
        List<Object[]> rows = dataQuery.getResultList();

        // ---------- MAP RESULTS ----------
        List<RoleModleActionPermisisonResponse> results = new ArrayList<>();

        for (Object[] row : rows) {
            RoleModleActionPermisisonResponse dto = new RoleModleActionPermisisonResponse();
            dto.setRoleModulePermissionId(((Number) row[0]).longValue());

            RoleResponse roleResponse  = new RoleResponse();
            roleResponse.setRoleId(((Number) row[1]).longValue());
            roleResponse.setRoleName((String) row[2]);
            dto.setRole(roleResponse);

            ModuleDto moduleDto = new ModuleDto();
            moduleDto.setId(((Number) row[3]).longValue());
            moduleDto.setName((String) row[4]);
            dto.setModule(moduleDto);

            ActionDto actionDto = new ActionDto();
            actionDto.setId(((Number) row[5]).longValue());
            actionDto.setName((String) row[6]);
            dto.setAction(actionDto);

            results.add(dto);
        }

        ResultDto<RoleModleActionPermisisonResponse> resultDto = new ResultDto<>();
        resultDto.setCount(totalCount);
        resultDto.setResults(results);

        log.info("Exit [RoleActionPermissionCustomRepository] with count = {}", totalCount);
        return resultDto;
    }

    private void bindParameters(Map<String, String> filters,
                                Query dataQuery, Query countQuery) {
        if (filters != null) {
            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "roleId", "moduleId", "actionId" -> {
                            dataQuery.setParameter(key, Long.parseLong(value));
                            countQuery.setParameter(key, Long.parseLong(value));
                        }
                    }
                }
            });
        }
    }
}
