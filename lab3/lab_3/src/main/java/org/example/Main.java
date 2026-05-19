package org.example;

import org.example.dao.hash.ClusterHashDao;
import org.example.dao.hash.FieldHashDao;
import org.example.dao.hash.WellHashDao;
import org.example.dao.keyvalue.ClusterKeyValueDao;
import org.example.dao.keyvalue.FieldKeyValueDao;
import org.example.dao.keyvalue.WellKeyValueDao;
import org.example.domain.model.Cluster;
import org.example.domain.model.Field;
import org.example.domain.model.Well;
import org.example.infrastructure.redis.RedisConnectionManager;

public class Main {

    public static void main(String[] args) {
        try {
            checkConnection();
            cleanup();

            runKeyValueDaoDemo();

            System.out.println();
            System.out.println("=== ПУНКТ 2. СПИСКИ REDIS ===");
            new ListDemo().run();

            System.out.println();
            System.out.println("=== ПУНКТ 3. МНОЖЕСТВА REDIS ===");
            new SetDemo().run();

            System.out.println();
            System.out.println("=== ПУНКТ 4. УПОРЯДОЧЕННЫЕ МНОЖЕСТВА REDIS ===");
            new SortedSetDemo().run();

            System.out.println();
            System.out.println("=== ПУНКТ 5. CRUD ЧЕРЕЗ HASH-ТАБЛИЦЫ REDIS ===");
            runHashDaoDemo();
            
        } finally {
            RedisConnectionManager.closeClient();
        }
    }

    private static void checkConnection() {
        String response = RedisConnectionManager.getClient().ping();
        System.out.println("Redis PING: " + response);
    }

    private static void cleanup() {
        RedisConnectionManager.getClient().flushDB();
    }

    private static void runKeyValueDaoDemo() {
        FieldKeyValueDao fieldDao = new FieldKeyValueDao();
        ClusterKeyValueDao clusterDao = new ClusterKeyValueDao();
        WellKeyValueDao wellDao = new WellKeyValueDao();

        Field field = new Field(
                1L,
                "Самотлорское",
                "Ханты-Мансийский АО",
                61.154,
                76.684
        );

        Cluster cluster = new Cluster(
                1L,
                "Северная группа",
                1L,
                null
        );

        Well well = new Well(
                1L,
                "СМ-101",
                "active",
                3050.0,
                0.22,
                1L
        );

        System.out.println();
        System.out.println("=== CREATE ===");

        fieldDao.create(field);
        clusterDao.create(cluster);
        wellDao.create(well);

        System.out.println("Сущности созданы");

        System.out.println();
        System.out.println("=== READ BY ID ===");

        System.out.println(fieldDao.getById(1L));
        System.out.println(clusterDao.getById(1L));
        System.out.println(wellDao.getById(1L));

        System.out.println();
        System.out.println("=== UPDATE ===");

        Field updatedField = new Field(
                1L,
                "Самотлорское",
                "ХМАО - Югра",
                61.154,
                76.684
        );

        Cluster updatedCluster = new Cluster(
                1L,
                "Северная эксплуатационная группа",
                1L,
                null
        );

        Well updatedWell = new Well(
                1L,
                "СМ-101",
                "maintenance",
                3050.0,
                0.22,
                1L
        );

        fieldDao.update(updatedField);
        clusterDao.update(updatedCluster);
        wellDao.update(updatedWell);

        System.out.println(fieldDao.getById(1L));
        System.out.println(clusterDao.getById(1L));
        System.out.println(wellDao.getById(1L));

        System.out.println();
        System.out.println("=== DELETE ===");

        wellDao.delete(1L);
        clusterDao.delete(1L);
        fieldDao.delete(1L);

        System.out.println("Field после удаления: " + fieldDao.getById(1L));
        System.out.println("Cluster после удаления: " + clusterDao.getById(1L));
        System.out.println("Well после удаления: " + wellDao.getById(1L));
    }

    private static void runHashDaoDemo() {
        FieldHashDao fieldDao = new FieldHashDao();
        ClusterHashDao clusterDao = new ClusterHashDao();
        WellHashDao wellDao = new WellHashDao();

        Field field = new Field(
                10L,
                "Приобское",
                "Ханты-Мансийский АО",
                61.000,
                73.500
        );

        Cluster cluster = new Cluster(
                10L,
                "Центральная группа",
                10L,
                null
        );

        Well well = new Well(
                10L,
                "ПР-101",
                "active",
                3400.0,
                0.25,
                10L
        );

        System.out.println();
        System.out.println("=== HASH CREATE ===");

        fieldDao.create(field);
        clusterDao.create(cluster);
        wellDao.create(well);

        System.out.println("Сущности сохранены в Redis Hash");

        System.out.println();
        System.out.println("=== HASH READ BY ID ===");

        System.out.println(fieldDao.getById(10L));
        System.out.println(clusterDao.getById(10L));
        System.out.println(wellDao.getById(10L));

        System.out.println();
        System.out.println("=== HASH UPDATE ===");

        Field updatedField = new Field(
                10L,
                "Приобское",
                "ХМАО — Югра",
                61.000,
                73.500
        );

        Cluster updatedCluster = new Cluster(
                10L,
                "Центральная эксплуатационная группа",
                10L,
                null
        );

        Well updatedWell = new Well(
                10L,
                "ПР-101",
                "maintenance",
                3400.0,
                0.25,
                10L
        );

        fieldDao.update(updatedField);
        clusterDao.update(updatedCluster);
        wellDao.update(updatedWell);

        System.out.println(fieldDao.getById(10L));
        System.out.println(clusterDao.getById(10L));
        System.out.println(wellDao.getById(10L));

        System.out.println();
        System.out.println("=== HASH DELETE ===");

        wellDao.delete(10L);
        clusterDao.delete(10L);
        fieldDao.delete(10L);

        System.out.println("Field после удаления: " + fieldDao.getById(10L));
        System.out.println("Cluster после удаления: " + clusterDao.getById(10L));
        System.out.println("Well после удаления: " + wellDao.getById(10L));
    }
}