package com.erp.init;

import com.erp.Service.Schema.SchemaManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);
    private final SchemaManagementService schemaManagementService;

    @Autowired
    public ApplicationStartup(SchemaManagementService schemaManagementService) {
        this.schemaManagementService = schemaManagementService;
    }

    @Override
    public void run(String... args) {
        logger.info("Triggering migration for all tenant schemas on startup");
        try {
            schemaManagementService.migrateAllTenantSchemas();
        } catch (Exception e) {
            logger.error("Failed to migrate tenant schemas on startup", e);
            throw new RuntimeException("Startup tenant schema migration failed", e);
        }
    }
}