package com.example;


import com.example.infrastructure.cassandra.CassandraMigrationRunner;
import com.example.infrastructure.cassandra.CassandraSessionManager;

public class Main {
    public static void main(String[] args) {
        try {
            CassandraMigrationRunner migrationRunner = new CassandraMigrationRunner();
            migrationRunner.migrate();

            ApplicationRunner runner = new ApplicationRunner();
            runner.run();
            
        } finally {
            CassandraSessionManager.close();
        }
    }
}