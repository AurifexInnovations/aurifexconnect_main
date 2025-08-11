package com.erp.Repository.Admin;

import com.erp.Model.Admin;
import java.util.Optional;

public interface AdminUserRepositoryCustom {
    Optional<Admin> findByEmailWithSchema(String email, String schemaName);
}