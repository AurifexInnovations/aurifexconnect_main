package com.erp.Repository.Admin;

import com.erp.Model.Admin;
import com.erp.Repository.GenericUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("adminUserRepository")
public interface AdminUserRepository extends JpaRepository<Admin, Long>, GenericUserRepository<Admin>,AdminUserRepositoryCustom {
    Optional<Admin> findByEmail(String email);

    /**
     * Retrieves an Admin by ID or name.
     *
     * @param id   the ID of the admin
     * @param name the name of the admin
     * @return an Optional containing the matching Admin, if found
     */
    Optional<Admin> findByIdOrNameAndIsActiveTrue(Long id, String name);

    List<Admin> findByIsActiveTrue();

    boolean existsByEmail(String email);
}
