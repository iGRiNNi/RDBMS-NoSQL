package com.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.example.domain.model.Field;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import com.example.mapper.FieldMapper;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class FieldDao implements CrudDao<Field, Long> {

    private final MongoCollection<Document> collection;

    public FieldDao() {
        MongoDatabase database = MongoConnectionManager.getDatabase();
        this.collection = database.getCollection(MongoCollections.FIELDS);
    }

    @Override
    public void create(Field field) {
        collection.insertOne(FieldMapper.toDocument(field));
    }

    @Override
    public Field getById(Long id) {
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
        collection.replaceOne(eq("_id", field.getId()), FieldMapper.toDocument(field));
    }

    @Override
    public void delete(Long id) {
        collection.deleteOne(eq("_id", id));
    }
}
