package com.example;

import com.example.demo.CrudDemo;
import com.example.demo.FilterQueryDemo;
import com.example.demo.SparseColumnsDemo;
import com.example.infrastructure.cassandra.CassandraMigrationRunner;
import com.example.infrastructure.cassandra.CassandraSessionManager;

public class Main {
    public static void main(String[] args) {
        try {
            CassandraMigrationRunner migrationRunner = new CassandraMigrationRunner();
            migrationRunner.migrate();

            CrudDemo.run();

            FilterQueryDemo.run();

            SparseColumnsDemo.run();
        } finally {
            CassandraSessionManager.close();
        }
    }
}