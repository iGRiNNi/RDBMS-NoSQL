package org.example;

import org.example.infrastructure.redis.RedisConnectionManager;
import redis.clients.jedis.RedisClient;

public class ListDemo {

    private static final String EVENTS_KEY = "demo:well:1:events";
    private static final String QUEUE_KEY = "demo:production:queue";

    public void run() {
        RedisClient redis = RedisConnectionManager.getClient();

        redis.del(EVENTS_KEY);
        redis.del(QUEUE_KEY);

        showListOperations(redis);
        showQueueLikeOperations(redis);
    }

    private void showListOperations(RedisClient redis) {
        System.out.println();
        System.out.println("=== LIST: события по скважине ===");

        redis.rpush(EVENTS_KEY, "Замер дебита");
        redis.rpush(EVENTS_KEY, "Проверка давления");
        redis.rpush(EVENTS_KEY, "Плановый осмотр");

        System.out.println("После RPUSH:");
        printList(redis, EVENTS_KEY);

        redis.lpush(EVENTS_KEY, "Аварийное отключение");

        System.out.println("После LPUSH:");
        printList(redis, EVENTS_KEY);

        String firstEvent = redis.lpop(EVENTS_KEY);

        System.out.println("Удалён первый элемент через LPOP: " + firstEvent);
        printList(redis, EVENTS_KEY);

        String lastEvent = redis.rpop(EVENTS_KEY);

        System.out.println("Удалён последний элемент через RPOP: " + lastEvent);
        printList(redis, EVENTS_KEY);

        redis.rpush(EVENTS_KEY, "Повторный замер");
        redis.rpush(EVENTS_KEY, "Повторный замер");
        redis.rpush(EVENTS_KEY, "Контроль температуры");

        System.out.println("После добавления повторяющихся элементов:");
        printList(redis, EVENTS_KEY);

        long removedCount = redis.lrem(EVENTS_KEY, 1, "Повторный замер");

        System.out.println("Удалено элементов через LREM: " + removedCount);
        printList(redis, EVENTS_KEY);

        redis.ltrim(EVENTS_KEY, 0, 1);

        System.out.println("После LTRIM, оставлены только первые 2 элемента:");
        printList(redis, EVENTS_KEY);
    }

    private void showQueueLikeOperations(RedisClient redis) {
        System.out.println();
        System.out.println("=== LIST: очередь событий добычи ===");

        redis.rpush(QUEUE_KEY, "production:1");
        redis.rpush(QUEUE_KEY, "production:2");
        redis.rpush(QUEUE_KEY, "production:3");

        System.out.println("Очередь после добавления элементов:");
        printList(redis, QUEUE_KEY);

        String handledEvent = redis.lpop(QUEUE_KEY);

        System.out.println("Обработан элемент очереди: " + handledEvent);
        System.out.println("Очередь после обработки:");
        printList(redis, QUEUE_KEY);
    }

    private void printList(RedisClient redis, String key) {
        System.out.println(key + " = " + redis.lrange(key, 0, -1));
        System.out.println("Длина списка: " + redis.llen(key));
        System.out.println();
    }
}
