package org.example.infrastructure.clickhouse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ClickHouseConnectionManager {

    private static final ClickHouseConfig CONFIG = ClickHouseConfig.load();

    private ClickHouseConnectionManager() {
    }

    public static Connection getConnection() {
        Properties properties = new Properties();
        properties.setProperty("user", CONFIG.getUsername());
        properties.setProperty("password", CONFIG.getPassword());
        properties.setProperty("client_name", "oil-lab-java");
        System.out.println(properties);

        try {
            //return DriverManager.getConnection(CONFIG.getUrl(), properties);
            return DriverManager.getConnection(CONFIG.getUrl(), CONFIG.getUsername(), CONFIG.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get ClickHouse connection", e);
        }
    }
}
