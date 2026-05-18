package org.example.dao.keyvalue;

import org.example.dao.abstracts.CrudDao;
import org.example.infrastructure.redis.RedisConnectionManager;
import org.example.infrastructure.redis.RedisKeyBuilder;
import redis.clients.jedis.AbstractTransaction;
import redis.clients.jedis.RedisClient;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.*;


public abstract class AbstractKeyValueDao<T> implements CrudDao<T, Long> {

    private final String entityType;
    private final List<String> fieldNames;
    private final String requiredFieldName;

    protected AbstractKeyValueDao(String entityType, List<String> fieldNames, String requiredFieldName) {
        this.entityType = entityType;
        this.fieldNames = fieldNames;
        this.requiredFieldName = requiredFieldName;
    }

    protected abstract long getId(T entity);

    protected abstract Map<String, String> toRedisMap(T entity);

    protected abstract T fromRedisMap(long id, Map<String, String> values);

    @Override
    public void create(T entity) {
        long id = getId(entity);

        if (id <= 0) {
            throw new IllegalArgumentException("Entity id must be positive");
        }

        if (exists(id)) {
            throw new IllegalStateException(entityType + " with id " + id + " already exists");
        }

        save(entity);
    }

    @Override
    public T getById(Long id) {
        RedisClient redis = RedisConnectionManager.getClient();

        Map<String, String> values = new LinkedHashMap<>();

        for (String fieldName : fieldNames) {
            String value = redis.get(key(id, fieldName));

            if (value != null) {
                values.put(fieldName, value);
            }
        }

        if (!values.containsKey(requiredFieldName)) {
            return null;
        }

        return fromRedisMap(id, values);
    }

    @Override
    public void update(T entity) {
        long id = getId(entity);

        if (!exists(id)) {
            throw new IllegalStateException(entityType + " with id " + id + " not found");
        }

        save(entity);
    }

    @Override
    public void delete(Long id) {
        RedisClient redis = RedisConnectionManager.getClient();

        for (String fieldName : fieldNames) {
            redis.del(key(id, fieldName));
        }
    }

    private void save(T entity) {
        RedisClient redis = RedisConnectionManager.getClient();

        long id = getId(entity);
        Map<String, String> values = toRedisMap(entity);

        String requiredValue = values.get(requiredFieldName);

        if (requiredValue == null || requiredValue.isBlank()) {
            throw new IllegalArgumentException("Required field is missing: " + requiredFieldName);
        }

        AbstractTransaction transaction = redis.multi();

        try {
            for (String fieldName : fieldNames) {
                transaction.del(key(id, fieldName));
            }

            for (Map.Entry<String, String> entry : values.entrySet()) {
                if (entry.getValue() != null) {
                    transaction.set(key(id, entry.getKey()), entry.getValue());
                }
            }

            transaction.exec();

        } catch (RuntimeException e) {
            transaction.discard();
            throw e;
        }
    }

    private boolean exists(long id) {
        RedisClient redis = RedisConnectionManager.getClient();

        return redis.exists(key(id, requiredFieldName));
    }

    private String key(long id, String fieldName) {
        return RedisKeyBuilder.entityFieldKey(entityType, id, fieldName);
    }
}
