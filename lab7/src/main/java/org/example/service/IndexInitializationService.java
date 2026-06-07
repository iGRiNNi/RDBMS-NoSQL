package org.example.service;

import org.example.infrastructure.elasticsearch.ElasticsearchIndexInitializer;

public class IndexInitializationService {

    private final ElasticsearchIndexInitializer indexInitializer;

    public IndexInitializationService() {
        this.indexInitializer = new ElasticsearchIndexInitializer();
    }

    public void initializeIndices() {
        System.out.println();
        System.out.println("=== ИНИЦИАЛИЗАЦИЯ ИНДЕКСОВ ===");

        indexInitializer.initialize();

        System.out.println("Индексы fields, wells, productions созданы");
    }
}