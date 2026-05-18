package org.example;

import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        checkClickHouseConnection();
    }

    private static void checkClickHouseConnection() {
        try (Connection connection =
                     ClickHouseConnectionManager.getConnection();

             Statement statement =
                     connection.createStatement()) {

            statement.execute(
                    "CREATE DATABASE IF NOT EXISTS oil_lab"
            );

            System.out.println(
                    "База данных oil_lab создана или уже существует."
            );

        } catch (Exception e) {

            System.out.println(
                    "Ошибка создания базы данных: " + e.getMessage()
            );
        }
    }
}