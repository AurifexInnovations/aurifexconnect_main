package com.erp.Repository.Admin;

import com.erp.Model.Admin;
import com.erp.Model.User;
import com.erp.Multitenancy.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class AdminUserRepositoryImpl implements AdminUserRepositoryCustom {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Optional<Admin> findByEmailWithSchema(String email, String schemaName) {
//        log.debug("Current tenant before query: {}", TenantContext.getCurrentTenant());
//        String query = "SELECT * FROM " + schemaName + ".user_admin WHERE email = :email";
//        log.debug("Executing query: {}", query);
//        try {
//            return entityManager.createNativeQuery(query, Admin.class)
//                    .setParameter("email", email)
//                    .getResultStream()
//                    .findFirst();
//        } catch (Exception e) {
//            log.error("Error executing query for schema: {}", schemaName, e);
//            throw new RuntimeException("Failed to execute query", e);
//        }

        // Switch schema at connection level
        entityManager.unwrap(org.hibernate.Session.class).doWork(connection -> {
            connection.setSchema(schemaName);
        });

        // Build native SQL with schema injected directly
        String sql = "SELECT * FROM " + schemaName + ".user_admin WHERE email = :email AND is_active = true";

        Query query = entityManager
                .createNativeQuery(sql, Admin.class)
                .setParameter("email", email);

        Admin user = null;
        try {
            user = (Admin) query.getSingleResult();
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
}