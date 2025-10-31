package com.erp.Meta;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MetaUserRepository {

    Optional<MetaAdmin> findByUserEmail(String email);

    boolean existsByUserEmail(String email);

    @Query("SELECT m.schemaName FROM MetaUser m WHERE m.userEmail = :email")
    Optional<String> findSchemaNameByUserEmail(@Param("email") String email);
}
