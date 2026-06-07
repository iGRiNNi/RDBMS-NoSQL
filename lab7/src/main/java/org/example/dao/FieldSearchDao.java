package org.example.dao;

import org.example.domain.document.FieldDocument;
import org.example.infrastructure.elasticsearch.ElasticsearchIndices;

public class FieldSearchDao extends AbstractElasticsearchCrudDao<FieldDocument> {

    public FieldSearchDao() {
        super(FieldDocument.class);
    }

    @Override
    protected String getIndexName() {
        return ElasticsearchIndices.FIELDS;
    }

    @Override
    protected String getDocumentId(FieldDocument document) {
        return document.id();
    }
}
