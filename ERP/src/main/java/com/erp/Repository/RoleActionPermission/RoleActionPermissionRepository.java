package com.erp.Repository.RoleActionPermission;

import com.erp.Model.RolesActionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleActionPermissionRepository extends JpaRepository<RolesActionPermission , Long> {

    @Query(value = "select * from roles_action_permissions where active = true " , nativeQuery = true)
    List<RolesActionPermission> findByIds(List<Long> ids);

    @Query(value = "select * from roles_action_permissions where id =:id and active = true " , nativeQuery = true)
    RolesActionPermission getById(Long id);

    @Query(value =
            "    select rap.id , rap.role_id,r.role_name , rap.module_id ,m.name, rap.action_id , a.name, " +
            "    from roles_action_permissions as rap " +
            "    inner join modules as m on m.id = rap.module_id and m.active=true " +
            "    inner join actions as a on a.id = rap.action_id and a.active=true " +
            "    inner join roles as r on r.role_id = rap.role_id " +
            "    where rap.active = true;" , nativeQuery = true)
    List<?> getAllRoleModuleActions();
}
