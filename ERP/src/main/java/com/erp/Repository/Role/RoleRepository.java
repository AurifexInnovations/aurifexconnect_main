package com.erp.Repository.Role;

import com.erp.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role ,Long> {

    Optional<Role> findByRoleName(String roleName);
}
