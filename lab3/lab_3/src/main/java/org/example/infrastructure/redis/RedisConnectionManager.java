package org.example.infrastructure.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.RedisClient;

public final class RedisConnectionManager {

    private static final RedisConfig CONFIG = RedisConfig.local();

    private static final RedisClient CLIENT =
            RedisClient.create(new HostAndPort(CONFIG.getHost(), CONFIG.getPort()));

    private RedisConnectionManager() {
    }

    public static RedisClient getClient() {
        return CLIENT;
    }

    public static void closeClient() {
        CLIENT.close();
    }
}