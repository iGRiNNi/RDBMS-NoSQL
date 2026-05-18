package org.example.infrastructure.redis;

public final class RedisValueUtils {

    private RedisValueUtils() {
    }

    public static String toRedisString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    public static Double parseNullableDouble(String value) {
        return value == null ? null : Double.parseDouble(value);
    }

    public static Long parseNullableLong(String value) {
        return value == null ? null : Long.parseLong(value);
    }

    public static long parseRequiredLong(String value, String fieldName) {
        if (value == null) {
            throw new IllegalStateException("Required Redis field is missing: " + fieldName);
        }

        return Long.parseLong(value);
    }
}
