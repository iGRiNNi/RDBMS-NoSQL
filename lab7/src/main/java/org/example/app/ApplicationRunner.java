package org.example.app;

import org.example.dao.FieldSearchDao;
import org.example.dao.ProductionSearchDao;
import org.example.dao.WellSearchDao;
import org.example.service.CrudScenarioService;
import org.example.service.IndexInitializationService;

public class ApplicationRunner {

    private final IndexInitializationService indexInitializationService;
    private final CrudScenarioService crudScenarioService;

    public ApplicationRunner() {
        FieldSearchDao fieldSearchDao = new FieldSearchDao();
        WellSearchDao wellSearchDao = new WellSearchDao();
        ProductionSearchDao productionSearchDao = new ProductionSearchDao();

        this.indexInitializationService = new IndexInitializationService();

        this.crudScenarioService = new CrudScenarioService(
                fieldSearchDao,
                wellSearchDao,
                productionSearchDao
        );
    }

    public void run() {
        System.out.println("=== Лабораторная работа Elasticsearch ===");

        indexInitializationService.initializeIndices();

        System.out.println();
        System.out.println("=== ПУНКТ 1. CRUD ДЛЯ 3 СВЯЗАННЫХ СУЩНОСТЕЙ ===");

        crudScenarioService.run();
    }
}