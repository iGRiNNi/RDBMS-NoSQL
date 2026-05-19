package com.example.infrastructure.mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class MongoConfig {

    private static final String PROPERTIES_FILE = "mongodb.properties";

    private final String connectionString;
    private final String databaseName;

    private MongoConfig(String connectionString, String databaseName) {
        this.connectionString = connectionString;
        this.databaseName = databaseName;
    }

    public static MongoConfig load() {
        Properties properties = new Properties();

        try (InputStream inputStream = MongoConfig.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("File " + PROPERTIES_FILE + " not found");
            }

            properties.load(inputStream);

            String connectionString = properties.getProperty("mongodb.connectionString");
            String databaseName = properties.getProperty("mongodb.database");

            if (connectionString == null || connectionString.isBlank()) {
                throw new RuntimeException("Property mongodb.connectionString is missing");
            }

            if (databaseName == null || databaseName.isBlank()) {
                throw new RuntimeException("Property mongodb.database is missing");
            }

            return new MongoConfig(connectionString, databaseName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load MongoDB config", e);
        }
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}