package org.example.dao;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateByQueryResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import org.example.domain.document.WellDocument;
import org.example.dto.SearchResultDto;
import org.example.infrastructure.elasticsearch.ElasticsearchIndices;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public List<WellDocument> findActiveWellsByTypeAndMinDepth(String type, double minDepth) {
        try {
            SearchResponse<WellDocument> response = client.search(request -> request
                            .index(getIndexName())
                            .query(query -> query
                                    .bool(bool -> bool
                                            .filter(filter -> filter
                                                    .term(term -> term
                                                            .field("status")
                                                            .value("ACTIVE")
                                                    )
                                            )
                                            .filter(filter -> filter
                                                    .term(term -> term
                                                            .field("type")
                                                            .value(type)
                                                    )
                                            )
                                            .filter(filter -> filter
                                                    .range(range -> range
                                                            .number(number -> number
                                                                    .field("depth")
                                                                    .gt(minDepth)
                                                            )
                                                    )
                                            )
                                    )
                            )
                            .sort(sort -> sort
                                    .field(field -> field
                                            .field("depth")
                                            .order(SortOrder.Desc)
                                    )
                            ),
                    WellDocument.class
            );

            return response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("Failed to find active wells by type and depth", e);
        }
    }

    public long updateStatusByTypeAndMinDepth(String type, double minDepth, String newStatus) {
        try {
            UpdateByQueryResponse response = client.updateByQuery(request -> request
                    .index(getIndexName())
                    .query(query -> query
                            .bool(bool -> bool
                                    .filter(filter -> filter
                                            .term(term -> term
                                                    .field("status")
                                                    .value("ACTIVE")
                                            )
                                    )
                                    .filter(filter -> filter
                                            .term(term -> term
                                                    .field("type")
                                                    .value(type)
                                            )
                                    )
                                    .filter(filter -> filter
                                            .range(range -> range
                                                    .number(number -> number
                                                            .field("depth")
                                                            .gt(minDepth)
                                                    )
                                            )
                                    )
                            )
                    )
                    .script(script -> script
                            .lang("painless")
                            .source("ctx._source.status = params['newStatus']")
                            .params("newStatus", JsonData.of(newStatus))
                    )
                    .refresh(true)
            );

            return response.updated();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update well status by query", e);
        }
    }

    public void partialUpdateStatus(String id, String newStatus) {
        try {
            Map<String, Object> partialDocument = new HashMap<>();
            partialDocument.put("status", newStatus);

            UpdateResponse<WellDocument> response = client.update(request -> request
                            .index(getIndexName())
                            .id(id)
                            .doc(partialDocument)
                            .refresh(Refresh.WaitFor),
                    WellDocument.class
            );

            System.out.println("Update result: " + response.result());

        } catch (Exception e) {
            throw new RuntimeException("Failed to update well status by id: " + id, e);
        }
    }

    public void upsert(WellDocument document) {
        try {
            UpdateResponse<WellDocument> response = client.update(request -> request
                            .index(getIndexName())
                            .id(document.id())
                            .doc(document)
                            .docAsUpsert(true)
                            .refresh(Refresh.WaitFor),
                    WellDocument.class
            );

            System.out.println("Upsert result: " + response.result());

        } catch (Exception e) {
            throw new RuntimeException("Failed to upsert well: " + document.id(), e);
        }
    }

    public void partialUpdate(String id, Map<String, Object> fields) {
        try {
            UpdateResponse<WellDocument> response = client.update(request -> request
                            .index(getIndexName())
                            .id(id)
                            .doc(fields)
                            .refresh(Refresh.WaitFor),
                    WellDocument.class
            );

            System.out.println("Partial update result: " + response.result());

        } catch (Exception e) {
            throw new RuntimeException("Failed to partially update well: " + id, e);
        }
    }

    public void replaceDocument(String id, Map<String, Object> replacement) {
        try {
            IndexResponse response = client.index(request -> request
                    .index(getIndexName())
                    .id(id)
                    .document(replacement)
                    .refresh(Refresh.WaitFor)
            );

            System.out.println("Replace result: " + response.result());

        } catch (Exception e) {
            throw new RuntimeException("Failed to replace well document: " + id, e);
        }
    }

    public List<SearchResultDto<WellDocument>> searchByDescription(String text) {
        try {
            SearchResponse<WellDocument> response = client.search(request -> request
                            .index(getIndexName())
                            .query(query -> query
                                    .match(match -> match
                                            .field("description")
                                            .query(text)
                                    )
                            )
                            .highlight(highlight -> highlight
                                    .preTags("<mark>")
                                    .postTags("</mark>")
                                    .fields("description", field -> field)
                            ),
                    WellDocument.class
            );

            return mapSearchResults(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to search wells by description", e);
        }
    }

    public List<SearchResultDto<WellDocument>> multiMatchSearch(String text) {
        try {
            SearchResponse<WellDocument> response = client.search(request -> request
                            .index(getIndexName())
                            .query(query -> query
                                    .multiMatch(multiMatch -> multiMatch
                                            .query(text)
                                            .fields("number")
                                            .fields("fieldName")
                                            .fields("description")
                                    )
                            )
                            .highlight(highlight -> highlight
                                    .preTags("<mark>")
                                    .postTags("</mark>")
                                    .fields("number", field -> field)
                                    .fields("fieldName", field -> field)
                                    .fields("description", field -> field)
                            ),
                    WellDocument.class
            );

            return mapSearchResults(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute multi match search", e);
        }
    }

    public List<SearchResultDto<WellDocument>> fuzzySearchByDescription(String text) {
        try {
            SearchResponse<WellDocument> response = client.search(request -> request
                            .index(getIndexName())
                            .query(query -> query
                                    .match(match -> match
                                            .field("description")
                                            .query(text)
                                            .fuzziness("AUTO")
                                    )
                            )
                            .highlight(highlight -> highlight
                                    .preTags("<mark>")
                                    .postTags("</mark>")
                                    .fields("description", field -> field)
                            ),
                    WellDocument.class
            );

            return mapSearchResults(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute fuzzy search", e);
        }
    }

    public List<SearchResultDto<WellDocument>> boolSearch(
            String text,
            String type,
            String status,
            double minDepth,
            int from,
            int size
    ) {
        try {
            SearchResponse<WellDocument> response = client.search(request -> request
                            .index(getIndexName())
                            .from(from)
                            .size(size)
                            .query(query -> query
                                    .bool(bool -> bool
                                            .must(must -> must
                                                    .match(match -> match
                                                            .field("description")
                                                            .query(text)
                                                    )
                                            )
                                            .filter(filter -> filter
                                                    .term(term -> term
                                                            .field("type")
                                                            .value(type)
                                                    )
                                            )
                                            .filter(filter -> filter
                                                    .term(term -> term
                                                            .field("status")
                                                            .value(status)
                                                    )
                                            )
                                            .filter(filter -> filter
                                                    .range(range -> range
                                                            .number(number -> number
                                                                    .field("depth")
                                                                    .gte(minDepth)
                                                            )
                                                    )
                                            )
                                    )
                            )
                            .sort(sort -> sort
                                    .field(field -> field
                                            .field("depth")
                                            .order(SortOrder.Desc)
                                    )
                            )
                            .highlight(highlight -> highlight
                                    .preTags("<mark>")
                                    .postTags("</mark>")
                                    .fields("description", field -> field)
                            ),
                    WellDocument.class
            );

            return mapSearchResults(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute bool search", e);
        }
    }

    private List<SearchResultDto<WellDocument>> mapSearchResults(SearchResponse<WellDocument> response) {
        return response.hits()
                .hits()
                .stream()
                .map(this::mapSearchHit)
                .filter(Objects::nonNull)
                .toList();
    }

    private SearchResultDto<WellDocument> mapSearchHit(Hit<WellDocument> hit) {
        WellDocument source = hit.source();

        if (source == null) {
            return null;
        }

        Map<String, List<String>> highlights = hit.highlight() == null
                ? Collections.emptyMap()
                : hit.highlight();

        return new SearchResultDto<>(
                hit.id(),
                source,
                highlights
        );
    }
}