package org.example.infrastructure.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public final class ElasticsearchClientManager {

    private static final ElasticsearchConfig CONFIG = ElasticsearchConfig.load();

    private static RestClient restClient;
    private static ElasticsearchTransport transport;
    private static ElasticsearchClient client;

    private ElasticsearchClientManager() {
    }

    public static synchronized ElasticsearchClient getClient() {
        if (client == null) {
            restClient = RestClient.builder(
                    new HttpHost(
                            CONFIG.getHost(),
                            CONFIG.getPort(),
                            CONFIG.getScheme()
                    )
            ).build();

            transport = new RestClientTransport(
                    restClient,
                    new JacksonJsonpMapper()
            );

            client = new ElasticsearchClient(transport);
        }

        return client;
    }

    public static synchronized void close() {
        try {
            if (transport != null) {
                transport.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to close Elasticsearch transport", e);
        }
    }
}