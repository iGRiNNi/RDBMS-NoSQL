package org.example;

import org.example.dao.keyvalue.ClusterKeyValueDao;
import org.example.dao.keyvalue.FieldKeyValueDao;
import org.example.dao.keyvalue.WellKeyValueDao;
import org.example.domain.model.Cluster;
import org.example.domain.model.Field;
import org.example.domain.model.Well;
import org.example.infrastructure.redis.RedisConnectionManager;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        try {
            checkConnection();
            cleanup();

            runKeyValueDaoDemo();

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
                "ХМАО — Югра",
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
}