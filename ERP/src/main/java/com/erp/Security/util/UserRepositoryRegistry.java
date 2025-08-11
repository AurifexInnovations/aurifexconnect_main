package com.erp.Security.util;

import com.erp.Model.Admin;
import com.erp.Model.GenericUser;
import com.erp.Model.RootUser;
import com.erp.Model.User;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.GenericUserRepository;
import com.erp.Repository.Rootuser.RootUserRepository;
import com.erp.Repository.User.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class UserRepositoryRegistry {

    private final RootUserRepository rootUserRepository;
    private final AdminUserRepository adminUserRepository;
    private final UserRepository userRepository;

    public Optional<GenericUser> findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            log.warn("Email is null or empty. Cannot proceed with user lookup.");
            return Optional.empty();
        }

        log.debug("Searching for user with email: {}", email);
        String tenantId = TenantContext.getCurrentTenant();
        log.debug("Current tenant: {}", tenantId);

        return findRootUserByEmail(email)
                .or(() -> findAdminByEmail(email, tenantId))
                .or(() -> findUserByEmailInTenant(email));
    }

    private Optional<GenericUser> findRootUserByEmail(String email) {
        Optional<RootUser> rootUser = rootUserRepository.findByEmail(email);
        if (rootUser.isPresent()) {
            log.debug("Found RootUser with email: {}", email);
        }
        return rootUser.map(user -> user);
    }

    private Optional<GenericUser> findAdminByEmail(String email, String tenantId) {
        if (tenantId == null || tenantId.equals("public")) {
            log.warn("Tenant is null or 'public'; skipping Admin lookup for email: {}", email);
            return Optional.empty();
        }

        Optional<Admin> admin = adminUserRepository.findByEmailWithSchema(email, tenantId);
        if (admin.isPresent()) {
            log.debug("Found Admin with email: {} in tenant: {}", email, tenantId);
        }
        return admin.map(user -> user);
    }

    private Optional<GenericUser> findUserByEmailInTenant(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.debug("Found User with email: {} in tenant: {}", email, TenantContext.getCurrentTenant());
        }
        return user.map(userObj -> userObj);
    }

    @SuppressWarnings("unchecked")
    public <T extends GenericUser> GenericUserRepository<T> getRepositoryForUser(GenericUser user) {
        if (user instanceof RootUser) {
            log.debug("Returning RootUserRepository for user: {}", user.getEmail());
            return (GenericUserRepository<T>) rootUserRepository;
        } else if (user instanceof Admin) {
            log.debug("Returning AdminUserRepository for user: {}", user.getEmail());
            return (GenericUserRepository<T>) adminUserRepository;
        } else if (user instanceof User) {
            log.debug("Returning UserRepository for user: {}", user.getEmail());
            return (GenericUserRepository<T>) userRepository;
        }

        log.error("Unknown user type: {}", user.getClass().getName());
        throw new IllegalArgumentException("Unknown user type: " + user.getClass().getName());
    }
}
