package org.example.infrastructure.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

public class ElasticsearchIndexInitializer {

    private final ElasticsearchClient client;
    private final ElasticsearchConfig config;

    public ElasticsearchIndexInitializer() {
        this.client = ElasticsearchClientManager.getClient();
        this.config = ElasticsearchConfig.load();
    }

    public void initialize() {
        try {
            if (config.isRecreateIndices()) {
                deleteIndexIfExists(ElasticsearchIndices.PRODUCTIONS);
                deleteIndexIfExists(ElasticsearchIndices.WELLS);
                deleteIndexIfExists(ElasticsearchIndices.FIELDS);
            }

            createFieldsIndexIfNotExists();
            createWellsIndexIfNotExists();
            createProductionsIndexIfNotExists();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Elasticsearch indices", e);
        }
    }

    private void deleteIndexIfExists(String indexName) throws Exception {
        boolean exists = client.indices()
                .exists(request -> request.index(indexName))
                .value();

        if (exists) {
            client.indices().delete(request -> request.index(indexName));
        }
    }

    private void createFieldsIndexIfNotExists() throws Exception {
        if (indexExists(ElasticsearchIndices.FIELDS)) {
            return;
        }

        client.indices().create(request -> request
                .index(ElasticsearchIndices.FIELDS)
                .mappings(mapping -> mapping
                        .properties("id", property -> property.keyword(keyword -> keyword))
                        .properties("name", property -> property.text(text -> text
                                .fields("keyword", field -> field.keyword(keyword -> keyword))
                        ))
                        .properties("region", property -> property.keyword(keyword -> keyword))
                        .properties("latitude", property -> property.double_(number -> number))
                        .properties("longitude", property -> property.double_(number -> number))
                        .properties("startDate", property -> property.date(date -> date))
                        .properties("description", property -> property.text(text -> text))
                )
        );
    }

    private void createWellsIndexIfNotExists() throws Exception {
        if (indexExists(ElasticsearchIndices.WELLS)) {
            return;
        }

        client.indices().create(request -> request
                .index(ElasticsearchIndices.WELLS)
                .mappings(mapping -> mapping
                        .properties("id", property -> property.keyword(keyword -> keyword))
                        .properties("fieldId", property -> property.keyword(keyword -> keyword))
                        .properties("fieldName", property -> property.text(text -> text
                                .fields("keyword", field -> field.keyword(keyword -> keyword))
                        ))
                        .properties("region", property -> property.keyword(keyword -> keyword))
                        .properties("number", property -> property.keyword(keyword -> keyword))
                        .properties("type", property -> property.keyword(keyword -> keyword))
                        .properties("status", property -> property.keyword(keyword -> keyword))
                        .properties("depth", property -> property.double_(number -> number))
                        .properties("diameter", property -> property.double_(number -> number))
                        .properties("commissioningDate", property -> property.date(date -> date))
                        .properties("description", property -> property.text(text -> text))
                )
        );
    }

    private void createProductionsIndexIfNotExists() throws Exception {
        if (indexExists(ElasticsearchIndices.PRODUCTIONS)) {
            return;
        }

        client.indices().create(request -> request
                .index(ElasticsearchIndices.PRODUCTIONS)
                .mappings(mapping -> mapping
                        .properties("id", property -> property.keyword(keyword -> keyword))
                        .properties("fieldId", property -> property.keyword(keyword -> keyword))
                        .properties("fieldName", property -> property.text(text -> text
                                .fields("keyword", field -> field.keyword(keyword -> keyword))
                        ))
                        .properties("wellId", property -> property.keyword(keyword -> keyword))
                        .properties("wellNumber", property -> property.keyword(keyword -> keyword))
                        .properties("productionDate", property -> property.date(date -> date))
                        .properties("oil", property -> property.double_(number -> number))
                        .properties("gas", property -> property.double_(number -> number))
                        .properties("water", property -> property.double_(number -> number))
                        .properties("comment", property -> property.text(text -> text))
                )
        );
    }

    private boolean indexExists(String indexName) throws Exception {
        return client.indices()
                .exists(request -> request.index(indexName))
                .value();
    }
}