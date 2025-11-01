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

    @Modifying
    @Transactional
    @Query(
            value = "update user_permissions   set active = false where role_action_id   in ( " +
                    "select up.role_action_id  from user_permissions  as up " +
                    "inner join   roles_action_permissions as rap  on rap.id = up.role_action_id and rap.role_id =:roleId " +
                    " and up.user_id =:userId and rap.active = true and up.active = true) ",
            nativeQuery = true
    )
    int deactiveUserPermissionByRoleId(@Param("roleId") Long roleId,@Param("userId") Long userId);


    @Query(value = """
            SELECT 
                up.id AS userPermissionId,
                rpm.id AS roleModulePermissionId,
                r.role_id AS roleId,
                r.role_name AS roleName,
                m.id AS moduleId,
                m.name AS moduleName,
                a.id AS actionId,
                a.name AS actionName,a.description as description
            FROM user_permissions up
            JOIN roles_action_permissions rpm ON up.role_action_id = rpm.id
            JOIN roles r ON rpm.role_id = r.role_id
            JOIN modules m ON rpm.module_id = m.id
            JOIN actions a ON rpm.action_id = a.id
            WHERE up.user_id =:userId 
""", nativeQuery = true)
    List<RoleModuleActionProjection> findRoleModuleActionPermissionsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_permissions up " +
            "SET active = false " +
            "FROM roles_action_permissions rap " +
            "WHERE up.role_action_id = rap.id " +
            "  AND rap.role_id = :roleId           " +
            "  AND up.user_id = :userId           " +
            "  AND rap.active = false " +
            "  AND up.active = true; " , nativeQuery = true)
    void updateUserPermissionByUserRoleIdAndRoleId(long roleId , long userId);

    @Modifying
    @Transactional
    @Query(value =
            "INSERT INTO user_permissions " +
            "    (user_id, role_action_id, created_by, active, created_at) " +
            "SELECT  " +
            "    :userId , " +
            "    rap.id, " +
            "    :createdBY ,  " +
            "    true, " +
            "    NOW() " +
            "FROM roles_action_permissions rap\n" +
            "WHERE rap.role_id = :roleId " +
            "  AND rap.active = true " +
            "  AND rap.id NOT IN ( " +
            "      SELECT up.role_action_id" +
            "      FROM user_permissions up " +
            "      WHERE up.user_id = :userId " +
            "       AND up.active = true " +
            "  ) " +
            " ON CONFLICT (user_id, role_action_id) DO NOTHING; " , nativeQuery = true)
    void insertUserPermission(long roleId , long userId , long createdBY);
}
