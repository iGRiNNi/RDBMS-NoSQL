package org.example.dao;

import org.example.domain.document.ProductionDocument;
import org.example.infrastructure.elasticsearch.ElasticsearchIndices;

public class ProductionSearchDao extends AbstractElasticsearchCrudDao<ProductionDocument> {

    public ProductionSearchDao() {
        super(ProductionDocument.class);
    }

    @Override
    protected String getIndexName() {
        return ElasticsearchIndices.PRODUCTIONS;
    }

    @Override
    protected String getDocumentId(ProductionDocument document) {
        return document.id();
    }
}
