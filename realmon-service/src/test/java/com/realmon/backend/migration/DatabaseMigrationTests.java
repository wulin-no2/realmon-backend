package com.realmon.backend.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseMigrationTests {

    @Test
    void existingSchemaIsBaselinedAndUpgraded() throws Exception {
        String url = legacyDatabaseUrl();
        createLegacySchema(url, false);

        migrateExistingDatabase(url);

        try (Connection connection = DriverManager.getConnection(url, "sa", "");
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT role FROM `user` WHERE username = 'existing-user'")) {
            assertTrue(result.next());
            assertEquals("USER", result.getString("role"));
        }
    }

    @Test
    void existingDuplicatesStopMigrationWithClearMessage() throws Exception {
        String url = legacyDatabaseUrl();
        createLegacySchema(url, true);

        FlywayException exception = assertThrows(FlywayException.class,
                () -> migrateExistingDatabase(url));

        assertTrue(rootMessage(exception).contains("Duplicate usernames must be resolved"));
    }

    private void migrateExistingDatabase(String url) {
        Flyway.configure()
                .dataSource(url, "sa", "")
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .load()
                .migrate();
    }

    private void createLegacySchema(String url, boolean duplicateUsername) throws Exception {
        try (Connection connection = DriverManager.getConnection(url, "sa", "");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE `user` ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255))");
            statement.execute("CREATE TABLE user_species ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, user_id BIGINT, species_id VARCHAR(255))");
            statement.execute("INSERT INTO `user` (username) VALUES ('existing-user')");
            if (duplicateUsername) {
                statement.execute("INSERT INTO `user` (username) VALUES ('existing-user')");
            }
        }
    }

    private String legacyDatabaseUrl() {
        return "jdbc:h2:mem:" + UUID.randomUUID()
                + ";MODE=MySQL;NON_KEYWORDS=USER;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    }

    private String rootMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage();
    }
}
