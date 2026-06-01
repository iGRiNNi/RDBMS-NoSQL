package com.example.infrastructure.cassandra;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class CassandraConfig {

    private static final String PROPERTIES_FILE = "cassandra.properties";

    private final String contactPoint;
    private final int port;
    private final String localDatacenter;
    private final String keyspace;
    private final String flywayUrl;

    private CassandraConfig(
            String contactPoint,
            int port,
            String localDatacenter,
            String keyspace,
            String flywayUrl
    ) {
        this.contactPoint = contactPoint;
        this.port = port;
        this.localDatacenter = localDatacenter;
        this.keyspace = keyspace;
        this.flywayUrl = flywayUrl;
    }

    public static CassandraConfig load() {
        Properties properties = new Properties();

        try (InputStream inputStream = CassandraConfig.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("File " + PROPERTIES_FILE + " not found in resources");
            }

            properties.load(inputStream);

            return new CassandraConfig(
                    getRequiredProperty(properties, "cassandra.contact-point"),
                    Integer.parseInt(getRequiredProperty(properties, "cassandra.port")),
                    getRequiredProperty(properties, "cassandra.local-datacenter"),
                    getRequiredProperty(properties, "cassandra.keyspace"),
                    getRequiredProperty(properties, "cassandra.flyway-url")
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to load Cassandra config", e);
        }
    }

    private static String getRequiredProperty(Properties properties, String name) {
        String value = properties.getProperty(name);

        if (value == null || value.isBlank()) {
            throw new RuntimeException("Property " + name + " is missing");
        }

        return value;
    }

    public String getContactPoint() {
        return contactPoint;
    }

    public int getPort() {
        return port;
    }

    public String getLocalDatacenter() {
        return localDatacenter;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public String getFlywayUrl() {
        return flywayUrl;
    }
}