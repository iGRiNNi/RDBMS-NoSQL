package org.example;

import org.example.dao.FieldDao;
import org.example.dao.ProductionDao;
import org.example.dao.WellDao;
import org.example.demo.AnalyticsDemo;
import org.example.demo.CollapsingMergeTreeDemo;
import org.example.demo.JoinDemo;
import org.example.demo.ReplacingMergeTreeDemo;
import org.example.domain.model.Field;
import org.example.domain.model.Production;
import org.example.domain.model.Well;
import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import org.example.infrastructure.clickhouse.ClickHouseMigrationRunner;

public class Main {

    public static void main(String[] args) {

        ClickHouseMigrationRunner migrationRunner = new ClickHouseMigrationRunner();
        migrationRunner.migrate();

        printTableCounts();

        runCrudDemo();

        System.out.println();
        System.out.println("=== ПУНКТ 2. JOIN ===");
        JoinDemo joinDemo = new JoinDemo();
        joinDemo.run();

        System.out.println();
        System.out.println("=== ПУНКТ 3. АНАЛИТИЧЕСКИЙ ЗАПРОС ===");
        AnalyticsDemo.run();

        System.out.println();
        System.out.println("=== ПУНКТ 4. REPLACING MERGE TREE ===");
        ReplacingMergeTreeDemo.run();

        System.out.println();
        System.out.println("=== ПУНКТ 5. COLLAPSING MERGE TREE ===");
        CollapsingMergeTreeDemo.run();
    }

    private static void printTableCounts() {
        String sql = """
                SELECT 'fields' AS table_name, count() AS rows_count FROM fields
                UNION ALL
                SELECT 'wells' AS table_name, count() AS rows_count FROM wells
                UNION ALL
                SELECT 'productions' AS table_name, count() AS rows_count FROM productions
                UNION ALL
                SELECT 'well_versioned' AS table_name, count() AS rows_count FROM well_versioned
                UNION ALL
                SELECT 'production_collapsing' AS table_name, count() AS rows_count FROM production_collapsing
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            System.out.println();
            System.out.println("=== Количество строк в таблицах ===");

            while (rs.next()) {
                System.out.println(
                        rs.getString("table_name") + ": " + rs.getLong("rows_count")
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка вывода количества строк", e);
        }
    }

    private static void runCrudDemo() {
        FieldDao fieldDao = new FieldDao();
        WellDao wellDao = new WellDao();
        ProductionDao productionDao = new ProductionDao();

        Field field = new Field(
                100,
                "Тестовое месторождение",
                "Тестовый регион",
                55.0,
                60.0
        );

        Well well = new Well(
                100,
                "TEST-100",
                "active",
                3000.0,
                0.22,
                100L
        );

        Production production = new Production(
                100,
                100,
                LocalDate.of(2026, 3, 10),
                100.0,
                500.0,
                20.0
        );

        System.out.println("=== CREATE ===");

        fieldDao.create(field);
        wellDao.create(well);
        productionDao.create(production);

        System.out.println(fieldDao.getById(100L));
        System.out.println(wellDao.getById(100L));
        System.out.println(productionDao.getById(100L));

        System.out.println();
        System.out.println("=== UPDATE ===");

        Field updatedField = new Field(
                100,
                "Тестовое месторождение",
                "Обновлённый регион",
                55.0,
                60.0
        );

        Well updatedWell = new Well(
                100,
                "TEST-100",
                "maintenance",
                3000.0,
                0.22,
                100L
        );

        Production updatedProduction = new Production(
                100,
                100,
                LocalDate.of(2026, 3, 10),
                120.0,
                550.0,
                30.0
        );

        fieldDao.update(updatedField);
        wellDao.update(updatedWell);
        productionDao.update(updatedProduction);

        System.out.println(fieldDao.getById(100L));
        System.out.println(wellDao.getById(100L));
        System.out.println(productionDao.getById(100L));

        System.out.println();
        System.out.println("=== GET ALL ===");

        fieldDao.getAll().forEach(System.out::println);
        wellDao.getAll().forEach(System.out::println);
        productionDao.getAll().forEach(System.out::println);

        System.out.println();
        System.out.println("=== DELETE ===");

        productionDao.delete(100L);
        wellDao.delete(100L);
        fieldDao.delete(100L);

        System.out.println("Field после удаления: " + fieldDao.getById(100L));
        System.out.println("Well после удаления: " + wellDao.getById(100L));
        System.out.println("Production после удаления: " + productionDao.getById(100L));
    }
}