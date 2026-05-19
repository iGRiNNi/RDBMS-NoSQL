package com.example.infrastructure.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class MongoConnectionManager {

    private static final MongoConfig CONFIG = MongoConfig.load();

    private static final MongoClient CLIENT =
            MongoClients.create(CONFIG.getConnectionString());

    private MongoConnectionManager() {
    }

    public static MongoDatabase getDatabase() {
        return CLIENT.getDatabase(CONFIG.getDatabaseName());
    }

    public static void closeClient() {
        CLIENT.close();
    }
}