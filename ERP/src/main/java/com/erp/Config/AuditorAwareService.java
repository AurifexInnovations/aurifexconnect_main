package com.erp.Config;

import com.erp.Model.Admin;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Security.util.UserIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component("auditorAware")
public class AuditorAwareService implements AuditorAware<Admin> {

    private final UserIdentity userIdentity;
    private final AdminUserRepository userRepository;

    @Override
    public Optional<Admin> getCurrentAuditor() {
        //TODO: fetch user from userIdentity and then fetch user from userRepository
        /*return Optional.ofNullable(userIdentity.getCurrentUserEmail())
                .map(user -> userRepository.findByEmail(user))
                .orElse(null);

         */
        return null;
    }
}