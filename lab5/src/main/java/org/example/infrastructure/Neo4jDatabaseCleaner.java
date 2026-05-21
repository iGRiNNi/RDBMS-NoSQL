package org.example.infrastructure;

import org.neo4j.driver.Session;

public class Neo4jDatabaseCleaner {

    public void clean() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            deleteRelationships(session);
            deleteNodes(session);
        }
    }

    private void deleteRelationships(Session session) {
        session.run("""
                MATCH ()-[r]->()
                DELETE r
                """).consume();
    }

    private void deleteNodes(Session session) {
        session.run("""
                MATCH (n)
                DELETE n
                """).consume();
    }
}