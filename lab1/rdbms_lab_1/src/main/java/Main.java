import dal.infrastructure.connection.ConnectionManager;
import dal.infrastructure.connection.DatabaseConfig;
import dal.infrastructure.migration.MigrationRunner;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig config = DatabaseConfig.load();

        MigrationRunner migrationRunner = new MigrationRunner(config);
        migrationRunner.migrate();
    }
}
