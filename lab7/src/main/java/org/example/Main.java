package org.example;

import org.example.app.ApplicationRunner;
import org.example.infrastructure.elasticsearch.ElasticsearchClientManager;

public class Main {

    public static void main(String[] args) {
        try {
            ApplicationRunner applicationRunner = new ApplicationRunner();
            applicationRunner.run();
        } finally {
            ElasticsearchClientManager.close();
        }
    }
}