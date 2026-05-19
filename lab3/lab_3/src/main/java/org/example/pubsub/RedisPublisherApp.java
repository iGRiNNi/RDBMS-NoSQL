package org.example.pubsub;

import org.example.infrastructure.redis.RedisConnectionManager;
import redis.clients.jedis.RedisClient;

import java.util.Scanner;

public class RedisPublisherApp {

    public static void main(String[] args) {
        RedisClient redis = RedisConnectionManager.getClient();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Издатель запущен.");
            System.out.println("Канал: " + RedisChannels.PRODUCTION_EVENTS);
            System.out.println("Введите сообщения для отправки.");
            System.out.println("Для завершения введите: /exit");
            System.out.println();

            while (true) {
                System.out.print("message> ");
                String message = scanner.nextLine();

                long deliveredCount = redis.publish(RedisChannels.PRODUCTION_EVENTS, message);

                System.out.println("Сообщение отправлено. Количество получателей: " + deliveredCount);
                System.out.println();

                if ("/exit".equalsIgnoreCase(message)) {
                    break;
                }
            }
        } finally {
            RedisConnectionManager.closeClient();
        }
    }
}
