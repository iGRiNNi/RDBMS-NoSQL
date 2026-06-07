package org.example.dao;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch._types.Refresh;
import org.example.infrastructure.elasticsearch.ElasticsearchClientManager;

import java.util.Optional;

public abstract class AbstractElasticsearchCrudDao<T> implements CrudDao<T, String> {

    protected final ElasticsearchClient client;
    private final Class<T> documentClass;

    protected AbstractElasticsearchCrudDao(Class<T> documentClass) {
        this.client = ElasticsearchClientManager.getClient();
        this.documentClass = documentClass;
    }

    protected abstract String getIndexName();

    protected abstract String getDocumentId(T document);

    @Override
    public void create(T document) {
        try {
            client.create(request -> request
                    .index(getIndexName())
                    .id(getDocumentId(document))
                    .document(document)
                    .refresh(Refresh.WaitFor)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to create document in index " + getIndexName(), e);
        }
    }

    @Override
    public Optional<T> getById(String id) {
        try {
            GetResponse<T> response = client.get(request -> request
                    .index(getIndexName())
                    .id(id), documentClass
            );

            if (!response.found()) {
                return Optional.empty();
            }

            return Optional.ofNullable(response.source());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get document from index " + getIndexName(), e);
        }
    }

    @Override
    public void update(T document) {
        try {
            client.index(request -> request
                    .index(getIndexName())
                    .id(getDocumentId(document))
                    .document(document)
                    .refresh(Refresh.WaitFor)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to update document in index " + getIndexName(), e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            client.delete(request -> request
                    .index(getIndexName())
                    .id(id)
                    .refresh(Refresh.WaitFor)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete document from index " + getIndexName(), e);
        }
    }
}
