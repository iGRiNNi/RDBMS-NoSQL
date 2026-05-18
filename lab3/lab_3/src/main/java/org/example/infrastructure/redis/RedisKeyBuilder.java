package org.example.infrastructure.redis;

public final class RedisKeyBuilder {

    private RedisKeyBuilder() {
    }

    public static String entityFieldKey(String entityType, long entityId, String fieldName) {
        return entityType + ":" + entityId + ":" + fieldName;
    }

    public static String idsKey(String entityType) {
        return entityType + ":ids";
    }

    public static String hashKey(String entityType, long entityId) {
        return "hash:" + entityType + ":" + entityId;
    }
}