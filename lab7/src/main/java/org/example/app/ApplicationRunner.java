package org.example.app;

import org.example.dao.FieldSearchDao;
import org.example.dao.ProductionSearchDao;
import org.example.dao.WellSearchDao;
import org.example.service.BulkDataService;
import org.example.service.CrudService;
import org.example.service.FilterService;
import org.example.service.FullTextSearchService;
import org.example.service.IndexInitializationService;
import org.example.service.ReplaceService;
import org.example.service.UpdateUpsertService;

public class ApplicationRunner {

    private final IndexInitializationService indexInitializationService;
    private final BulkDataService bulkDataService;
    private final CrudService crudService;
    private final FilterService filterService;
    private final UpdateUpsertService updateUpsertService;
    private final ReplaceService replaceService;
    private final FullTextSearchService fullTextSearchService;

    public ApplicationRunner() {
        FieldSearchDao fieldSearchDao = new FieldSearchDao();
        WellSearchDao wellSearchDao = new WellSearchDao();
        ProductionSearchDao productionSearchDao = new ProductionSearchDao();

        this.indexInitializationService = new IndexInitializationService();

        this.bulkDataService = new BulkDataService(
                fieldSearchDao,
                wellSearchDao,
                productionSearchDao
        );

        this.crudService = new CrudService(
                fieldSearchDao,
                wellSearchDao,
                productionSearchDao
        );

        this.filterService = new FilterService(
                wellSearchDao,
                productionSearchDao
        );

        this.updateUpsertService = new UpdateUpsertService(
                wellSearchDao
        );

        this.replaceService = new ReplaceService(wellSearchDao);

        this.fullTextSearchService = new FullTextSearchService(wellSearchDao);
    }

    public void run() {
        System.out.println("=== Лабораторная работа Elasticsearch ===");

        indexInitializationService.initializeIndices();

        bulkDataService.loadTestData();

        System.out.println();
        System.out.println("=== ПУНКТ 1. CRUD ===");
        crudService.run();

        System.out.println();
        System.out.println("=== ПУНКТ 3. ФИЛЬТРАЦИЯ, UPDATE BY QUERY, DELETE BY QUERY ===");
        filterService.run();

        System.out.println();
        System.out.println("=== ПУНКТ 4. UPDATE И UPSERT ===");
        updateUpsertService.run();

        System.out.println();
        System.out.println("=== ПУНКТ 5. ЧАСТИЧНОЕ ОБНОВЛЕНИЕ И ПОЛНАЯ ЗАМЕНА ===");
        replaceService.run();

        System.out.println();
        System.out.println("=== ПУНКТ 6. ПОЛНОТЕКСТОВЫЙ ПОИСК ===");
        fullTextSearchService.run();
    }
}