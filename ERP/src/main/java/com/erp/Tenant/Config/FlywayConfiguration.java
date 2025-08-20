//package com.erp.Tenant.Config;
//
//import org.flywaydb.core.Flyway;
//import org.flywaydb.core.api.configuration.FluentConfiguration;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class FlywayConfiguration {
//
//    private static final Logger logger = LoggerFactory.getLogger(FlywayConfiguration.class);
//
//    private final DataSource dataSource;
//
//    @Autowired
//    public FlywayConfiguration(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Bean
//    @Order(0) // Ensure Flyway runs before other CommandLineRunners
//    public Flyway flyway() {
//        logger.info("Configuring Flyway for public schema");
//        Flyway flyway = new FluentConfiguration()
//                .dataSource(dataSource)
//                .locations("classpath:db/migration/global")
//                .schemas("public")
//                .baselineOnMigrate(true)
//                .table("flyway_schema_history")
//                .load();
//        flyway.migrate(); // Run migrations immediately
//        return flyway;
//    }
//}