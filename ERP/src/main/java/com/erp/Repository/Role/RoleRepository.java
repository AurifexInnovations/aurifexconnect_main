package com.erp.Repository.Role;

import com.erp.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role ,Long> {

    Optional<Role> findByRoleName(String roleName);

    @Query(value = "select role_id from roles where role_name in (:roleNames) " , nativeQuery = true)
    List<Long> findIdByRoleNames(List<String> roleNames);
}
