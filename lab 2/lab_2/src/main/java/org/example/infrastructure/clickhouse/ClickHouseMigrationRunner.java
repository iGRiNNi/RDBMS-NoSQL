package org.example.infrastructure.clickhouse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class ClickHouseMigrationRunner {

    public void migrate() {
        executeScript("sql/V1__init_schema.sql");
        executeScript("sql/V2__seed_data.sql");
    }

    private void executeScript(String resourcePath) {
        String script = readResource(resourcePath);
        String scriptWithoutComments = removeSqlComments(script);

        List<String> statements = Arrays.stream(scriptWithoutComments.split(";"))
                .map(String::trim)
                .filter(statement -> !statement.isBlank())
                .toList();

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {

            for (String sql : statements) {
                statement.execute(sql);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute script: " + resourcePath, e);
        }
    }

    private String readResource(String resourcePath) {
        try (InputStream inputStream = ClickHouseMigrationRunner.class
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

    private String removeSqlComments(String script) {
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
}