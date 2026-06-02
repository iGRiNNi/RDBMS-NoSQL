package com.example.infrastructure.cassandra;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class CassandraMigrationRunner {

    private static final List<String> KEYSPACE_SCRIPTS = List.of(
            "cql/V1__init_keyspace.cql"
    );

    private static final List<String> KEYSPACE_DEPENDENT_SCRIPTS = List.of(
            "cql/V2__init_tables.cql",
            "cql/V3__init_indexes.cql"
    );

    private final CassandraConfig config;

    public CassandraMigrationRunner() {
        this.config = CassandraConfig.load();
    }

    public void migrate() {
        executeKeyspaceScripts();
        executeKeyspaceDependentScripts();
    }

    private void executeKeyspaceScripts() {
        try (CqlSession session = createSessionWithoutKeyspace()) {
            for (String scriptPath : KEYSPACE_SCRIPTS) {
                executeScript(session, scriptPath);
            }
        }
    }

    private void executeKeyspaceDependentScripts() {
        try (CqlSession session = createSessionWithKeyspace()) {
            for (String scriptPath : KEYSPACE_DEPENDENT_SCRIPTS) {
                executeScript(session, scriptPath);
            }
        }
    }

    private CqlSession createSessionWithoutKeyspace() {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(
                        config.getContactPoint(),
                        config.getPort()
                ))
                .withLocalDatacenter(config.getLocalDatacenter())
                .build();
    }

    private CqlSession createSessionWithKeyspace() {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(
                        config.getContactPoint(),
                        config.getPort()
                ))
                .withLocalDatacenter(config.getLocalDatacenter())
                .withKeyspace(CqlIdentifier.fromCql(config.getKeyspace()))
                .build();
    }

    private void executeScript(CqlSession session, String resourcePath) {
        String script = readResource(resourcePath);
        String preparedScript = prepareScript(script);

        List<String> statements = Arrays.stream(preparedScript.split(";"))
                .map(String::trim)
                .filter(statement -> !statement.isBlank())
                .toList();

        try {
            for (String cql : statements) {
                SimpleStatement statement = SimpleStatement.builder(cql)
                        .setTimeout(Duration.ofSeconds(30))
                        .build();

                session.execute(statement);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute CQL script: " + resourcePath, e);
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

    private String prepareScript(String script) {
        String withoutComments = removeLineComments(script);
        return removeUseStatements(withoutComments);
    }

    private String removeLineComments(String script) {
        StringBuilder result = new StringBuilder();

        for (String line : script.split("\\R")) {
            int commentIndex = line.indexOf("--");

            if (commentIndex >= 0) {
                result.append(line, 0, commentIndex);
            } else {
                result.append(line);
            }

            result.append(System.lineSeparator());
        }

        return result.toString();
    }

    private String removeUseStatements(String script) {
        return script.replaceAll("(?im)^\\s*USE\\s+\\w+\\s*;?\\s*$", "");
    }
}