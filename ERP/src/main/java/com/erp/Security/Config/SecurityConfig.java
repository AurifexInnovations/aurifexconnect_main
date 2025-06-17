package com.erp.Security.Config;

import com.erp.Config.AppEnv;
import com.erp.Model.GenericUser;
import com.erp.Security.Filter.AuthFilter;
import com.erp.Security.Filter.RefreshAuthFilter;
import com.erp.Security.Filter.TokenBlackListService;
import com.erp.Security.JWT.JWTService;
import com.erp.Security.util.CookieManager;
import com.erp.Security.util.UserRepositoryRegistry;
import com.erp.Service.Auth.AuthService;
import com.erp.Service.Auth.GenericAuthServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AppEnv env;
    private final JWTService jwtService;
    private final TokenBlackListService tokenBlackListService;
    private final UserRepositoryRegistry userRepositoryRegistry;
    private final CookieManager cookieManager;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            log.debug("Loading user by email: {}", username);
            return userRepositoryRegistry.findUserByEmail(username)
                    .orElseThrow(() -> {
                        log.error("User not found: {}", username);
                        return new UsernameNotFoundException("User not found: " + username);
                    });
        };
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(DaoAuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    AuthService authService(
            AuthenticationManager authenticationManager,
            UserRepositoryRegistry userRepositoryRegistry,
            TokenBlackListService tokenBlackListService,
            JWTService jwtService) {
        return new GenericAuthServiceImpl(
                authenticationManager,
                userRepositoryRegistry,
                tokenBlackListService,
                jwtService,
                cookieManager);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return hierarchy;
    }

    @Bean
    @Order(1)
    SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        String baseUrl = env.getBaseUrl();
        log.info("Configuring public filter chain for {}", baseUrl + "/auth/**");
        return http
                .securityMatcher(baseUrl + "/auth/**", baseUrl + "/login")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(baseUrl + "/auth/register/**", baseUrl + "/login").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous(anonymous -> anonymous.principal("anonymousUser").authorities("ROLE_ANONYMOUS"))
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain refreshSecurityFilterChain(HttpSecurity http) throws Exception {
        String baseUrl = env.getBaseUrl();
        log.info("Configuring refresh filter chain for {}", baseUrl + "/refresh-login/**");
        return http
                .securityMatcher(baseUrl + "/refresh-login/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(baseUrl + "/refresh-login/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new RefreshAuthFilter(jwtService, tokenBlackListService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String baseUrl = env.getBaseUrl();
        log.info("Configuring default filter chain for {}", baseUrl + "/**");
        return http
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(baseUrl + "/admins/**").hasAnyAuthority("ROLE_ROOT")
                        .requestMatchers(baseUrl+"/roles/**").hasAnyAuthority("ROLE_ROOT","ROLE_ADMIN")
                        .requestMatchers(baseUrl+"/user",baseUrl+"/user/delete/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(baseUrl+"/user/update/**").hasRole("EMPLOYEE")
                        .requestMatchers(baseUrl + "/logout").authenticated()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new AuthFilter(jwtService, tokenBlackListService,userRepositoryRegistry
                ), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // allow cookies / JWT via headers
        config.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:5174"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // typical REST methods
        config.setAllowedHeaders(List.of("*")); // allow all headers
        config.setExposedHeaders(List.of("Authorization")); // expose the Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
