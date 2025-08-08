package com.erp.Utility;

import com.erp.Model.RootUser;
import com.erp.Multitenancy.TenantContextHolder;
import com.erp.Repository.Rootuser.RootUserRepository;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RootUserInitializer implements CommandLineRunner {

    private static final Logger logger =    LoggerFactory.getLogger(RootUserInitializer.class);

    private final RootUserRepository rootUserRepository;
    private final Flyway flyway; // Ensure migrations run first

    @Autowired
    public RootUserInitializer(RootUserRepository rootUserRepository, Flyway flyway) {
        this.rootUserRepository = rootUserRepository;
        this.flyway = flyway;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${root.email}")
    private String email;

    @Value("${root.password}")
    private String password;

    @Value("${root.name}")
    private String name;

    @Override
    public void run(String... args) {
        logger.info("Initializing root user");
        try (var context = new TenantContextHolder("public")){
            // Ensure Flyway migrations have run
            flyway.migrate();

            // Check if root user exists
            if (!rootUserRepository.existsByEmail(email)) {
                RootUser rootUser = new RootUser();
                rootUser.setName(name);
                rootUser.setEmail(email);
                rootUser.setPassword(passwordEncoder.encode(password)); // Replace with proper password hashing
                rootUser.setActive(true);
                rootUserRepository.save(rootUser);
                logger.info("Root user created successfully");
            } else {
                logger.info("Root user already exists");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize root user", e);
            throw new RuntimeException("Root user initialization failed", e);
        }
    }
}
