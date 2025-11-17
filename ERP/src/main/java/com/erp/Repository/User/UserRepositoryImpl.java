package com.erp.Repository.User;

import com.erp.Model.Admin;
import com.erp.Model.User;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Admin.AdminUserRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findByEmailWithSchema(String email, String schemaName) {
        String query = "SELECT * FROM " + schemaName + ".users WHERE email = :email LIMIT 1";

        try {
            User user = (User) entityManager.createNativeQuery(query, User.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return Optional.of(user);

        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }
}