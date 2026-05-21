package org.example.infrastructure;

import org.neo4j.driver.Session;

public class Neo4jConstraintsInitializer {

    public void initialize() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            session.run("""
                    CREATE CONSTRAINT field_id_unique IF NOT EXISTS
                    FOR (f:Field)
                    REQUIRE f.fieldId IS UNIQUE
                    """).consume();

            session.run("""
                    CREATE CONSTRAINT well_id_unique IF NOT EXISTS
                    FOR (w:Well)
                    REQUIRE w.wellId IS UNIQUE
                    """).consume();

            session.run("""
                    CREATE CONSTRAINT production_id_unique IF NOT EXISTS
                    FOR (p:Production)
                    REQUIRE p.productionId IS UNIQUE
                    """).consume();
        }
    }
}
