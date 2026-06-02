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

    private CassandraConfig(
            String contactPoint,
            int port,
            String localDatacenter,
            String keyspace
    ) {
        this.contactPoint = contactPoint;
        this.port = port;
        this.localDatacenter = localDatacenter;
        this.keyspace = keyspace;
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

            String contactPoint = getRequiredProperty(properties, "cassandra.contact-point");
            int port = Integer.parseInt(getRequiredProperty(properties, "cassandra.port"));
            String localDatacenter = getRequiredProperty(properties, "cassandra.local-datacenter");
            String keyspace = getRequiredProperty(properties, "cassandra.keyspace");

            return new CassandraConfig(
                    contactPoint,
                    port,
                    localDatacenter,
                    keyspace
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
}