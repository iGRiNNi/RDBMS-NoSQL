package org.example.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Neo4jConfig {

    private static final String PROPERTIES_FILE = "neo4j.properties";

    private final String uri;
    private final String username;
    private final String password;
    private final String database;

    private Neo4jConfig(String uri, String username, String password, String database) {
        this.uri = uri;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public static Neo4jConfig load() {
        Properties properties = new Properties();

        try (InputStream inputStream = Neo4jConfig.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("File " + PROPERTIES_FILE + " not found in resources");
            }

            properties.load(inputStream);

            String uri = properties.getProperty("neo4j.uri");
            String username = properties.getProperty("neo4j.username");
            String password = properties.getProperty("neo4j.password");
            String database = properties.getProperty("neo4j.database", "neo4j");

            if (uri == null || uri.isBlank()) {
                throw new RuntimeException("Property neo4j.uri is missing");
            }

            if (username == null || username.isBlank()) {
                throw new RuntimeException("Property neo4j.username is missing");
            }

            if (password == null || password.isBlank()) {
                throw new RuntimeException("Property neo4j.password is missing");
            }

            return new Neo4jConfig(uri, username, password, database);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load Neo4j config", e);
        }
    }

    public String getUri() {
        return uri;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }
}
