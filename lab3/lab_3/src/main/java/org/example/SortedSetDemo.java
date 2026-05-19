package org.example;

import org.example.infrastructure.redis.RedisConnectionManager;
import redis.clients.jedis.RedisClient;

public class SortedSetDemo {

    private static final String OIL_RATING_KEY = "demo:zset:wells:oil_rating";

    public void run() {
        RedisClient redis = RedisConnectionManager.getClient();

        redis.del(OIL_RATING_KEY);

        demonstrateZadd(redis);
        demonstrateZrange(redis);
        demonstrateZrevrange(redis);
        demonstrateZrangeByScore(redis);
        demonstrateZremrangeByScore(redis);
    }

    private void demonstrateZadd(RedisClient redis) {
        System.out.println("=== ZADD ===");

        redis.zadd(OIL_RATING_KEY, 120.5, "well:1");
        redis.zadd(OIL_RATING_KEY, 132.0, "well:2");
        redis.zadd(OIL_RATING_KEY, 88.4, "well:3");
        redis.zadd(OIL_RATING_KEY, 184.5, "well:4");
        redis.zadd(OIL_RATING_KEY, 95.0, "well:5");

        System.out.println("Добавлен рейтинг скважин по добыче нефти");
        printSortedSet(redis);
    }

    private void demonstrateZrange(RedisClient redis) {
        System.out.println("=== ZRANGE ===");

        System.out.println("Скважины по возрастанию добычи:");
        System.out.println(redis.zrange(OIL_RATING_KEY, 0, -1));
        System.out.println();
    }

    private void demonstrateZrevrange(RedisClient redis) {
        System.out.println("=== ZREVRANGE ===");

        System.out.println("Скважины по убыванию добычи:");
        System.out.println(redis.zrevrange(OIL_RATING_KEY, 0, -1));
        System.out.println();

        System.out.println("Топ-3 скважины по добыче:");
        System.out.println(redis.zrevrange(OIL_RATING_KEY, 0, 2));
        System.out.println();
    }

    private void demonstrateZrangeByScore(RedisClient redis) {
        System.out.println("=== ZRANGEBYSCORE ===");

        System.out.println("Скважины с добычей от 90 до 140:");
        System.out.println(redis.zrangeByScore(OIL_RATING_KEY, 90.0, 140.0));
        System.out.println();
    }

    private void demonstrateZremrangeByScore(RedisClient redis) {
        System.out.println("=== ZREMRANGEBYSCORE ===");

        long removedCount = redis.zremrangeByScore(OIL_RATING_KEY, 0.0, 100.0);

        System.out.println("Удалено скважин с добычей от 0 до 100: " + removedCount);
        printSortedSet(redis);
    }

    private void printSortedSet(RedisClient redis) {
        System.out.println("Текущее упорядоченное множество:");

        for (String well : redis.zrange(OIL_RATING_KEY, 0, -1)) {
            Double score = redis.zscore(OIL_RATING_KEY, well);
            System.out.println(well + " -> " + score);
        }

        System.out.println();
    }
}