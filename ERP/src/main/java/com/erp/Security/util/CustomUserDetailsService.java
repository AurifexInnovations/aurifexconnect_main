package com.erp.Security.util;

import com.erp.Model.Admin;
import com.erp.Model.GenericUser;
import com.erp.Model.Role;
import com.erp.Model.User;
import com.erp.Repository.Admin.AdminUserRepository;
import com.erp.Repository.Rootuser.RootUserRepository;
import com.erp.Repository.User.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RootUserRepository rootUserRepository;
    private final AdminUserRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<GenericUser> user = Stream.of(
                        rootUserRepository.findByEmail(email).map(GenericUser.class::cast),
                        adminRepository.findByEmail(email).map(GenericUser.class::cast),
                        userRepository.findByEmail(email).map(GenericUser.class::cast)
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        GenericUser genericUser = user.get();
        Set<SimpleGrantedAuthority> authorities;

        if (genericUser instanceof User) {
            Set<Role> roles = ((User) genericUser).getRoles();
            authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
                    .collect(Collectors.toSet());
        } else if (genericUser instanceof Admin) {
            authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else { // RootUser
            authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ROOT"));
        }

        return new org.springframework.security.core.userdetails.User(
                genericUser.getEmail(),
                genericUser.getPassword(),
                authorities
        );
    }
}