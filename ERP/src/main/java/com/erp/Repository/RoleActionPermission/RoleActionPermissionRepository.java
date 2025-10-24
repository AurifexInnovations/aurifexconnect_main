package com.erp.Repository.RoleActionPermission;

import com.erp.Model.RolesActionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleActionPermissionRepository extends JpaRepository<RolesActionPermission , Long> {

    @Query(value = "select * from roles_action_permissions where active = true " , nativeQuery = true)
    List<RolesActionPermission> findByIds(List<Long> ids);

    @Query(value = "select * from roles_action_permissions where id =:id and active = true " , nativeQuery = true)
    RolesActionPermission getById(Long id);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE roles_action_permissions SET active = false WHERE id IN (:ids)",
            nativeQuery = true
    )    int deactivateRoleModulePermissionByIds(List<Long> ids);
}
