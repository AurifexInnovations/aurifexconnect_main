package com.erp.Service.Schema;

import com.erp.Meta.MetaAdmin;
import com.erp.Meta.MetaAdminRepository;
import com.erp.Multitenancy.MultiTenantConnectionProviderImpl;
import com.erp.Multitenancy.TenantContextHolder;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class SchemaManagementService {
    private static final Logger logger = LoggerFactory.getLogger(SchemaManagementService.class);
    private final DataSource dataSource;
    private final MetaAdminRepository metaAdminRepository;
    private final JdbcTemplate jdbcTemplate;
    private final MultiTenantConnectionProviderImpl connectionProvider;

    public SchemaManagementService(DataSource dataSource, MetaAdminRepository metaAdminRepository,
                                   JdbcTemplate jdbcTemplate, MultiTenantConnectionProviderImpl connectionProvider) {
        this.dataSource = dataSource;
        this.metaAdminRepository = metaAdminRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.connectionProvider = connectionProvider;
    }

    public static String validateSchemaName(String schemaName) {
        if (schemaName == null || schemaName.trim().isEmpty()) {
            throw new IllegalArgumentException("Schema name cannot be null or empty");
        }
        if (schemaName.length() > 63) {
            throw new IllegalArgumentException("Schema name too long: " + schemaName);
        }
        if (!schemaName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Invalid schema name: " + schemaName);
        }
        return schemaName;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createTenantSchema(String schemaName, String adminEmail) {
        logger.info("Creating tenant schema: {}", schemaName);
        validateSchemaName(schemaName);

        // Step 1: Create schema
        try (var context = new TenantContextHolder("public")) {
            boolean schemaExists = jdbcTemplate.queryForObject(
                    "SELECT EXISTS(SELECT 1 FROM information_schema.schemata WHERE schema_name = ?)",
                    Boolean.class, schemaName);
            if (!schemaExists) {
                try (Connection conn = dataSource.getConnection()) {
                    conn.setAutoCommit(true); // Ensure immediate commit
                    conn.createStatement().execute("CREATE SCHEMA \"" + schemaName + "\"");
                    logger.info("Schema {} created successfully", schemaName);
                } catch (SQLException e) {
                    logger.error("Failed to create schema: {}", schemaName, e);
                    throw new RuntimeException("Failed to create schema: " + schemaName, e);
                }
            } else {
                logger.debug("Schema {} already exists", schemaName);
            }
        }

        // Step 2: Commit and verify schema existence with a fresh connection
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (var context = new TenantContextHolder("public")) {
                boolean schemaExists = jdbcTemplate.queryForObject(
                        "SELECT EXISTS(SELECT 1 FROM information_schema.schemata WHERE schema_name = ?)",
                        Boolean.class, schemaName);
                if (!schemaExists) {
                    logger.error("Schema {} was not committed", schemaName);
                    throw new RuntimeException("Schema creation failed for: " + schemaName);
                }
                logger.debug("Schema {} verified as committed", schemaName);
            }
        } catch (SQLException e) {
            logger.error("Failed to verify schema: {}", schemaName, e);
            throw new RuntimeException("Failed to verify schema: " + schemaName, e);
        }

        // Step 3: Run Flyway migrations
        try (var context = new TenantContextHolder(schemaName)) {
            Connection connection = connectionProvider.getConnection(schemaName);
            try {
                Flyway flyway = new FluentConfiguration()
                        .dataSource(new SingleConnectionDataSource(connection, false))
                        .locations("classpath:db/migration/tenant")
                        .schemas(schemaName)
                        .baselineOnMigrate(true)
                        .table("flyway_schema_history")
                        .load();
                flyway.migrate();
                logger.info("Flyway migrations completed for schema: {}", schemaName);
            } finally {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    logger.debug("Connection closed for schema: {}", schemaName);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to apply Flyway migrations for schema: {}", schemaName, e);
            throw new RuntimeException("Failed to apply Flyway migrations for schema: " + schemaName, e);
        }

        // Step 4: Save tenant metadata
        try (var context = new TenantContextHolder("public")) {
            MetaAdmin metaAdmin = new MetaAdmin();
            metaAdmin.setAdminEmail(adminEmail);
            metaAdmin.setSchemaName(schemaName);
            metaAdminRepository.save(metaAdmin);
            logger.info("MetaAdmin saved for schema: {}", schemaName);
        } catch (Exception e) {
            logger.error("Failed to save MetaAdmin for schema: {}", schemaName, e);
            throw new RuntimeException("Failed to save MetaAdmin for schema: " + schemaName, e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void migrateAllTenantSchemas() {
        logger.info("Migrating all tenant schemas on startup");
        try (var context = new TenantContextHolder("public")) {
            List<MetaAdmin> tenants = metaAdminRepository.findAll();
            if (tenants.isEmpty()) {
                logger.info("No tenant schemas found in meta_admin");
                return;
            }
            for (MetaAdmin tenant : tenants) {
                String schemaName = tenant.getSchemaName();
                logger.info("Migrating schema: {}", schemaName);
                try (var tenantContext = new TenantContextHolder(schemaName)) {
                    Connection connection = connectionProvider.getConnection(schemaName);
                    try {
                        Flyway flyway = new FluentConfiguration()
                                .dataSource(new SingleConnectionDataSource(connection, false))
                                .locations("classpath:db/migration/tenant")
                                .schemas(schemaName)
                                .baselineOnMigrate(true)
                                .table("flyway_schema_history")
                                .load();
                        flyway.migrate();
                        logger.info("Migration completed for schema: {}", schemaName);
                    } finally {
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                            logger.debug("Connection closed for schema: {}", schemaName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to migrate tenant schemas", e);
            throw new RuntimeException("Failed to migrate tenant schemas", e);
        }
    }
}