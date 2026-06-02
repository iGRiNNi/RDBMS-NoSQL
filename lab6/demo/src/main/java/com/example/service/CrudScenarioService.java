package com.example.service;

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

public class CrudScenarioService {

    private final FieldDao fieldDao;
    private final WellDao wellDao;
    private final ProductionDao productionDao;

    public CrudScenarioService(
            FieldDao fieldDao,
            WellDao wellDao,
            ProductionDao productionDao
    ) {
        this.fieldDao = fieldDao;
        this.wellDao = wellDao;
        this.productionDao = productionDao;
    }

    public void run() {
        UUID fieldId = UUID.randomUUID();

        UUID producerWellId = UUID.randomUUID();
        UUID injectorWellId = UUID.randomUUID();
        UUID explorationWellId = UUID.randomUUID();

        UUID productionId = Uuids.timeBased();

        ProductionKey productionKey = new ProductionKey(
                producerWellId,
                LocalDate.of(2026, 3, 10),
                productionId
        );

        try {
            Field field = createField(fieldId);

            Well producerWell = createProducerWell(producerWellId, fieldId);
            Well injectorWell = createInjectorWell(injectorWellId, fieldId);
            Well explorationWell = createExplorationWell(explorationWellId, fieldId);

            Production production = createProduction(producerWellId, productionId);

            System.out.println();
            System.out.println("CREATE");

            fieldDao.create(field);
            wellDao.create(producerWell);
            wellDao.create(injectorWell);
            wellDao.create(explorationWell);
            productionDao.create(production);

            System.out.println("Сущности созданы");

            System.out.println();
            System.out.println("READ BY ID");

            System.out.println(fieldDao.getById(fieldId).orElse(null));
            System.out.println(wellDao.getById(producerWellId).orElse(null));
            System.out.println(productionDao.getById(productionKey).orElse(null));

            System.out.println();
            System.out.println("СВЯЗАННЫЕ ДАННЫЕ");

            System.out.println("Месторождение:");
            System.out.println(fieldDao.getById(fieldId).orElse(null));

            System.out.println("Скважины месторождения:");
            wellDao.findByFieldId(fieldId).forEach(System.out::println);

            System.out.println("Записи добычи по скважине:");
            productionDao.findByWellId(producerWellId).forEach(System.out::println);

            System.out.println();
            System.out.println("РАЗРЕЖЕННЫЕ ДАННЫЕ В wells_by_id");

            System.out.println("PRODUCER:");
            System.out.println(wellDao.getById(producerWellId).orElse(null));

            System.out.println("INJECTOR:");
            System.out.println(wellDao.getById(injectorWellId).orElse(null));

            System.out.println("EXPLORATION:");
            System.out.println(wellDao.getById(explorationWellId).orElse(null));

            System.out.println();
            System.out.println("UPDATE");

            fieldDao.update(new Field(
                    fieldId,
                    "Самотлорское",
                    "ХМАО — Югра",
                    61.154,
                    76.684,
                    LocalDate.of(1965, 5, 29)
            ));

            wellDao.update(new Well(
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
            ));

            productionDao.update(new Production(
                    producerWellId,
                    LocalDate.of(2026, 3, 10),
                    productionId,
                    130.0,
                    560.0,
                    20.0
            ));

            System.out.println(fieldDao.getById(fieldId).orElse(null));
            System.out.println(wellDao.getById(producerWellId).orElse(null));
            System.out.println(productionDao.getById(productionKey).orElse(null));

            System.out.println();
            System.out.println("DELETE");

            productionDao.delete(productionKey);
            wellDao.delete(producerWellId);
            wellDao.delete(injectorWellId);
            wellDao.delete(explorationWellId);
            fieldDao.delete(fieldId);

            System.out.println("Field после удаления: " + fieldDao.getById(fieldId).orElse(null));
            System.out.println("Well после удаления: " + wellDao.getById(producerWellId).orElse(null));
            System.out.println("Production после удаления: " + productionDao.getById(productionKey).orElse(null));

        } finally {
            productionDao.delete(productionKey);
            wellDao.delete(producerWellId);
            wellDao.delete(injectorWellId);
            wellDao.delete(explorationWellId);
            fieldDao.delete(fieldId);
        }
    }

    private Field createField(UUID fieldId) {
        return new Field(
                fieldId,
                "Самотлорское",
                "Ханты-Мансийский АО",
                61.154,
                76.684,
                LocalDate.of(1965, 5, 29)
        );
    }

    private Well createProducerWell(UUID wellId, UUID fieldId) {
        return new Well(
                wellId,
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
    }

    private Well createInjectorWell(UUID wellId, UUID fieldId) {
        return new Well(
                wellId,
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
    }

    private Well createExplorationWell(UUID wellId, UUID fieldId) {
        return new Well(
                wellId,
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
    }

    private Production createProduction(UUID wellId, UUID productionId) {
        return new Production(
                wellId,
                LocalDate.of(2026, 3, 10),
                productionId,
                120.5,
                540.0,
                18.2
        );
    }
}