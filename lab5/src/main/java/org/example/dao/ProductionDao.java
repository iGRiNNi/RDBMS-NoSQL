package org.example.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.domain.model.Production;
import org.example.domain.relationship.WellProductionRelation;
import org.example.infrastructure.Neo4jConnectionManager;
import org.example.mapper.ProductionMapper;
import org.neo4j.driver.Session;

public class ProductionDao implements CrudDao<Production, Long> {

    @Override
    public Long create(Production production) {
        if (production.getProductionId() == null) {
            throw new IllegalArgumentException("Production productionId must not be null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("productionId", production.getProductionId());
        params.put("productionDate", production.getProductionDate().toString());
        params.put("oil", production.getOil());
        params.put("gas", production.getGas());
        params.put("water", production.getWater());

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        CREATE (p:Production {
                            productionId: $productionId,
                            productionDate: date($productionDate),
                            oil: $oil,
                            gas: $gas,
                            water: $water
                        })
                        """, params).consume();

                return null;
            });
        }

        return production.getProductionId();
    }

    public Long create(Production production, Long wellId, WellProductionRelation relation) {
        if (production.getProductionId() == null) {
            throw new IllegalArgumentException("Production productionId must not be null");
        }

        if (wellId == null) {
            throw new IllegalArgumentException("Well wellId must not be null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("wellId", wellId);

        params.put("productionId", production.getProductionId());
        params.put("productionDate", production.getProductionDate().toString());
        params.put("oil", production.getOil());
        params.put("gas", production.getGas());
        params.put("water", production.getWater());

        params.put("measuredAt", relation.getMeasuredAt().toString());
        params.put("method", relation.getMethod());
        params.put("verified", relation.getVerified());

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (w:Well {wellId: $wellId})
                        CREATE (p:Production {
                            productionId: $productionId,
                            productionDate: date($productionDate),
                            oil: $oil,
                            gas: $gas,
                            water: $water
                        })
                        CREATE (w)-[:HAS_PRODUCTION {
                            measuredAt: datetime($measuredAt),
                            method: $method,
                            verified: $verified
                        }]->(p)
                        """, params).consume();

                return null;
            });
        }

        return production.getProductionId();
    }

    @Override
    public Production getById(Long productionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("productionId", productionId);

        try (Session session = Neo4jConnectionManager.getSession()) {
            return session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (p:Production {productionId: $productionId})
                        RETURN p
                        """, params);

                if (!result.hasNext()) {
                    return null;
                }

                return ProductionMapper.fromNode(result.single().get("p").asNode());
            });
        }
    }

    @Override
    public List<Production> getAll() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            return session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (p:Production)
                        RETURN p
                        ORDER BY p.productionId
                        """);

                List<Production> productions = new ArrayList<>();

                while (result.hasNext()) {
                    productions.add(ProductionMapper.fromNode(result.next().get("p").asNode()));
                }

                return productions;
            });
        }
    }

    @Override
    public void update(Production production) {
        if (production.getProductionId() == null) {
            throw new IllegalArgumentException("Production productionId must not be null for update");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("productionId", production.getProductionId());
        params.put("productionDate", production.getProductionDate().toString());
        params.put("oil", production.getOil());
        params.put("gas", production.getGas());
        params.put("water", production.getWater());

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (p:Production {productionId: $productionId})
                        SET p.productionDate = date($productionDate),
                            p.oil = $oil,
                            p.gas = $gas,
                            p.water = $water
                        """, params).consume();

                return null;
            });
        }
    }

    @Override
    public void delete(Long productionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("productionId", productionId);

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH ()-[r:HAS_PRODUCTION]->(p:Production {productionId: $productionId})
                        DELETE r
                        """, params).consume();

                tx.run("""
                        MATCH (p:Production {productionId: $productionId})
                        DELETE p
                        """, params).consume();

                return null;
            });
        }
    }
}
