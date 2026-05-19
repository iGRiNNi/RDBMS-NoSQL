package com.example;

import com.example.domain.model.Field;
import com.example.domain.model.Production;
import com.example.domain.model.Well;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import com.example.mapper.FieldMapper;
import com.example.mapper.ProductionMapper;
import com.example.mapper.WellMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.set;

public class FilterQueryDemo {

    private final MongoCollection<Document> fieldsCollection;
    private final MongoCollection<Document> wellsCollection;
    private final MongoCollection<Document> productionsCollection;

    public FilterQueryDemo() {
        MongoDatabase database = MongoConnectionManager.getDatabase();

        this.fieldsCollection = database.getCollection(MongoCollections.FIELDS);
        this.wellsCollection = database.getCollection(MongoCollections.WELLS);
        this.productionsCollection = database.getCollection(MongoCollections.PRODUCTIONS);
    }

    public void run() {
        cleanup();
        seedData();

        showAllWells();

        showFindByFilter();
        showUpdateByFilter();

        showAllProductions();
        showDeleteByFilter();
    }

    private void cleanup() {
        productionsCollection.deleteMany(new Document());
        wellsCollection.deleteMany(new Document());
        fieldsCollection.deleteMany(new Document());
    }

    private void seedData() {
        ObjectId samotlorskoeFieldId = insertField(new Field(
                null,
                "Самотлорское",
                "Ханты-Мансийский АО",
                61.154,
                76.684
        ));

        ObjectId mukhanovskoeFieldId = insertField(new Field(
                null,
                "Мухановское",
                "Самарская область",
                53.650,
                51.350
        ));

        ObjectId wellId1 = insertWell(new Well(
                null,
                "СМ-101",
                "active",
                3050.0,
                0.22,
                samotlorskoeFieldId
        ));

        ObjectId wellId2 = insertWell(new Well(
                null,
                "СМ-102",
                "active",
                3120.0,
                0.22,
                samotlorskoeFieldId
        ));

        ObjectId wellId3 = insertWell(new Well(
                null,
                "МХ-101",
                "active",
                2450.0,
                0.16,
                mukhanovskoeFieldId
        ));

        ObjectId wellId4 = insertWell(new Well(
                null,
                "МХ-102",
                "maintenance",
                2600.0,
                0.18,
                mukhanovskoeFieldId
        ));

        insertWell(new Well(
                null,
                "СБ-101",
                "disabled",
                3600.0,
                0.25,
                mukhanovskoeFieldId
        ));

        insertProduction(new Production(
                null,
                wellId1,
                LocalDate.of(2026, 3, 10),
                120.5,
                540.0,
                18.2
        ));

        insertProduction(new Production(
                null,
                wellId1,
                LocalDate.of(2026, 3, 11),
                42.0,
                510.0,
                125.0
        ));

        insertProduction(new Production(
                null,
                wellId2,
                LocalDate.of(2026, 3, 10),
                132.0,
                560.0,
                20.1
        ));

        insertProduction(new Production(
                null,
                wellId3,
                LocalDate.of(2026, 3, 10),
                35.0,
                220.0,
                90.0
        ));

        insertProduction(new Production(
                null,
                wellId4,
                LocalDate.of(2026, 3, 10),
                88.0,
                300.0,
                40.0
        ));
    }

    private ObjectId insertField(Field field) {
        InsertOneResult result = fieldsCollection.insertOne(
                FieldMapper.toDocument(field)
        );

        return result.getInsertedId().asObjectId().getValue();
    }

    private ObjectId insertWell(Well well) {
        InsertOneResult result = wellsCollection.insertOne(
                WellMapper.toDocument(well)
        );

        return result.getInsertedId().asObjectId().getValue();
    }

    private ObjectId insertProduction(Production production) {
        InsertOneResult result = productionsCollection.insertOne(
                ProductionMapper.toDocument(production)
        );

        return result.getInsertedId().asObjectId().getValue();
    }

    private void showAllWells() {
        System.out.println();
        System.out.println("=== ALL WELLS ===");

        FindIterable<Document> result = wellsCollection.find();

        printDocuments(result);
    }

    private void showFindByFilter() {
        System.out.println();
        System.out.println("=== FIND BY FILTER ===");
        System.out.println("Активные скважины глубже 3000 м:");

        FindIterable<Document> result = wellsCollection.find(
                and(
                        eq("status", "active"),
                        gt("depth", 3000.0)
                )
        );

        printDocuments(result);
    }

    private void showUpdateByFilter() {
        System.out.println();
        System.out.println("=== UPDATE BY FILTER ===");
        System.out.println("Обновляем статус активных скважин глубже 3000 м на needs_inspection");

        UpdateResult updateResult = wellsCollection.updateMany(
                and(
                        eq("status", "active"),
                        gt("depth", 3000.0)
                ),
                set("status", "needs_inspection")
        );

        System.out.println("Найдено документов: " + updateResult.getMatchedCount());
        System.out.println("Изменено документов: " + updateResult.getModifiedCount());

        System.out.println();
        System.out.println("Скважины после обновления:");

        printDocuments(wellsCollection.find());
    }

    private void showAllProductions() {
        System.out.println();
        System.out.println("=== ALL PRODUCTIONS ===");

        FindIterable<Document> result = productionsCollection.find();

        printDocuments(result);
    }

    private void showDeleteByFilter() {
        System.out.println();
        System.out.println("=== DELETE BY FILTER ===");
        System.out.println("Удаляем записи добычи, где oil < 50");

        DeleteResult deleteResult = productionsCollection.deleteMany(
                lt("oil", 50.0)
        );

        System.out.println("Удалено документов: " + deleteResult.getDeletedCount());

        System.out.println();
        System.out.println("Записи добычи после удаления:");

        printDocuments(productionsCollection.find());
    }

    private void printDocuments(FindIterable<Document> documents) {
        for (Document document : documents) {
            System.out.println(document.toJson());
        }
    }
}