package org.example.infrastracture.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public final class RedisConnectionManager {

    private static final RedisConfig CONFIG = RedisConfig.local();

    private static final JedisPool POOL =
            new JedisPool(CONFIG.getHost(), CONFIG.getPort());

    private RedisConnectionManager() {
    }

    public static Jedis getConnection() {
        return POOL.getResource();
    }

    public static void closePool() {
        POOL.close();
    }
}
