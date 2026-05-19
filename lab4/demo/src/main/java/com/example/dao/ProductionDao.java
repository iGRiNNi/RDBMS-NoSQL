package com.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.example.domain.model.Production;
import com.example.infrastructure.mongodb.MongoCollections;
import com.example.infrastructure.mongodb.MongoConnectionManager;
import com.example.mapper.ProductionMapper;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ProductionDao implements CrudDao<Production, Long> {

    private final MongoCollection<Document> collection;

    public ProductionDao() {
        MongoDatabase database = MongoConnectionManager.getDatabase();
        this.collection = database.getCollection(MongoCollections.PRODUCTIONS);
    }

    @Override
    public void create(Production production) {
        collection.insertOne(ProductionMapper.toDocument(production));
    }

    @Override
    public Production getById(Long id) {
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
        collection.replaceOne(eq("_id", production.getId()), ProductionMapper.toDocument(production));
    }

    @Override
    public void delete(Long id) {
        collection.deleteOne(eq("_id", id));
    }
}
