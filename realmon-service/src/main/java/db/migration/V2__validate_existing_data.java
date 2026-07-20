package db.migration;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class V2__validate_existing_data extends BaseJavaMigration {

    @Override
    public Integer getChecksum() {
        return 1;
    }

    @Override
    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        ensureRoleColumnExists(connection);
        assertNoDuplicates(connection,
                "SELECT username FROM `user` WHERE username IS NOT NULL "
                        + "GROUP BY username HAVING COUNT(*) > 1 LIMIT 1",
                "Duplicate usernames must be resolved before applying database constraints");
        assertNoDuplicates(connection,
                "SELECT user_id FROM user_species WHERE user_id IS NOT NULL AND species_id IS NOT NULL "
                        + "GROUP BY user_id, species_id HAVING COUNT(*) > 1 LIMIT 1",
                "Duplicate user/species collections must be resolved before applying database constraints");
    }

    private void ensureRoleColumnExists(Connection connection) throws SQLException {
        if (columnExists(connection, "user", "role")) {
            return;
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute("ALTER TABLE `user` ADD COLUMN role VARCHAR(20)");
        }
    }

    private boolean columnExists(Connection connection, String table, String column) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        String catalog = connection.getCatalog();
        for (String tableName : new String[]{table, table.toUpperCase()}) {
            for (String columnName : new String[]{column, column.toUpperCase()}) {
                try (ResultSet columns = metadata.getColumns(catalog, null, tableName, columnName)) {
                    if (columns.next()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void assertNoDuplicates(Connection connection, String query, String message)
            throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {
            if (result.next()) {
                throw new FlywayException(message);
            }
        }
    }
}
