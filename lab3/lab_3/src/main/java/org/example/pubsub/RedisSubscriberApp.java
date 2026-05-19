package org.example.pubsub;

import org.example.infrastructure.redis.RedisConnectionManager;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.RedisClient;

public class RedisSubscriberApp {

    public static void main(String[] args) {
        RedisClient redis = RedisConnectionManager.getClient();

        JedisPubSub subscriber = new JedisPubSub() {

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                System.out.println("Подписка выполнена.");
                System.out.println("Канал: " + channel);
                System.out.println("Ожидание сообщений...");
                System.out.println("Для завершения отправьте сообщение: /exit");
                System.out.println();
            }

            @Override
            public void onMessage(String channel, String message) {
                System.out.println("Получено сообщение");
                System.out.println("Канал: " + channel);
                System.out.println("Сообщение: " + message);
                System.out.println();

                if ("/exit".equalsIgnoreCase(message)) {
                    System.out.println("Получена команда завершения. Отписка от канала...");
                    unsubscribe();
                }
            }

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                System.out.println("Подписчик отписался от канала: " + channel);
            }
        };

        try {
            redis.subscribe(subscriber, RedisChannels.PRODUCTION_EVENTS);
        } finally {
            RedisConnectionManager.closeClient();
        }
    }
}