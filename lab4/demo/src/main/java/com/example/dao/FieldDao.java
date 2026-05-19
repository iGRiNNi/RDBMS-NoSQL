package com.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import com.example.domain.model.Field;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import com.example.mapper.FieldMapper;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class FieldDao implements CrudDao<Field, ObjectId> {

    private final MongoCollection<Document> collection;

    public FieldDao() {
        MongoDatabase database = MongoConnectionManager.getDatabase();
        this.collection = database.getCollection(MongoCollections.FIELDS);
    }

    @Override
    public ObjectId create(Field field) {
        InsertOneResult result = collection.insertOne(FieldMapper.toDocument(field));

        return result.getInsertedId().asObjectId().getValue();
    }

    @Override
    public Field getById(ObjectId id) {
        Document document = collection.find(eq("_id", id)).first();
        return FieldMapper.fromDocument(document);
    }

    @Override
    public List<Field> getAll() {
        List<Field> result = new ArrayList<>();

        for (Document document : collection.find()) {
            result.add(FieldMapper.fromDocument(document));
        }

        return result;
    }

    @Override
    public void update(Field field) {
        if (field.getId() == null) {
            throw new IllegalArgumentException("Field id must not be null for update");
        }

        collection.replaceOne(eq("_id", field.getId()), FieldMapper.toDocument(field));
    }

    @Override
    public void delete(ObjectId id) {
        collection.deleteOne(eq("_id", id));
    }
}
