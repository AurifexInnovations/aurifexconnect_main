package com.erp.Repository.UserPermission;

import com.erp.Model.UserPermission;
import com.erp.Projection.RoleModuleActionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    @Query("SELECT up FROM UserPermission up WHERE up.userId = :userId ")
    List<UserPermission> findByUserId(Long userId);


    @Query("SELECT CASE WHEN COUNT(up) > 0 THEN true ELSE false END " +
            "FROM UserPermission up " +
            "JOIN RolesActionPermission rap ON up.roleActionPermission = rap.id " +
            "WHERE up.userId = :userId " +
            "AND rap.moduleId = :moduleId " +
            "AND rap.actionId = :actionId " +
            "AND rap.active = true")
    boolean hasUserPermission(@Param("userId") Long userId,
                              @Param("moduleId") Long moduleId,
                              @Param("actionId") Long actionId);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE user_permissions SET active = false WHERE id IN (:ids) AND active = true",
            nativeQuery = true
    )
    int deactiveUserPermissionByIds(@Param("ids") List<Long> ids);


    @Query(value = """
    SELECT 
        up.id AS userPermissionId,
        rpm.id AS roleModulePermissionId,
        r.id AS roleId,
        r.role_name AS roleName,
        m.id AS moduleId,
        m.module_name AS moduleName,
        a.id AS actionId,
        a.action_name AS actionName
    FROM user_permissions up
    JOIN role_module_permission rpm ON up.role_action_id = rpm.id
    JOIN roles r ON rpm.role_id = r.id
    JOIN modules m ON rpm.module_id = m.id
    JOIN actions a ON rpm.action_id = a.id
    WHERE up.user_id = :userId
""", nativeQuery = true)
    List<RoleModuleActionProjection> findRoleModuleActionPermissionsByUserId(@Param("userId") Long userId);

}
