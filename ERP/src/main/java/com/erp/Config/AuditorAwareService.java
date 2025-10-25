package com.erp.Config;

import com.erp.Model.Admin;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Security.util.UserIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component("auditorAware")
public class AuditorAwareService implements AuditorAware<Admin> {

    private final UserIdentity userIdentity;
    private final AdminUserRepository userRepository;

    @Override
    public Optional<Admin> getCurrentAuditor() {
//        String email = null;
//
//        try {
//            email = userIdentity.getCurrentUserEmail();
//        } catch (Exception ignored) {
//        }
//
//        if (email == null || email.isBlank()) {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
//                email = auth.getName();
//            }
//        }
//
//        if (email == null || email.isBlank()) {
//            return Optional.empty();
//        }
//
//        // return repository result directly; consider providing a "system" fallback user if you always need a non-empty auditor
//        return userRepository.findByEmail(email);
        return null;
    }
}