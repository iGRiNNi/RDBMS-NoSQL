package com.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import com.example.domain.model.Well;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import com.example.mapper.WellMapper;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class WellDao implements CrudDao<Well, ObjectId> {

    private final MongoCollection<Document> collection;

    public WellDao() {
        MongoDatabase database = MongoConnectionManager.getDatabase();
        this.collection = database.getCollection(MongoCollections.WELLS);
    }

    @Override
    public ObjectId create(Well well) {
        Document document = WellMapper.toDocument(well);

        InsertOneResult result = collection.insertOne(document);

        return result.getInsertedId().asObjectId().getValue();
    }

    @Override
    public Well getById(ObjectId id) {
        Document document = collection.find(eq("_id", id)).first();

        return WellMapper.fromDocument(document);
    }

    @Override
    public List<Well> getAll() {
        List<Well> result = new ArrayList<>();

        for (Document document : collection.find()) {
            result.add(WellMapper.fromDocument(document));
        }

        return result;
    }

    @Override
    public void update(Well well) {
        if (well.getId() == null) {
            throw new IllegalArgumentException("Well id must not be null for update");
        }

        collection.replaceOne(
                eq("_id", well.getId()),
                WellMapper.toDocument(well)
        );
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }
}