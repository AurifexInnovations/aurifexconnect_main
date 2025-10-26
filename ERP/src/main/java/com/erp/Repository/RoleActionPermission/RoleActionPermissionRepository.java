package com.erp.Repository.RoleActionPermission;

import com.erp.Model.RolesActionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoleActionPermissionRepository extends JpaRepository<RolesActionPermission , Long> {

    @Query(value = "select * from roles_action_permissions where active = true " , nativeQuery = true)
    List<RolesActionPermission> findByIds(List<Long> ids);

    @Query(value = "select * from roles_action_permissions where id =:id and active = true " , nativeQuery = true)
    RolesActionPermission getById(Long id);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE roles_action_permissions SET active = false WHERE id IN (:ids) and active = true ",
            nativeQuery = true
    )    int deactivateRoleModulePermissionByIds(List<Long> ids);



    @Query("SELECT rap FROM RolesActionPermission rap " +
            "WHERE rap.roleId = :roleId AND rap.moduleId = :moduleId AND rap.actionId = :actionId")
    Optional<RolesActionPermission> findByRoleIdAndModuleIdAndActionId(Long roleId, Long moduleId, Long actionId);


    @Query("SELECT m.id FROM Module m WHERE m.name = :moduleName")
    Optional<Long> findModuleIdByName(@Param("moduleName") String moduleName);

    @Query("SELECT a.id FROM Action a WHERE a.name = :actionName")
    Optional<Long> findActionIdByName(@Param("actionName") String actionName);

    @Query(value = "" +
            " select rap from roles_action_permissions as rap " +
            " inner join roles as r " +
            " on rap.role_id = r.role_id  " +
            " where rap.active = true " +
            " and r.role_name = :roleName " , nativeQuery = true)
    List<RolesActionPermission> findByRoleName(String roleName);


}
