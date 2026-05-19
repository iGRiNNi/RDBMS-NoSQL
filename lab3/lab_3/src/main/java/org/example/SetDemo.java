package org.example;

import org.example.infrastructure.redis.RedisConnectionManager;
import redis.clients.jedis.RedisClient;

public class SetDemo {

    private static final String ACTIVE_WELLS = "demo:set:wells:active";
    private static final String SAMARA_WELLS = "demo:set:wells:samara";
    private static final String MAINTENANCE_WELLS = "demo:set:wells:maintenance";

    private static final String DIFF_RESULT = "demo:set:result:active_without_maintenance";
    private static final String UNION_RESULT = "demo:set:result:all_selected_wells";

    public void run() {
        RedisClient redis = RedisConnectionManager.getClient();

        cleanup(redis);
        fillInitialSets(redis);

        demonstrateSadd(redis);
        demonstrateScard(redis);
        demonstrateSmembers(redis);
        demonstrateSismember(redis);
        demonstrateSdiff(redis);
        demonstrateSdiffstore(redis);
        demonstrateSinter(redis);
        demonstrateSmove(redis);
        demonstrateSrandmember(redis);
        demonstrateSpop(redis);
        demonstrateSrem(redis);
        demonstrateSunion(redis);
        demonstrateSunionstore(redis);
    }

    private void cleanup(RedisClient redis) {
        redis.del(
                ACTIVE_WELLS,
                SAMARA_WELLS,
                MAINTENANCE_WELLS,
                DIFF_RESULT,
                UNION_RESULT
        );
    }

    private void fillInitialSets(RedisClient redis) {
        redis.sadd(ACTIVE_WELLS, "well:1", "well:2", "well:3", "well:5");
        redis.sadd(SAMARA_WELLS, "well:5", "well:6", "well:7");
        redis.sadd(MAINTENANCE_WELLS, "well:3", "well:7", "well:8");

        System.out.println("=== Исходные множества ===");
        printSet(redis, ACTIVE_WELLS);
        printSet(redis, SAMARA_WELLS);
        printSet(redis, MAINTENANCE_WELLS);
    }

    private void demonstrateSadd(RedisClient redis) {
        System.out.println("=== SADD ===");

        long addedCount = redis.sadd(ACTIVE_WELLS, "well:9", "well:10");

        System.out.println("Добавлено новых элементов: " + addedCount);
        printSet(redis, ACTIVE_WELLS);
    }

    private void demonstrateScard(RedisClient redis) {
        System.out.println("=== SCARD ===");

        long count = redis.scard(ACTIVE_WELLS);

        System.out.println("Количество активных скважин: " + count);
        System.out.println();
    }

    private void demonstrateSmembers(RedisClient redis) {
        System.out.println("=== SMEMBERS ===");

        System.out.println("Все активные скважины:");
        System.out.println(redis.smembers(ACTIVE_WELLS));
        System.out.println();
    }

    private void demonstrateSismember(RedisClient redis) {
        System.out.println("=== SISMEMBER ===");

        System.out.println("well:1 входит в active? " + redis.sismember(ACTIVE_WELLS, "well:1"));
        System.out.println("well:100 входит в active? " + redis.sismember(ACTIVE_WELLS, "well:100"));
        System.out.println();
    }

    private void demonstrateSdiff(RedisClient redis) {
        System.out.println("=== SDIFF ===");

        System.out.println("Активные скважины, которые не находятся на ремонте:");
        System.out.println(redis.sdiff(ACTIVE_WELLS, MAINTENANCE_WELLS));
        System.out.println();
    }

    private void demonstrateSdiffstore(RedisClient redis) {
        System.out.println("=== SDIFFSTORE ===");

        long savedCount = redis.sdiffstore(DIFF_RESULT, ACTIVE_WELLS, MAINTENANCE_WELLS);

        System.out.println("Сохранено элементов в " + DIFF_RESULT + ": " + savedCount);
        printSet(redis, DIFF_RESULT);
    }

    private void demonstrateSinter(RedisClient redis) {
        System.out.println("=== SINTER ===");

        System.out.println("Скважины, которые одновременно активные и из Самарской области:");
        System.out.println(redis.sinter(ACTIVE_WELLS, SAMARA_WELLS));
        System.out.println();
    }

    private void demonstrateSmove(RedisClient redis) {
        System.out.println("=== SMOVE ===");

        long moved = redis.smove(SAMARA_WELLS, MAINTENANCE_WELLS, "well:6");

        System.out.println("Перемещён well:6 из samara в maintenance? " + moved);

        System.out.println("После SMOVE:");
        printSet(redis, SAMARA_WELLS);
        printSet(redis, MAINTENANCE_WELLS);
    }

    private void demonstrateSrandmember(RedisClient redis) {
        System.out.println("=== SRANDMEMBER ===");

        String randomOne = redis.srandmember(ACTIVE_WELLS);

        System.out.println("Случайная активная скважина без удаления: " + randomOne);
        System.out.println("Множество после SRANDMEMBER не изменилось:");
        printSet(redis, ACTIVE_WELLS);
    }

    private void demonstrateSpop(RedisClient redis) {
        System.out.println("=== SPOP ===");

        String removedRandom = redis.spop(ACTIVE_WELLS);

        System.out.println("Случайно удалённая активная скважина: " + removedRandom);
        System.out.println("Множество после SPOP:");
        printSet(redis, ACTIVE_WELLS);
    }

    private void demonstrateSrem(RedisClient redis) {
        System.out.println("=== SREM ===");

        long removedCount = redis.srem(ACTIVE_WELLS, "well:2");

        System.out.println("Удалено элементов: " + removedCount);
        printSet(redis, ACTIVE_WELLS);
    }

    private void demonstrateSunion(RedisClient redis) {
        System.out.println("=== SUNION ===");

        System.out.println("Объединение active, samara и maintenance:");
        System.out.println(redis.sunion(ACTIVE_WELLS, SAMARA_WELLS, MAINTENANCE_WELLS));
        System.out.println();
    }

    private void demonstrateSunionstore(RedisClient redis) {
        System.out.println("=== SUNIONSTORE ===");

        long savedCount = redis.sunionstore(
                UNION_RESULT,
                ACTIVE_WELLS,
                SAMARA_WELLS,
                MAINTENANCE_WELLS
        );

        System.out.println("Сохранено элементов в " + UNION_RESULT + ": " + savedCount);
        printSet(redis, UNION_RESULT);
    }

    private void printSet(RedisClient redis, String key) {
        System.out.println(key + " = " + redis.smembers(key));
        System.out.println();
    }
}
