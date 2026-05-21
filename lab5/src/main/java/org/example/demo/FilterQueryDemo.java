package org.example.demo;

import org.example.infrastructure.Neo4jConnectionManager;
import org.neo4j.driver.Session;

import java.util.HashMap;
import java.util.Map;

public class FilterQueryDemo {

    public void run() {
        cleanup();
        seedData();

        showFindNodesByFilter();
        showUpdateNodesByFilter();
        showDeleteNodesByFilter();

        showFindRelationshipsByFilter();
        showUpdateRelationshipsByFilter();
        showDeleteRelationshipsByFilter();
    }

    private void cleanup() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH ()-[r]->()
                        DELETE r
                        """).consume();

                tx.run("""
                        MATCH (n)
                        DELETE n
                        """).consume();

                return null;
            });
        }
    }

    private void seedData() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        CREATE (f1:Field {
                            fieldId: 1,
                            name: 'Самотлорское',
                            region: 'Ханты-Мансийский АО',
                            latitude: 61.154,
                            longitude: 76.684,
                            oilReserves: 2700000000.0
                        })
                        
                        CREATE (f2:Field {
                            fieldId: 2,
                            name: 'Мухановское',
                            region: 'Самарская область',
                            latitude: 53.650,
                            longitude: 51.350,
                            oilReserves: 800000000.0
                        })
                        
                        CREATE (w1:Well {
                            wellId: 1,
                            number: 'СМ-101',
                            status: 'active',
                            depth: 3050.0,
                            diameter: 0.22
                        })
                        
                        CREATE (w2:Well {
                            wellId: 2,
                            number: 'СМ-102',
                            status: 'active',
                            depth: 3120.0,
                            diameter: 0.22
                        })
                        
                        CREATE (w3:Well {
                            wellId: 3,
                            number: 'МХ-101',
                            status: 'maintenance',
                            depth: 2450.0,
                            diameter: 0.16
                        })
                        
                        CREATE (p1:Production {
                            productionId: 1,
                            productionDate: date('2026-03-10'),
                            oil: 120.5,
                            gas: 540.0,
                            water: 18.2
                        })
                        
                        CREATE (p2:Production {
                            productionId: 2,
                            productionDate: date('2026-03-11'),
                            oil: 42.0,
                            gas: 510.0,
                            water: 125.0
                        })
                        
                        CREATE (p3:Production {
                            productionId: 3,
                            productionDate: date('2026-03-10'),
                            oil: 88.0,
                            gas: 300.0,
                            water: 40.0
                        })
                        
                        CREATE (f1)-[:HAS_WELL {
                            assignedAt: date('2020-01-15'),
                            relationStatus: 'active',
                            comment: 'Основная эксплуатационная скважина'
                        }]->(w1)
                        
                        CREATE (f1)-[:HAS_WELL {
                            assignedAt: date('2021-02-10'),
                            relationStatus: 'active',
                            comment: 'Резервная эксплуатационная скважина'
                        }]->(w2)
                        
                        CREATE (f2)-[:HAS_WELL {
                            assignedAt: date('2019-05-20'),
                            relationStatus: 'archived',
                            comment: 'Архивная связь'
                        }]->(w3)
                        
                        CREATE (w1)-[:HAS_PRODUCTION {
                            measuredAt: datetime('2026-03-10T08:30:00'),
                            method: 'automatic',
                            verified: true
                        }]->(p1)
                        
                        CREATE (w2)-[:HAS_PRODUCTION {
                            measuredAt: datetime('2026-03-11T09:00:00'),
                            method: 'manual',
                            verified: false
                        }]->(p2)
                        
                        CREATE (w3)-[:HAS_PRODUCTION {
                            measuredAt: datetime('2026-03-10T10:15:00'),
                            method: 'automatic',
                            verified: true
                        }]->(p3)
                        """).consume();

                return null;
            });
        }
    }

    private void showFindNodesByFilter() {
        System.out.println();
        System.out.println("=== FIND NODES BY FILTER ===");
        System.out.println("Активные скважины глубже 3000 м:");

        Map<String, Object> params = new HashMap<>();
        params.put("status", "active");
        params.put("depth", 3000.0);

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (w:Well)
                        WHERE w.status = $status
                          AND w.depth > $depth
                        RETURN w
                        ORDER BY w.wellId
                        """, params);

                while (result.hasNext()) {
                    var node = result.next().get("w").asNode();
                    System.out.println(node.asMap());
                }

                return null;
            });
        }
    }

    private void showUpdateNodesByFilter() {
        System.out.println();
        System.out.println("=== UPDATE NODES BY FILTER ===");
        System.out.println("Меняем статус активных скважин глубже 3000 м на needs_inspection");

        Map<String, Object> params = new HashMap<>();
        params.put("oldStatus", "active");
        params.put("newStatus", "needs_inspection");
        params.put("depth", 3000.0);

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                var result = tx.run("""
                        MATCH (w:Well)
                        WHERE w.status = $oldStatus
                          AND w.depth > $depth
                        SET w.status = $newStatus
                        RETURN count(w) AS updatedCount
                        """, params);

                System.out.println("Обновлено узлов: " + result.single().get("updatedCount").asLong());

                return null;
            });
        }

        System.out.println("Скважины после обновления:");
        printAllWells();
    }

    private void showDeleteNodesByFilter() {
        System.out.println();
        System.out.println("=== DELETE NODES BY FILTER ===");
        System.out.println("Удаляем Production, где oil < 50");

        Map<String, Object> params = new HashMap<>();
        params.put("oilLimit", 50.0);

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                var relationResult = tx.run("""
                        MATCH ()-[r:HAS_PRODUCTION]->(p:Production)
                        WHERE p.oil < $oilLimit
                        DELETE r
                        RETURN count(r) AS deletedRelations
                        """, params);

                long deletedRelations = relationResult.single().get("deletedRelations").asLong();

                var nodeResult = tx.run("""
                        MATCH (p:Production)
                        WHERE p.oil < $oilLimit
                        DELETE p
                        RETURN count(p) AS deletedNodes
                        """, params);

                long deletedNodes = nodeResult.single().get("deletedNodes").asLong();

                System.out.println("Удалено связей HAS_PRODUCTION: " + deletedRelations);
                System.out.println("Удалено узлов Production: " + deletedNodes);

                return null;
            });
        }

        System.out.println("Production после удаления:");
        printAllProductions();
    }

    private void showFindRelationshipsByFilter() {
        System.out.println();
        System.out.println("=== FIND RELATIONSHIPS BY FILTER ===");
        System.out.println("Активные связи HAS_WELL:");

        Map<String, Object> params = new HashMap<>();
        params.put("relationStatus", "active");

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (f:Field)-[r:HAS_WELL]->(w:Well)
                        WHERE r.relationStatus = $relationStatus
                        RETURN f.name AS fieldName, r, w.number AS wellNumber
                        ORDER BY w.wellId
                        """, params);

                while (result.hasNext()) {
                    var record = result.next();

                    System.out.println(
                            record.get("fieldName").asString()
                                    + " -["
                                    + record.get("r").asRelationship().asMap()
                                    + "]-> "
                                    + record.get("wellNumber").asString()
                    );
                }

                return null;
            });
        }
    }

    private void showUpdateRelationshipsByFilter() {
        System.out.println();
        System.out.println("=== UPDATE RELATIONSHIPS BY FILTER ===");
        System.out.println("Обновляем comment у активных связей HAS_WELL");

        Map<String, Object> params = new HashMap<>();
        params.put("relationStatus", "active");
        params.put("comment", "Связь проверена");

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                var result = tx.run("""
                        MATCH (:Field)-[r:HAS_WELL]->(:Well)
                        WHERE r.relationStatus = $relationStatus
                        SET r.comment = $comment
                        RETURN count(r) AS updatedRelations
                        """, params);

                System.out.println("Обновлено связей: " + result.single().get("updatedRelations").asLong());

                return null;
            });
        }

        System.out.println("Связи после обновления:");
        showFindRelationshipsByFilter();
    }

    private void showDeleteRelationshipsByFilter() {
        System.out.println();
        System.out.println("=== DELETE RELATIONSHIPS BY FILTER ===");
        System.out.println("Удаляем архивные связи HAS_WELL");

        Map<String, Object> params = new HashMap<>();
        params.put("relationStatus", "archived");

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                var result = tx.run("""
                        MATCH (:Field)-[r:HAS_WELL]->(:Well)
                        WHERE r.relationStatus = $relationStatus
                        DELETE r
                        RETURN count(r) AS deletedRelations
                        """, params);

                System.out.println("Удалено связей: " + result.single().get("deletedRelations").asLong());

                return null;
            });
        }

        System.out.println("Оставшиеся связи HAS_WELL:");
        printAllFieldWellRelationships();
    }

    private void printAllWells() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (w:Well)
                        RETURN w
                        ORDER BY w.wellId
                        """);

                while (result.hasNext()) {
                    System.out.println(result.next().get("w").asNode().asMap());
                }

                return null;
            });
        }
    }

    private void printAllProductions() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (p:Production)
                        RETURN p
                        ORDER BY p.productionId
                        """);

                while (result.hasNext()) {
                    System.out.println(result.next().get("p").asNode().asMap());
                }

                return null;
            });
        }
    }

    private void printAllFieldWellRelationships() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (f:Field)-[r:HAS_WELL]->(w:Well)
                        RETURN f.name AS fieldName, r, w.number AS wellNumber
                        ORDER BY w.wellId
                        """);

                while (result.hasNext()) {
                    var record = result.next();

                    System.out.println(
                            record.get("fieldName").asString()
                                    + " -["
                                    + record.get("r").asRelationship().asMap()
                                    + "]-> "
                                    + record.get("wellNumber").asString()
                    );
                }

                return null;
            });
        }
    }
}