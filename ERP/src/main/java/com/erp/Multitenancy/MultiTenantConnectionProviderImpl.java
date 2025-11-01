package com.erp.Multitenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider<Object> {
    private static final String DEFAULT_SCHEMA = "public";
    private static final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);
    private final DataSource dataSource;

    public MultiTenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        logger.debug("Acquiring any connection from DataSource");
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        logger.debug("Acquired connection: {}, autoCommit: {}", connection, connection.getAutoCommit());
        return connection;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
            }
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.debug("Connection closed");
            }
        }
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        String schema = tenantIdentifier != null ? tenantIdentifier.toString() : DEFAULT_SCHEMA;
        logger.info("Requesting connection for tenant: {}", schema);
        Connection connection = getAnyConnection();
        try {
            logger.debug("Setting schema to: {}", schema);
            connection.setSchema(schema);
            // Verify schema existence
//            try (var statement = connection.createStatement();
//                 var rs = statement.executeQuery("SELECT EXISTS(SELECT 1 FROM information_schema.schemata WHERE schema_name = '" + schema + "')")) {
//                if (rs.next() && !rs.getBoolean(1)) {
//                    throw new SQLException("Schema does not exist: " + schema);
//                }
//            }
//            // Verify current schema
//            try (var statement = connection.createStatement();
//                 var rs = statement.executeQuery("SELECT current_schema()")) {
//                if (rs.next()) {
//                    String currentSchema = rs.getString(1);
//                    logger.info("Current schema after setSchema: {}", currentSchema);
//                    if (!schema.equals(currentSchema)) {
//                        throw new SQLException("Failed to set schema to: " + schema + ", got: " + currentSchema);
//                    }
//                } else {
//                    throw new SQLException("No schema returned from current_schema()");
//                }
//            }
            return connection;
        } catch (SQLException e) {
            logger.error("Error setting schema: {}", schema, e);
            connection.close();
            throw new SQLException("Error setting schema: " + schema, e);
        }
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
                logger.debug("Committed connection for tenant: {}", tenantIdentifier);
            }
        } finally {
            releaseAnyConnection(connection);
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false; // Changed to false to prevent premature connection release
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return (T) this;
        }
        throw new org.hibernate.service.UnknownUnwrapTypeException(unwrapType);
    }
}