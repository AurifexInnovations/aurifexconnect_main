package com.erp.Service.RootUser;

import com.erp.Model.GenericUser;
import com.erp.Repository.Rootuser.RootUserRepository;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final RootUserRepository rootUserRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String currentTenant = TenantContext.getCurrentTenant();
        log.debug("🔍 TenantContext.getCurrentTenant(): {}", currentTenant);

        GenericUser user;

        if ("public".equals(currentTenant)) {
            log.debug("🔐 Looking up root user in public schema...");
            user = rootUserRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Root user not found: " + email));
        } else {
            log.debug("👥 Looking up tenant user in schema: {}", currentTenant);
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Tenant user not found: " + email));
        }

        log.info("✅ Loaded user: {} with authorities: {} in schema: {}",
                user.getEmail(), user.getAuthorities(), currentTenant);

        return user;
    }
}
