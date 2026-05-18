package org.example.infrastructure.redis;

public final class RedisConfig {

    private final String host;
    private final int port;

    private RedisConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static RedisConfig local() {
        return new RedisConfig("localhost", 6379);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}