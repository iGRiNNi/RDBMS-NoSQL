package com.example;

import com.example.dao.FieldDao;
import com.example.dao.ProductionDao;
import com.example.dao.WellDao;
import com.example.service.CrudScenarioService;
import com.example.service.FilterQueryService;
import com.example.service.SparseColumnQueryService;

public class ApplicationRunner {

    private final CrudScenarioService crudScenarioService;
    private final FilterQueryService filterQueryService;
    private final SparseColumnQueryService sparseColumnQueryService;

    public ApplicationRunner() {
        FieldDao fieldDao = new FieldDao();
        WellDao wellDao = new WellDao();
        ProductionDao productionDao = new ProductionDao();

        this.crudScenarioService = new CrudScenarioService(
                fieldDao,
                wellDao,
                productionDao
        );

        this.filterQueryService = new FilterQueryService(
                fieldDao,
                wellDao
        );

        this.sparseColumnQueryService = new SparseColumnQueryService(
                fieldDao,
                wellDao
        );
    }

    public void run() {
        System.out.println();
        System.out.println("CRUD");
        crudScenarioService.run();

        System.out.println();
        System.out.println("ЗАПРОСЫ С ФИЛЬТРАЦИЕЙ");
        filterQueryService.run();

        System.out.println();
        System.out.println("ЗАПРОСЫ ПО РАЗРЕЖЕННЫМ СТОЛБЦАМ");
        sparseColumnQueryService.run();
    }
}