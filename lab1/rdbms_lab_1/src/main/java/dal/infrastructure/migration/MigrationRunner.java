package dal.infrastructure.migration;

import dal.infrastructure.connection.DatabaseConfig;
import org.flywaydb.core.Flyway;

public class MigrationRunner {

    private final DatabaseConfig config;

    public MigrationRunner(DatabaseConfig config) {
        this.config = config;
    }

    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        config.getUrl(),
                        config.getUsername(),
                        config.getPassword()
                )
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();
    }
}
