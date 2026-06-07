package org.example.dao;

import org.example.domain.document.WellDocument;
import org.example.infrastructure.elasticsearch.ElasticsearchIndices;

public class WellSearchDao extends AbstractElasticsearchCrudDao<WellDocument> {

    public WellSearchDao() {
        super(WellDocument.class);
    }

    @Override
    protected String getIndexName() {
        return ElasticsearchIndices.WELLS;
    }

    @Override
    protected String getDocumentId(WellDocument document) {
        return document.id();
    }
}
