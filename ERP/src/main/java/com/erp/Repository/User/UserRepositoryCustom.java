package com.erp.Repository.User;

import com.erp.Model.Admin;
import com.erp.Model.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findByEmailWithSchema(String email, String schemaName);
}