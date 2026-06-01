package com.example.demo;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.dao.FieldDao;
import com.example.dao.ProductionDao;
import com.example.dao.WellDao;
import com.example.domain.key.ProductionKey;
import com.example.domain.model.Field;
import com.example.domain.model.Production;
import com.example.domain.model.Well;

import java.time.LocalDate;
import java.util.UUID;

public final class CrudDemo {

    private CrudDemo() {
    }

    public static void run() {
        FieldDao fieldDao = new FieldDao();
        WellDao wellDao = new WellDao();
        ProductionDao productionDao = new ProductionDao();

        UUID fieldId = UUID.randomUUID();

        UUID producerWellId = UUID.randomUUID();
        UUID injectorWellId = UUID.randomUUID();
        UUID explorationWellId = UUID.randomUUID();

        UUID productionId = Uuids.timeBased();

        Field field = new Field(
                fieldId,
                "Самотлорское",
                "Ханты-Мансийский АО",
                61.154,
                76.684,
                LocalDate.of(1965, 5, 29)
        );

        Well producerWell = new Well(
                producerWellId,
                fieldId,
                "СМ-101",
                "ACTIVE",
                "PRODUCER",
                3050.0,
                0.22,
                "ESP",
                120.0,
                null,
                null,
                null,
                null
        );

        Well injectorWell = new Well(
                injectorWellId,
                fieldId,
                "СМ-201",
                "ACTIVE",
                "INJECTOR",
                2900.0,
                0.20,
                null,
                null,
                15.5,
                500.0,
                null,
                null
        );

        Well explorationWell = new Well(
                explorationWellId,
                fieldId,
                "СМ-301",
                "PLANNED",
                "EXPLORATION",
                1500.0,
                0.16,
                null,
                null,
                null,
                null,
                1450.0,
                "HIGH"
        );

        Production production = new Production(
                producerWellId,
                LocalDate.of(2026, 3, 10),
                productionId,
                120.5,
                540.0,
                18.2
        );

        ProductionKey productionKey = new ProductionKey(
                producerWellId,
                LocalDate.of(2026, 3, 10),
                productionId
        );

        System.out.println();
        System.out.println("=== ПУНКТ 1. CREATE ===");

        fieldDao.create(field);
        wellDao.create(producerWell);
        wellDao.create(injectorWell);
        wellDao.create(explorationWell);
        productionDao.create(production);

        System.out.println("Сущности созданы");

        System.out.println();
        System.out.println("=== ПУНКТ 1. READ BY ID ===");

        System.out.println(fieldDao.getById(fieldId).orElse(null));
        System.out.println(wellDao.getById(producerWellId).orElse(null));
        System.out.println(productionDao.getById(productionKey).orElse(null));

        System.out.println();
        System.out.println("=== ПУНКТ 1. СВЯЗАННЫЕ ДАННЫЕ ===");

        System.out.println("Месторождение:");
        System.out.println(fieldDao.getById(fieldId).orElse(null));

        System.out.println("Скважины месторождения:");
        wellDao.findByFieldId(fieldId).forEach(System.out::println);

        System.out.println("Записи добычи по скважине:");
        productionDao.findByWellId(producerWellId).forEach(System.out::println);

        System.out.println();
        System.out.println("=== ПУНКТ 1. РАЗРЕЖЕННЫЕ ДАННЫЕ В ОДНОМ СЕМЕЙСТВЕ ===");

        System.out.println("PRODUCER:");
        System.out.println(wellDao.getById(producerWellId).orElse(null));

        System.out.println("INJECTOR:");
        System.out.println(wellDao.getById(injectorWellId).orElse(null));

        System.out.println("EXPLORATION:");
        System.out.println(wellDao.getById(explorationWellId).orElse(null));

        System.out.println();
        System.out.println("=== ПУНКТ 1. UPDATE ===");

        Field updatedField = new Field(
                fieldId,
                "Самотлорское",
                "ХМАО — Югра",
                61.154,
                76.684,
                LocalDate.of(1965, 5, 29)
        );

        Well updatedProducerWell = new Well(
                producerWellId,
                fieldId,
                "СМ-101",
                "MAINTENANCE",
                "PRODUCER",
                3050.0,
                0.22,
                "ESP",
                130.0,
                null,
                null,
                null,
                null
        );

        Production updatedProduction = new Production(
                producerWellId,
                LocalDate.of(2026, 3, 10),
                productionId,
                130.0,
                560.0,
                20.0
        );

        fieldDao.update(updatedField);
        wellDao.update(updatedProducerWell);
        productionDao.update(updatedProduction);

        System.out.println(fieldDao.getById(fieldId).orElse(null));
        System.out.println(wellDao.getById(producerWellId).orElse(null));
        System.out.println(productionDao.getById(productionKey).orElse(null));

        System.out.println();
        System.out.println("=== ПУНКТ 1. DELETE ===");

        productionDao.delete(productionKey);
        wellDao.delete(producerWellId);
        wellDao.delete(injectorWellId);
        wellDao.delete(explorationWellId);
        fieldDao.delete(fieldId);

        System.out.println("Field после удаления: " + fieldDao.getById(fieldId).orElse(null));
        System.out.println("Well после удаления: " + wellDao.getById(producerWellId).orElse(null));
        System.out.println("Production после удаления: " + productionDao.getById(productionKey).orElse(null));
    }
}