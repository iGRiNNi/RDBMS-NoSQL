package org.example.service;

import org.example.dao.FieldSearchDao;
import org.example.dao.ProductionSearchDao;
import org.example.dao.WellSearchDao;
import org.example.domain.document.FieldDocument;
import org.example.domain.document.ProductionDocument;
import org.example.domain.document.WellDocument;

public class CrudScenarioService {

    private final FieldSearchDao fieldSearchDao;
    private final WellSearchDao wellSearchDao;
    private final ProductionSearchDao productionSearchDao;

    public CrudScenarioService(
            FieldSearchDao fieldSearchDao,
            WellSearchDao wellSearchDao,
            ProductionSearchDao productionSearchDao
    ) {
        this.fieldSearchDao = fieldSearchDao;
        this.wellSearchDao = wellSearchDao;
        this.productionSearchDao = productionSearchDao;
    }

    public void run() {
        FieldDocument field = new FieldDocument(
                "field-1",
                "Самотлорское",
                "Ханты-Мансийский АО",
                61.154,
                76.684,
                "1965-05-29",
                "Крупное нефтяное месторождение в Западной Сибири"
        );

        WellDocument well = new WellDocument(
                "well-1",
                "field-1",
                "Самотлорское",
                "Ханты-Мансийский АО",
                "СМ-101",
                "PRODUCER",
                "ACTIVE",
                3050.0,
                0.22,
                "2020-05-12",
                "Добывающая скважина с электроцентробежным насосом. Стабильная добыча нефти."
        );

        ProductionDocument production = new ProductionDocument(
                "production-1",
                "field-1",
                "Самотлорское",
                "well-1",
                "СМ-101",
                "2026-03-10",
                120.5,
                540.0,
                18.2,
                "Стабильный режим добычи без превышения обводненности"
        );

        System.out.println();
        System.out.println("=== CREATE ===");

        fieldSearchDao.create(field);
        wellSearchDao.create(well);
        productionSearchDao.create(production);

        System.out.println("Документы созданы");

        System.out.println();
        System.out.println("=== READ BY ID ===");

        System.out.println(fieldSearchDao.getById("field-1").orElse(null));
        System.out.println(wellSearchDao.getById("well-1").orElse(null));
        System.out.println(productionSearchDao.getById("production-1").orElse(null));

        System.out.println();
        System.out.println("=== UPDATE / REPLACE DOCUMENT ===");

        FieldDocument updatedField = new FieldDocument(
                "field-1",
                "Самотлорское",
                "ХМАО — Югра",
                61.154,
                76.684,
                "1965-05-29",
                "Крупное нефтяное месторождение в Западной Сибири"
        );

        WellDocument updatedWell = new WellDocument(
                "well-1",
                "field-1",
                "Самотлорское",
                "ХМАО — Югра",
                "СМ-101",
                "PRODUCER",
                "MAINTENANCE",
                3050.0,
                0.22,
                "2020-05-12",
                "Добывающая скважина переведена в режим обслуживания"
        );

        ProductionDocument updatedProduction = new ProductionDocument(
                "production-1",
                "field-1",
                "Самотлорское",
                "well-1",
                "СМ-101",
                "2026-03-10",
                130.0,
                560.0,
                20.0,
                "Показатели добычи обновлены"
        );

        fieldSearchDao.update(updatedField);
        wellSearchDao.update(updatedWell);
        productionSearchDao.update(updatedProduction);

        System.out.println(fieldSearchDao.getById("field-1").orElse(null));
        System.out.println(wellSearchDao.getById("well-1").orElse(null));
        System.out.println(productionSearchDao.getById("production-1").orElse(null));

        System.out.println();
        System.out.println("=== DELETE ===");

        productionSearchDao.delete("production-1");
        wellSearchDao.delete("well-1");
        fieldSearchDao.delete("field-1");

        System.out.println("Field после удаления: " + fieldSearchDao.getById("field-1").orElse(null));
        System.out.println("Well после удаления: " + wellSearchDao.getById("well-1").orElse(null));
        System.out.println("Production после удаления: " + productionSearchDao.getById("production-1").orElse(null));
    }
}
