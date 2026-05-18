package org.example.infrastructure.clickhouse;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ClickHouseConfig {

    private static final String PROPERTIES_FILE = "clickhouse.properties";

    private final String url;
    private final String username;
    private final String password;

    private ClickHouseConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static ClickHouseConfig load() {
        Properties properties = new Properties();

        try (InputStream inputStream = ClickHouseConfig.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("File " + PROPERTIES_FILE + " not found in resources");
            }

            properties.load(inputStream);

            String url = properties.getProperty("clickhouse.url");
            String username = properties.getProperty("clickhouse.username", "default");
            String password = properties.getProperty("clickhouse.password", "");

            if (url == null || url.isBlank()) {
                throw new RuntimeException("Property clickhouse.url is missing");
            }

            return new ClickHouseConfig(url, username, password);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load ClickHouse properties", e);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
