package com.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class UpdateOneVsReplaceOneDemo {

    private final MongoCollection<Document> wellsCollection;

    private ObjectId demoWellId;
    private ObjectId demoFieldId;

    public UpdateOneVsReplaceOneDemo() {
        MongoDatabase database = MongoConnectionManager.getDatabase();
        this.wellsCollection = database.getCollection(MongoCollections.WELLS);
    }

    public void run() {
        cleanup();

        showUpdateOne();
        showReplaceOne();
    }

    private void cleanup() {
        wellsCollection.deleteMany(eq("number", "TEST-400"));
    }

    private void showUpdateOne() {
        System.out.println();
        System.out.println("=== UPDATE ONE ===");

        demoWellId = new ObjectId();
        demoFieldId = new ObjectId();

        wellsCollection.insertOne(new Document()
                .append("_id", demoWellId)
                .append("number", "TEST-400")
                .append("status", "active")
                .append("depth", 3000.0)
                .append("diameter", 0.22)
                .append("fieldId", demoFieldId)
                .append("comment", "Исходный документ"));

        System.out.println("Документ до updateOne:");
        printDocument(demoWellId);

        UpdateResult result = wellsCollection.updateOne(
                eq("_id", demoWellId),
                combine(
                        set("status", "maintenance"),
                        set("lastInspectionDate", "2026-05-19")
                )
        );

        System.out.println("Найдено документов: " + result.getMatchedCount());
        System.out.println("Изменено документов: " + result.getModifiedCount());

        System.out.println("Документ после updateOne:");
        printDocument(demoWellId);
    }

    private void showReplaceOne() {
        System.out.println();
        System.out.println("=== REPLACE ONE ===");

        Document replacement = new Document()
                .append("_id", demoWellId)
                .append("number", "TEST-400")
                .append("status", "disabled")
                .append("fieldId", demoFieldId);

        System.out.println("Документ до replaceOne:");
        printDocument(demoWellId);

        UpdateResult result = wellsCollection.replaceOne(
                eq("_id", demoWellId),
                replacement
        );

        System.out.println("Найдено документов: " + result.getMatchedCount());
        System.out.println("Изменено документов: " + result.getModifiedCount());

        System.out.println("Документ после replaceOne:");
        printDocument(demoWellId);
    }

    private void printDocument(ObjectId id) {
        Document document = wellsCollection.find(eq("_id", id)).first();

        if (document == null) {
            System.out.println("Документ не найден");
        } else {
            System.out.println(document.toJson());
        }

        System.out.println();
    }
}
