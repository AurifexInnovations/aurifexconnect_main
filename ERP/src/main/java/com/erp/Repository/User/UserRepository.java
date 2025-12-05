package com.erp.Repository.User;


import com.erp.Model.User;
import com.erp.Repository.GenericUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>, GenericUserRepository<User> ,UserRepositoryCustom {
    Optional<User> findByEmail(String email);

    List<User> findByIsActiveTrue();

    User findByIdAndIsActiveTrue(Long id);

    Optional<User> findByIdOrFirstNameAndIsActiveTrue(Long id, String name);

    long countByIsActiveTrueAndRoles_RoleName(String roleName);

    long countByIsActiveTrueAndRoles_RoleNameAndIdNot(String roleName, Long userId);

    List<User> findByIsManagerTrueAndIsActiveTrueAndBranch_BranchId(Long branchId);

    List<User> findByBranch_BranchId(long branchId);
}

