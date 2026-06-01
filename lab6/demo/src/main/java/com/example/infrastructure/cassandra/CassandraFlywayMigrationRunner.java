package com.example.infrastructure.cassandra;

import org.flywaydb.core.Flyway;

public class CassandraFlywayMigrationRunner {

    private final CassandraConfig config;

    public CassandraFlywayMigrationRunner() {
        this.config = CassandraConfig.load();
    }

    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(config.getFlywayUrl(), "", "")
                .locations("classpath:db/migration")
                .sqlMigrationSuffixes(".cql")
                .defaultSchema(config.getKeyspace())
                .schemas(config.getKeyspace())
                .initSql(createKeyspaceSql())
                .load();

        flyway.migrate();
    }

    private String createKeyspaceSql() {
        return """
                CREATE KEYSPACE IF NOT EXISTS %s
                WITH replication = {
                    'class': 'SimpleStrategy',
                    'replication_factor': 1
                }
                """.formatted(config.getKeyspace());
    }
}