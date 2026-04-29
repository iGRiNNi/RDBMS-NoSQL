package org.example.infrastracture.redis;

public final class RedisKeyBuilder {

    private RedisKeyBuilder() {
    }

    public static String fieldKey(String entityType, long id, String fieldName) {
        return entityType + ":" + id + ":" + fieldName;
    }

    public static String idsKey(String entityType) {
        return entityType + ":ids";
    }

    public static String hashKey(String entityType, long id) {
        return "hash:" + entityType + ":" + id;
    }
}
