package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.example.dao.FieldDao;
import org.example.dao.ProductionDao;
import org.example.dao.WellDao;
import org.example.demo.ComplexQueryDemo;
import org.example.demo.FilterQueryDemo;
import org.example.domain.model.Field;
import org.example.domain.model.Production;
import org.example.domain.model.Well;
import org.example.domain.relationship.FieldWellRelation;
import org.example.domain.relationship.WellProductionRelation;
import org.example.infrastructure.Neo4jConnectionManager;
import org.example.infrastructure.Neo4jConstraintsInitializer;
import org.example.infrastructure.Neo4jDatabaseCleaner;

public class Main {
     public static void main(String[] args) {
        try {
            checkConnection();

            new Neo4jConstraintsInitializer().initialize();
            new Neo4jDatabaseCleaner().clean();

            runCrudDemo();

            System.out.println();
            System.out.println("=== ПУНКТ 2. ФИЛЬТРАЦИЯ УЗЛОВ И СВЯЗЕЙ ===");
            new FilterQueryDemo().run();

            System.out.println();
            System.out.println("=== ПУНКТ 3. СЛОЖНЫЕ ЗАПРОСЫ ===");
            new ComplexQueryDemo().run();

        } finally {
            Neo4jConnectionManager.closeDriver();
        }
    }

    private static void checkConnection() {
        Neo4jConnectionManager.verifyConnectivity();
        System.out.println("Подключение к Neo4j успешно");
    }

    private static void runCrudDemo() {
        FieldDao fieldDao = new FieldDao();
        WellDao wellDao = new WellDao();
        ProductionDao productionDao = new ProductionDao();

        Long fieldId = 1L;
        Long wellId = 1L;
        Long productionId = 1L;

        Field field = new Field(
                fieldId,
                "Самотлорское",
                "Ханты-Мансийский АО",
                61.154,
                76.684,
                2700000000.0
        );

        Well well = new Well(
                wellId,
                "СМ-101",
                "active",
                3050.0,
                0.22
        );

        Production production = new Production(
                productionId,
                LocalDate.of(2026, 3, 10),
                120.5,
                540.0,
                18.2
        );

        FieldWellRelation fieldWellRelation = new FieldWellRelation(
                LocalDate.of(2020, 1, 15),
                "active",
                "Основная эксплуатационная скважина"
        );

        WellProductionRelation wellProductionRelation = new WellProductionRelation(
                LocalDateTime.of(2026, 3, 10, 8, 30),
                "automatic",
                true
        );

        System.out.println();
        System.out.println("=== CREATE ===");

        fieldDao.create(field);
        wellDao.create(well, fieldId, fieldWellRelation);
        productionDao.create(production, wellId, wellProductionRelation);

        System.out.println("Создан узел Field");
        System.out.println("Создан узел Well и связь Field -[:HAS_WELL]-> Well");
        System.out.println("Создан узел Production и связь Well -[:HAS_PRODUCTION]-> Production");

        System.out.println();
        System.out.println("=== READ BY ID ===");

        System.out.println(fieldDao.getById(fieldId));
        System.out.println(wellDao.getById(wellId));
        System.out.println(productionDao.getById(productionId));

        System.out.println();
        System.out.println("=== GET ALL ===");

        System.out.println("Месторождения:");
        fieldDao.getAll().forEach(System.out::println);

        System.out.println("Скважины:");
        wellDao.getAll().forEach(System.out::println);

        System.out.println("Записи добычи:");
        productionDao.getAll().forEach(System.out::println);

        System.out.println();
        System.out.println("=== UPDATE ===");

        Field updatedField = new Field(
                fieldId,
                "Самотлорское",
                "ХМАО — Югра",
                61.154,
                76.684,
                2750000000.0
        );

        Well updatedWell = new Well(
                wellId,
                "СМ-101",
                "maintenance",
                3050.0,
                0.22
        );

        Production updatedProduction = new Production(
                productionId,
                LocalDate.of(2026, 3, 10),
                130.0,
                560.0,
                20.0
        );

        fieldDao.update(updatedField);
        wellDao.update(updatedWell);
        productionDao.update(updatedProduction);

        System.out.println("После обновления:");
        System.out.println(fieldDao.getById(fieldId));
        System.out.println(wellDao.getById(wellId));
        System.out.println(productionDao.getById(productionId));

        System.out.println();
        System.out.println("=== DELETE ===");

        productionDao.delete(productionId);
        wellDao.delete(wellId);
        fieldDao.delete(fieldId);

        System.out.println("Production после удаления: " + productionDao.getById(productionId));
        System.out.println("Well после удаления: " + wellDao.getById(wellId));
        System.out.println("Field после удаления: " + fieldDao.getById(fieldId));
    }
}
