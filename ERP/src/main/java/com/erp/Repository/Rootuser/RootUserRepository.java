package com.erp.Repository.Rootuser;

import com.erp.Model.GenericUser;
import com.erp.Model.RootUser;
import com.erp.Repository.GenericUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("rootUserRepository")
public interface RootUserRepository extends JpaRepository<RootUser, Long>, GenericUserRepository<RootUser> {

    Optional<RootUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
