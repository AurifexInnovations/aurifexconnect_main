package com.erp.Security.util;

import com.erp.Meta.MetaAdmin;
import com.erp.Model.GenericUser;
import com.erp.Model.Role;
import com.erp.Model.User;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Rootuser.RootUserRepository;
import com.erp.Repository.User.UserRepository;
import com.erp.Meta.MetaAdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserIdentity
{
    private final RootUserRepository rootUserRepository;
    private final AdminUserRepository adminRepository;
    private final UserRepository userRepository;
    private final MetaAdminRepository metaAdminRepository;

    public Authentication getAuthentication()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
        {
            throw new IllegalStateException("No authentication context available");
        }
        return authentication;
    }

    public String getCurrentUserEmail()
    {
        Authentication authentication = getAuthentication();
        if (!authentication.isAuthenticated())
        {
            throw new IllegalStateException("No authenticated user found");
        }
        String email = authentication.getName();
        if (email == null || email.isEmpty())
        {
            throw new IllegalStateException("User email is null or empty");
        }
        return email;
    }

    public GenericUser getCurrentUser() throws UsernameNotFoundException
    {
        String email = getCurrentUserEmail();

        // Check RootUser (public schema)
        Optional<GenericUser> rootUser = rootUserRepository.findByEmail(email)
                .map(GenericUser.class::cast);
        if (rootUser.isPresent())
        {
            return rootUser.get();
        }

        // Check Admin (tenant schema)
        Optional<String> schemaName = metaAdminRepository.findByAdminEmail(email)
                .map(MetaAdmin::getSchemaName);
        if (schemaName.isPresent())
        {
            TenantContext.setCurrentTenant(schemaName.get());
            try
            {
                Optional<GenericUser> admin = adminRepository.findByEmail(email)
                        .map(GenericUser.class::cast);
                if (admin.isPresent())
                {
                    return admin.get();
                }
            }
            finally
            {
                TenantContext.clear();
            }
        }

        // Check User (tenant schema)
        // Assume User is linked to an admin's schema
        Optional<GenericUser> user = metaAdminRepository.findAll().stream()
                .map(MetaAdmin::getSchemaName)
                .map(schema -> {
                    TenantContext.setCurrentTenant(schema);
                    try
                    {
                        return userRepository.findByEmail(email)
                                .map(GenericUser.class::cast)
                                .orElse(null);
                    } finally
                    {
                        TenantContext.clear();
                    }
                })
                .filter(Objects::nonNull)
                .findFirst();
        if (user.isPresent()) {
            return user.get();
        }

        throw new UsernameNotFoundException("Invalid User: " + email);
    }

    public String getCurrentUsername() {
        GenericUser user = getCurrentUser();   // already resolves correct user
        String username = user.getUsername();  // or user.getUsernameField() if custom
        if (username == null || username.isEmpty()) {
            throw new IllegalStateException("Username is null or empty");
        }
        return username;
    }


    public Set<Role> getCurrentUserRoles() throws UsernameNotFoundException {
        GenericUser user = getCurrentUser();
        if (user instanceof User) {
            return ((User) user).getRoles();
        }
        return Collections.emptySet();
    }

    public void validateOwnership(String ownerEmail) throws IllegalAccessException {
        String currentUserEmail = getCurrentUserEmail();
        if (!currentUserEmail.equals(ownerEmail)) {
            throw new IllegalAccessException(
                    String.format("User %s not allowed to access or modify resource owned by %s",
                            currentUserEmail, ownerEmail));
        }
    }
}