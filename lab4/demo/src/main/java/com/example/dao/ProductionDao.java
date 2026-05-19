package com.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import com.example.domain.model.Production;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import com.example.mapper.ProductionMapper;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ProductionDao implements CrudDao<Production, ObjectId> {

    private final MongoCollection<Document> collection;

    public ProductionDao() {
        MongoDatabase database = MongoConnectionManager.getDatabase();
        this.collection = database.getCollection(MongoCollections.PRODUCTIONS);
    }

    @Override
    public ObjectId create(Production production) {
        Document document = ProductionMapper.toDocument(production);

        InsertOneResult result = collection.insertOne(document);

        return result.getInsertedId().asObjectId().getValue();
    }

    @Override
    public Production getById(ObjectId id) {
        Document document = collection.find(eq("_id", id)).first();

        return ProductionMapper.fromDocument(document);
    }

    @Override
    public List<Production> getAll() {
        List<Production> result = new ArrayList<>();

        for (Document document : collection.find()) {
            result.add(ProductionMapper.fromDocument(document));
        }

        return result;
    }

    @Override
    public void update(Production production) {
        if (production.getId() == null) {
            throw new IllegalArgumentException("Production id must not be null for update");
        }

        collection.replaceOne(
                eq("_id", production.getId()),
                ProductionMapper.toDocument(production)
        );
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }
}