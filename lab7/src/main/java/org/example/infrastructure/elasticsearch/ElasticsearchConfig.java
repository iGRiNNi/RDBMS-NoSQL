package org.example.infrastructure.elasticsearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ElasticsearchConfig {

    private static final String PROPERTIES_FILE = "elasticsearch.properties";

    private final String scheme;
    private final String host;
    private final int port;
    private final boolean recreateIndices;

    private ElasticsearchConfig(String scheme, String host, int port, boolean recreateIndices) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.recreateIndices = recreateIndices;
    }

    public static ElasticsearchConfig load() {
        Properties properties = new Properties();

        try (InputStream inputStream = ElasticsearchConfig.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("File " + PROPERTIES_FILE + " not found in resources");
            }

            properties.load(inputStream);

            return new ElasticsearchConfig(
                    getRequiredProperty(properties, "elasticsearch.scheme"),
                    getRequiredProperty(properties, "elasticsearch.host"),
                    Integer.parseInt(getRequiredProperty(properties, "elasticsearch.port")),
                    Boolean.parseBoolean(properties.getProperty("elasticsearch.recreate-indices", "false"))
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to load Elasticsearch config", e);
        }
    }

    private static String getRequiredProperty(Properties properties, String name) {
        String value = properties.getProperty(name);

        if (value == null || value.isBlank()) {
            throw new RuntimeException("Property " + name + " is missing");
        }

        return value;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isRecreateIndices() {
        return recreateIndices;
    }
}