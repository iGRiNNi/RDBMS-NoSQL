package org.example.demo;

import org.example.infrastructure.Neo4jConnectionManager;
import org.neo4j.driver.Session;

import java.util.HashMap;
import java.util.Map;

public class ComplexQueryDemo {

    public void run() {
        cleanup();
        seedData();

        showHighOilVerifiedActiveWellsInRegion();
        showWaterMoreThanOil();
        showGasRichVerifiedProductions();
        showUnionComplexQuery();
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
                        
                        CREATE (f3:Field {
                            fieldId: 3,
                            name: 'Приобское',
                            region: 'Ханты-Мансийский АО',
                            latitude: 61.000,
                            longitude: 73.500,
                            oilReserves: 5000000000.0
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
                        
                        CREATE (w4:Well {
                            wellId: 4,
                            number: 'ПР-101',
                            status: 'active',
                            depth: 3500.0,
                            diameter: 0.25
                        })
                        
                        CREATE (w5:Well {
                            wellId: 5,
                            number: 'ПР-102',
                            status: 'disabled',
                            depth: 2800.0,
                            diameter: 0.22
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
                        
                        CREATE (p4:Production {
                            productionId: 4,
                            productionDate: date('2026-03-12'),
                            oil: 180.0,
                            gas: 750.0,
                            water: 30.0
                        })
                        
                        CREATE (p5:Production {
                            productionId: 5,
                            productionDate: date('2026-03-13'),
                            oil: 110.0,
                            gas: 650.0,
                            water: 60.0
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
                        
                        CREATE (f3)-[:HAS_WELL {
                            assignedAt: date('2022-04-01'),
                            relationStatus: 'active',
                            comment: 'Высокодебитная скважина'
                        }]->(w4)
                        
                        CREATE (f3)-[:HAS_WELL {
                            assignedAt: date('2023-01-12'),
                            relationStatus: 'planned',
                            comment: 'Плановая скважина'
                        }]->(w5)
                        
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
                        
                        CREATE (w4)-[:HAS_PRODUCTION {
                            measuredAt: datetime('2026-03-12T11:30:00'),
                            method: 'automatic',
                            verified: true
                        }]->(p4)
                        
                        CREATE (w5)-[:HAS_PRODUCTION {
                            measuredAt: datetime('2026-03-13T12:00:00'),
                            method: 'manual',
                            verified: true
                        }]->(p5)
                        """).consume();

                return null;
            });
        }
    }

    private void showHighOilVerifiedActiveWellsInRegion() {
        System.out.println();
        System.out.println("=== COMPLEX QUERY 1 ===");
        System.out.println("Активные скважины в ХМАО с подтверждённой добычей нефти больше 100 т");

        Map<String, Object> params = new HashMap<>();
        params.put("region", "Ханты-Мансийский АО");
        params.put("relationStatus", "active");
        params.put("wellStatus", "active");
        params.put("verified", true);
        params.put("oilLimit", 100.0);

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (f:Field)-[fw:HAS_WELL]->(w:Well)-[wp:HAS_PRODUCTION]->(p:Production)
                        WHERE f.region = $region
                          AND fw.relationStatus = $relationStatus
                          AND w.status = $wellStatus
                          AND wp.verified = $verified
                          AND p.oil > $oilLimit
                        RETURN f.name AS fieldName,
                               w.number AS wellNumber,
                               p.productionDate AS productionDate,
                               p.oil AS oil,
                               wp.method AS method
                        ORDER BY p.oil DESC
                        """, params);

                printQueryResult(result);
                return null;
            });
        }
    }

    private void showWaterMoreThanOil() {
        System.out.println();
        System.out.println("=== COMPLEX QUERY 2 ===");
        System.out.println("Скважины, у которых средняя вода больше средней нефти");

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (f:Field)-[:HAS_WELL]->(w:Well)-[:HAS_PRODUCTION]->(p:Production)
                        WHERE w.status = 'active'
                        WITH f.name AS fieldName,
                            w.number AS wellNumber,
                            avg(p.oil) AS avgOil,
                            avg(p.water) AS avgWater
                        WHERE avgWater > avgOil
                        RETURN fieldName, wellNumber, avgOil, avgWater
                        ORDER BY avgWater DESC
                        """);

                printQueryResult(result);
                return null;
            });
        }
    }

    private void showGasRichVerifiedProductions() {
        System.out.println();
        System.out.println("=== COMPLEX QUERY 3 ===");
        System.out.println("Подтверждённые газонасыщенные записи по актуальным или плановым связям");

        Map<String, Object> params = new HashMap<>();
        params.put("relationStatus1", "active");
        params.put("relationStatus2", "planned");
        params.put("wellStatus1", "active");
        params.put("wellStatus2", "disabled");
        params.put("verified", true);
        params.put("minGas", 600.0);
        params.put("dateFrom", "2026-03-01");

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (f:Field)-[fw:HAS_WELL]->(w:Well)-[wp:HAS_PRODUCTION]->(p:Production)
                        WHERE f.region IS NOT NULL
                          AND fw.relationStatus IN [$relationStatus1, $relationStatus2]
                          AND w.status IN [$wellStatus1, $wellStatus2]
                          AND wp.verified = $verified
                          AND p.gas > $minGas
                          AND p.productionDate >= date($dateFrom)
                        RETURN f.name AS fieldName,
                               f.region AS region,
                               fw.relationStatus AS relationStatus,
                               w.number AS wellNumber,
                               w.status AS wellStatus,
                               p.productionDate AS productionDate,
                               p.gas AS gas
                        ORDER BY p.gas DESC
                        """, params);

                printQueryResult(result);
                return null;
            });
        }
    }

    private void showUnionComplexQuery() {
        System.out.println();
        System.out.println("=== COMPLEX QUERY WITH UNION ===");
        System.out.println("Скважины с высокой добычей нефти или газа");

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (f:Field)-[fw:HAS_WELL]->(w:Well)-[wp:HAS_PRODUCTION]->(p:Production)
                        WHERE f.region = 'Ханты-Мансийский АО'
                        AND fw.relationStatus = 'active'
                        AND w.status = 'active'
                        AND wp.verified = true
                        AND p.oil > 100
                        RETURN 'HIGH_OIL' AS reason,
                            f.name AS fieldName,
                            w.number AS wellNumber,
                            p.productionDate AS productionDate,
                            p.oil AS value
                        
                        UNION
                        
                        MATCH (f:Field)-[fw:HAS_WELL]->(w:Well)-[wp:HAS_PRODUCTION]->(p:Production)
                        WHERE f.region IS NOT NULL
                        AND fw.relationStatus IN ['active', 'planned']
                        AND w.status IN ['active', 'disabled']
                        AND wp.verified = true
                        AND p.gas > 600
                        RETURN 'HIGH_GAS' AS reason,
                            f.name AS fieldName,
                            w.number AS wellNumber,
                            p.productionDate AS productionDate,
                            p.gas AS value
                        """);

                printQueryResult(result);
                return null;
            });
        }
    }

    private void printQueryResult(org.neo4j.driver.Result result) {
        boolean hasRows = false;

        while (result.hasNext()) {
            hasRows = true;
            System.out.println(result.next().asMap());
        }

        if (!hasRows) {
            System.out.println("Нет подходящих данных");
        }
    }
}
