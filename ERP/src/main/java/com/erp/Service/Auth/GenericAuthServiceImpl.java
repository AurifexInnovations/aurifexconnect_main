package com.erp.Service.Auth;

import com.erp.Dto.Request.AuthRecord;
import com.erp.Dto.Request.LoginRequest;
import com.erp.Exception.User.UserInActiveException;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Model.GenericUser;
import com.erp.Model.RootUser;
import com.erp.Multitenancy.TenantContext;
import com.erp.Repository.Rootuser.RootUserRepository;
import com.erp.Security.Filter.TokenBlackListService;
import com.erp.Security.JWT.ClaimName;
import com.erp.Security.JWT.JWTService;
import com.erp.Security.JWT.TokenType;
import com.erp.Security.util.CookieManager;
import com.erp.Security.util.UserRepositoryRegistry;
import com.erp.Service.Helper.TokenGenerationServiceHelper;
import com.erp.Service.TokenGeneration.TokenGenerationService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class GenericAuthServiceImpl implements AuthService {

    private final UserRepositoryRegistry userRepositoryRegistry;
    private final TokenBlackListService tokenBlackListService;
    private final RootUserRepository rootUserRepository;
    private final MetaAdminRepository metaAdminRepository;
    private final JWTService jwtService;
    private final CookieManager cookieManager;
    private final PasswordEncoder passwordEncoder;
    private  final TokenGenerationService tokenGenerationService;
    private final TokenGenerationServiceHelper generationServiceHelper;

    @Override
    @Transactional
    public AuthRecord login(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();
        log.info("Attempting login for email: {}", email);

        // Step 1: RootUser in public schema
        TenantContext.setCurrentTenant("public");
        Optional<RootUser> rootUserOpt = rootUserRepository.findByEmail(email);
        if (rootUserOpt.isPresent()) {
            log.info("Login as RootUser in schema 'public'");
            return authenticateAndBuildRecord(rootUserOpt.get(), email, password, "public");
        }

        // Step 2: Admin — resolve schema from meta_admin
        Optional<String> resolvedTenantOpt = metaAdminRepository.findSchemaNameByAdminEmail(email);
        if (resolvedTenantOpt.isPresent()) {
            String resolvedTenant = resolvedTenantOpt.get();
            TenantContext.setCurrentTenant(resolvedTenant);
            log.info("Login as Admin in tenant '{}'", resolvedTenant);

            GenericUser adminUser = userRepositoryRegistry.findUserByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Admin user not found in tenant: " + resolvedTenant));

            return authenticateAndBuildRecord(adminUser, email, password, resolvedTenant);
        }

        // Step 3: Normal user — expect tenant auto-resolved from AuthFilter
        String currentTenant = TenantContext.getCurrentTenant();
        if (currentTenant == null || currentTenant.isBlank()) {
            throw new IllegalArgumentException("No matching tenant found for email: " + email);
        }

        log.info("Login as NormalUser in tenant '{}'", currentTenant);
        GenericUser user = userRepositoryRegistry.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in tenant: " + currentTenant));

        return authenticateAndBuildRecord(user, email, password, user.getSchemaName());
    }

    private AuthRecord authenticateAndBuildRecord(GenericUser user, String email, String password, String schemaName) {
        // ✅ Manual password verification
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Invalid password for user: {}", email);
            throw new BadCredentialsException("Invalid credentials for user: " + email);
        }

        if (!user.isActive()) {
            log.warn("Inactive user login attempt: {}", email);
            throw new UserInActiveException("User account is inactive.");
        }

        log.info("Authentication successful for user: {} in schema: {}", email, schemaName);
        return createAuthRecordFromUser(user, schemaName);
    }

    @Override
    @Transactional
    public AuthRecord refreshLogin(String refreshToken) {
        try {
            Claims claims = jwtService.parseToken(refreshToken);
            String email = claims.get(ClaimName.USER_EMAIL, String.class);
            String schemaName = claims.get(ClaimName.SCHEMA_NAME, String.class);
            long refreshExpiration = claims.getExpiration().toInstant().toEpochMilli();

            if (schemaName == null || schemaName.isBlank()) {
                throw new IllegalArgumentException("Missing schema_name in token claims.");
            }

            TenantContext.setCurrentTenant(schemaName);

            GenericUser user;
            if ("public".equals(schemaName)) {
                user = rootUserRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Root user not found"));
            } else {
                user = userRepositoryRegistry.findUserByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found in schema: " + schemaName));
            }

            return buildRefreshRecord(user, schemaName, refreshExpiration);

        } catch (Exception e) {
            log.error("Refresh token handling failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    private AuthRecord buildRefreshRecord(GenericUser user, String schemaName, long refreshExpiration) {
        long accessExpiration = Instant.now().plusSeconds(3600).toEpochMilli();
        List<String> roles = user.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList();

        AuthRecord authRecord = new AuthRecord(user.getId(), user.getEmail(),true,schemaName,accessExpiration,refreshExpiration,
                roles,"","");
        Map<String, Object> claim = tokenGenerationService.setClaim(authRecord);
        String accessCookie = generationServiceHelper.generateToken(
                TokenType.ACCESS, claim, Instant.ofEpochMilli(authRecord.accessExpiration()));
        String refreshCookie = generationServiceHelper.generateToken(
                TokenType.REFRESH, claim, Instant.ofEpochMilli(authRecord.refreshExpiration()));

        return new AuthRecord(
                user.getId(),
                user.getEmail(),
                user.isActive(),
                schemaName,
                accessExpiration,
                refreshExpiration,
                roles,
                accessCookie,
                refreshCookie
        );
    }

    @Override
    public HttpHeaders logout(String refreshToken, String accessToken) {
        try {
            tokenBlackListService.blackListToken(refreshToken);
            tokenBlackListService.blackListToken(accessToken);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE, cookieManager.generateCookie("rt", "", 0));
            headers.add(HttpHeaders.SET_COOKIE, cookieManager.generateCookie("at", "", 0));
            return headers;

        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    private AuthRecord createAuthRecordFromUser(GenericUser user, String schemaName) {
        Instant now = Instant.now();
        long accessExpiration = now.plusSeconds(3600).toEpochMilli();
        long refreshExpiration = now.plusSeconds(60L * 60 * 24 * 60).toEpochMilli();

        List<String> roles = user.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();

        AuthRecord authRecord = new AuthRecord(user.getId(), user.getEmail(),true,schemaName,accessExpiration,refreshExpiration,
                roles,"","");
        Map<String, Object> claim = tokenGenerationService.setClaim(authRecord);
        String accessCookie = generationServiceHelper.generateToken(
                TokenType.ACCESS, claim, Instant.ofEpochMilli(authRecord.accessExpiration()));
        String refreshCookie = generationServiceHelper.generateToken(
                TokenType.REFRESH, claim, Instant.ofEpochMilli(authRecord.refreshExpiration()));

        String token = accessCookie.substring(accessCookie.indexOf("at=") + 3, accessCookie.indexOf(";"));
        String refreshToken = refreshCookie.substring(refreshCookie.indexOf("at=") + 3, refreshCookie.indexOf(";"));


        return new AuthRecord(
                user.getId(),
                user.getEmail(),
                user.isActive(),
                schemaName,
                accessExpiration,
                refreshExpiration,
                roles,
                token,
                refreshToken
        );
    }

    public Optional<GenericUser> loadUserFromCentralSchema(String email) {
        String previous = TenantContext.getCurrentTenant();
        try {
            TenantContext.setCurrentTenant("master"); // central schema where users table exists
            return userRepositoryRegistry.findUserByEmail(email);
        } finally {
            // restore previous tenant (could be null)
            if (previous == null) {
                TenantContext.clear();
            } else {
                TenantContext.setCurrentTenant(previous);
            }

        }
    }


}