package com.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class UpdateVsUpsertDemo {

    private final MongoCollection<Document> wellsCollection;

    private ObjectId updateOnlyId;
    private ObjectId upsertNewId;
    private ObjectId existingId;
    private ObjectId demoFieldId;

    public UpdateVsUpsertDemo() {
        MongoDatabase database = MongoConnectionManager.getDatabase();
        this.wellsCollection = database.getCollection(MongoCollections.WELLS);
    }

    public void run() {
        initIds();
        cleanup();

        showUpdateWithoutUpsert();
        showUpdateWithUpsert();
        showUpsertForExistingDocument();
    }

    private void initIds() {
        updateOnlyId = new ObjectId();
        upsertNewId = new ObjectId();
        existingId = new ObjectId();
        demoFieldId = new ObjectId();
    }

    private void cleanup() {
        wellsCollection.deleteMany(
                in("number", "TEST-100", "TEST-200", "TEST-300")
        );
    }

    private void showUpdateWithoutUpsert() {
        System.out.println();
        System.out.println("=== UPDATE БЕЗ UPSERT ===");
        System.out.println("Пытаемся обновить скважину с _id = " + updateOnlyId + ", которой нет в БД");

        UpdateResult result = wellsCollection.updateOne(
                eq("_id", updateOnlyId),
                combine(
                        set("number", "TEST-100"),
                        set("status", "active"),
                        set("depth", 3000.0),
                        set("diameter", 0.22),
                        set("fieldId", demoFieldId)
                )
        );

        System.out.println("Найдено документов: " + result.getMatchedCount());
        System.out.println("Изменено документов: " + result.getModifiedCount());
        System.out.println("Созданный id: " + result.getUpsertedId());

        Document document = wellsCollection.find(eq("_id", updateOnlyId)).first();

        System.out.println("Документ после update без upsert:");
        System.out.println(document);
    }

    private void showUpdateWithUpsert() {
        System.out.println();
        System.out.println("=== UPDATE С UPSERT ===");
        System.out.println("Пытаемся обновить скважину с _id = " + upsertNewId + ", которой нет в БД");

        UpdateOptions options = new UpdateOptions().upsert(true);

        UpdateResult result = wellsCollection.updateOne(
                eq("_id", upsertNewId),
                combine(
                        set("number", "TEST-200"),
                        set("status", "active"),
                        set("depth", 3200.0),
                        set("diameter", 0.25),
                        set("fieldId", demoFieldId)
                ),
                options
        );

        System.out.println("Найдено документов: " + result.getMatchedCount());
        System.out.println("Изменено документов: " + result.getModifiedCount());
        System.out.println("Созданный id: " + result.getUpsertedId());

        Document document = wellsCollection.find(eq("_id", upsertNewId)).first();

        System.out.println("Документ после update с upsert:");
        System.out.println(document.toJson());
    }

    private void showUpsertForExistingDocument() {
        System.out.println();
        System.out.println("=== UPSERT ДЛЯ СУЩЕСТВУЮЩЕГО ДОКУМЕНТА ===");

        wellsCollection.insertOne(new Document()
                .append("_id", existingId)
                .append("number", "TEST-300")
                .append("status", "active")
                .append("depth", 2800.0)
                .append("diameter", 0.20)
                .append("fieldId", demoFieldId));

        System.out.println("Документ до upsert:");
        System.out.println(wellsCollection.find(eq("_id", existingId)).first().toJson());

        UpdateOptions options = new UpdateOptions().upsert(true);

        UpdateResult result = wellsCollection.updateOne(
                eq("_id", existingId),
                combine(
                        set("status", "maintenance"),
                        set("depth", 2850.0)
                ),
                options
        );

        System.out.println("Найдено документов: " + result.getMatchedCount());
        System.out.println("Изменено документов: " + result.getModifiedCount());
        System.out.println("Созданный id: " + result.getUpsertedId());

        System.out.println("Документ после upsert:");
        System.out.println(wellsCollection.find(eq("_id", existingId)).first().toJson());
    }
}
