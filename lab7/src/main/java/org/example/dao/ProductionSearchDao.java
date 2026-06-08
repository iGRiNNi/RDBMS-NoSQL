package org.example.dao;

import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;
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

    public long deleteByOilLessThan(double oilLimit) {
        try {
            DeleteByQueryResponse response = client.deleteByQuery(request -> request
                    .index(getIndexName())
                    .query(query -> query
                            .range(range -> range
                                    .number(number -> number
                                            .field("oil")
                                            .lt(oilLimit)
                                    )
                            )
                    )
                    .refresh(true)
            );

            return response.deleted();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete productions by oil limit", e);
        }
    }
}