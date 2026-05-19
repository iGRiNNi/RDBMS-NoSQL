package com.example;

import com.example.dao.FieldDao;
import com.example.dao.ProductionDao;
import com.example.dao.WellDao;
import com.example.domain.model.Field;
import com.example.domain.model.Production;
import com.example.domain.model.Well;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        try {
            runCrudDemo();

            System.out.println();
            System.out.println("=== ПУНКТ 2. ЗАПРОСЫ С ФИЛЬТРАЦИЕЙ ===");
            new FilterQueryDemo().run();

            System.out.println();
            System.out.println("=== ПУНКТ 3. UPDATE И UPSERT ===");
            new UpdateVsUpsertDemo().run();

            System.out.println();
            System.out.println("=== ПУНКТ 4. UPDATE ONE И REPLACE ONE ===");
            new UpdateOneVsReplaceOneDemo().run();

        } finally {
            MongoConnectionManager.closeClient();
        }
    }

    private static void runCrudDemo() {
        FieldDao fieldDao = new FieldDao();
        WellDao wellDao = new WellDao();
        ProductionDao productionDao = new ProductionDao();

        cleanup();

        Field field = new Field(
                null,
                "Самотлорское",
                "Ханты-Мансийский АО",
                61.154,
                76.684
        );

        System.out.println("=== CREATE ===");

        ObjectId fieldId = fieldDao.create(field);

        Well well = new Well(
                null,
                "СМ-101",
                "active",
                3050.0,
                0.22,
                fieldId
        );

        ObjectId wellId = wellDao.create(well);

        Production production = new Production(
                null,
                wellId,
                LocalDate.of(2026, 3, 10),
                120.5,
                540.0,
                18.2
        );

        ObjectId productionId = productionDao.create(production);

        System.out.println("Сущности созданы");
        System.out.println("Field id: " + fieldId);
        System.out.println("Well id: " + wellId);
        System.out.println("Production id: " + productionId);

        System.out.println();
        System.out.println("=== READ BY ID ===");

        System.out.println(fieldDao.getById(fieldId));
        System.out.println(wellDao.getById(wellId));
        System.out.println(productionDao.getById(productionId));

        System.out.println();
        System.out.println("=== UPDATE ===");

        Field updatedField = new Field(
                fieldId,
                "Самотлорское",
                "ХМАО — Югра",
                61.154,
                76.684
        );

        Well updatedWell = new Well(
                wellId,
                "СМ-101",
                "maintenance",
                3050.0,
                0.22,
                fieldId
        );

        Production updatedProduction = new Production(
                productionId,
                wellId,
                LocalDate.of(2026, 3, 10),
                130.0,
                560.0,
                20.0
        );

        fieldDao.update(updatedField);
        wellDao.update(updatedWell);
        productionDao.update(updatedProduction);

        System.out.println(fieldDao.getById(fieldId));
        System.out.println(wellDao.getById(wellId));
        System.out.println(productionDao.getById(productionId));

        System.out.println();
        System.out.println("=== GET ALL ===");

        fieldDao.getAll().forEach(System.out::println);
        wellDao.getAll().forEach(System.out::println);
        productionDao.getAll().forEach(System.out::println);

        System.out.println();
        System.out.println("=== DELETE ===");

        productionDao.delete(productionId);
        wellDao.delete(wellId);
        fieldDao.delete(fieldId);

        System.out.println("Field после удаления: " + fieldDao.getById(fieldId));
        System.out.println("Well после удаления: " + wellDao.getById(wellId));
        System.out.println("Production после удаления: " + productionDao.getById(productionId));
    }

    private static void cleanup() {
        MongoDatabase database = MongoConnectionManager.getDatabase();

        database.getCollection(MongoCollections.PRODUCTIONS)
                .deleteMany(new Document());

        database.getCollection(MongoCollections.WELLS)
                .deleteMany(new Document());

        database.getCollection(MongoCollections.FIELDS)
                .deleteMany(new Document());
    }
}