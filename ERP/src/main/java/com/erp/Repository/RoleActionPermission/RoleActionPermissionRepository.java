package com.erp.Repository.RoleActionPermission;

import com.erp.Model.Action;
import com.erp.Model.Module;
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


    @Query("select *  from modules  where id =:moduleId and active = true")
    Module findModuleId(@Param("moduleId") Long moduleId);

    @Query("select *  from actions where id =:actionId and active = true")
    Action findActionId(@Param("actionId") Long actionId);
}
