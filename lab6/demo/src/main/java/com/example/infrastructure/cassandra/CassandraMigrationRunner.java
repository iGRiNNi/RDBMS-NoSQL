package com.example.infrastructure.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class CassandraMigrationRunner {

    public void migrate() {
        executeScript("db/migration/V1__init_keyspace.cql");
        executeScript("db/migration/V2__init_tables.cql");
        executeScript("db/migration/V3__init_indexes.cql");
    }

    private void executeScript(String resourcePath) {
        String script = readResource(resourcePath);

        List<String> statements = Arrays.stream(script.split(";"))
                .map(String::trim)
                .filter(statement -> !statement.isBlank())
                .toList();

        CqlSession session = CassandraSessionManager.getSession();

        for (String statement : statements) {
            session.execute(statement);
        }
    }

    private String readResource(String resourcePath) {
        try (InputStream inputStream = CassandraMigrationRunner.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + resourcePath, e);
        }
    }
}