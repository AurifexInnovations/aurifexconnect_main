package com.erp.Meta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MetaAdminRepository extends JpaRepository<MetaAdmin, Long> {

    Optional<MetaAdmin> findByAdminEmail(String email);

    boolean existsByAdminEmail(String email);

    boolean existsBySchemaName(String schemaName);

    @Query("SELECT m.schemaName FROM MetaAdmin m WHERE m.adminEmail = :email")
    Optional<String> findSchemaNameByAdminEmail(@Param("email") String email);

    @Query("SELECT m.adminEmail FROM MetaAdmin m WHERE m.schemaName = :schemaName")
    Optional<String> findAdminEmailBySchemaName(String schemaName);

}
