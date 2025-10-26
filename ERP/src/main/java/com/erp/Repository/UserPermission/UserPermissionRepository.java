package com.erp.Repository.UserPermission;

import com.erp.Model.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    @Query("SELECT up FROM UserPermission up WHERE up.userId = :userId and " )
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
}
