package org.example.dao.hash;

import org.example.dao.abstracts.CrudDao;
import org.example.infrastructure.redis.RedisConnectionManager;
import org.example.infrastructure.redis.RedisKeyBuilder;

import redis.clients.jedis.AbstractTransaction;
import redis.clients.jedis.RedisClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractHashDao<T> implements CrudDao<T, Long> {

    private final String entityType;
    private final List<String> fieldNames;
    private final String requiredFieldName;

    protected AbstractHashDao(String entityType, List<String> fieldNames, String requiredFieldName) {
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

        Map<String, String> rawHash = redis.hgetAll(hashKey(id));

        if (!rawHash.containsKey(hashField(id, requiredFieldName))) {
            return null;
        }

        Map<String, String> values = new LinkedHashMap<>();

        for (String fieldName : fieldNames) {
            String value = rawHash.get(hashField(id, fieldName));

            if (value != null) {
                values.put(fieldName, value);
            }
        }

        return fromRedisMap(id, values);
    }

    @Override
    public void update(T entity) {
        long id = getId(entity);

        if (id <= 0) {
            throw new IllegalArgumentException("Entity id must be positive");
        }

        if (!exists(id)) {
            throw new IllegalStateException(entityType + " with id " + id + " not found");
        }

        save(entity);
    }

    @Override
    public void delete(Long id) {
        RedisClient redis = RedisConnectionManager.getClient();
        redis.del(hashKey(id));
    }

    private void save(T entity) {
        RedisClient redis = RedisConnectionManager.getClient();

        long id = getId(entity);
        Map<String, String> values = toRedisMap(entity);

        String requiredValue = values.get(requiredFieldName);

        if (requiredValue == null || requiredValue.isBlank()) {
            throw new IllegalArgumentException("Required field is missing: " + requiredFieldName);
        }

        Map<String, String> hashValues = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (entry.getValue() != null) {
                hashValues.put(hashField(id, entry.getKey()), entry.getValue());
            }
        }

        AbstractTransaction transaction = redis.multi();

        try {
            transaction.del(hashKey(id));
            transaction.hset(hashKey(id), hashValues);
            transaction.exec();
        } catch (RuntimeException e) {
            discardQuietly(transaction);
            throw e;
        }
    }

    private boolean exists(long id) {
        RedisClient redis = RedisConnectionManager.getClient();

        return redis.hexists(hashKey(id), hashField(id, requiredFieldName));
    }

    private String hashKey(long id) {
        return RedisKeyBuilder.hashKey(entityType, id);
    }

    private String hashField(long id, String fieldName) {
        return RedisKeyBuilder.entityFieldKey(entityType, id, fieldName);
    }

    private void discardQuietly(AbstractTransaction transaction) {
        try {
            transaction.discard();
        } catch (Exception ignored) {
        }
    }
}