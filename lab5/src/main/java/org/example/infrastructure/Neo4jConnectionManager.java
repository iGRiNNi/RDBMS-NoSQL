package org.example.infrastructure;


import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public final class Neo4jConnectionManager {

    private static final Neo4jConfig CONFIG = Neo4jConfig.load();

    private static final Driver DRIVER = GraphDatabase.driver(
            CONFIG.getUri(),
            AuthTokens.basic(CONFIG.getUsername(), CONFIG.getPassword())
    );

    private Neo4jConnectionManager() {
    }

    public static Session getSession() {
        return DRIVER.session(
                SessionConfig.forDatabase(CONFIG.getDatabase())
        );
    }

    public static void verifyConnectivity() {
        DRIVER.verifyConnectivity();
    }

    public static void closeDriver() {
        DRIVER.close();
    }
}
