package com.erp.Repository.User;

import com.erp.Model.Admin;
import com.erp.Model.User;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Admin.AdminUserRepositoryCustom;
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
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmailWithSchema(String email, String schemaName) {
        // Switch schema at connection level
        entityManager.unwrap(org.hibernate.Session.class).doWork(connection -> {
            connection.setSchema(schemaName);
        });

        // Build native SQL with schema injected directly
        String sql = "SELECT * FROM " + schemaName + ".users WHERE email = :email";

        Query query = entityManager
                .createNativeQuery(sql, User.class)
                .setParameter("email", email);

        User user = null;
        try {
            user = (User) query.getSingleResult();
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
}