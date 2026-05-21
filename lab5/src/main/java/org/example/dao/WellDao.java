package org.example.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.domain.model.Well;
import org.example.domain.relationship.FieldWellRelation;
import org.example.infrastructure.Neo4jConnectionManager;
import org.example.mapper.WellMapper;
import org.neo4j.driver.Session;

public class WellDao implements CrudDao<Well, Long> {

    @Override
    public Long create(Well well) {
        if (well.getWellId() == null) {
            throw new IllegalArgumentException("Well wellId must not be null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("wellId", well.getWellId());
        params.put("number", well.getNumber());
        params.put("status", well.getStatus());
        params.put("depth", well.getDepth());
        params.put("diameter", well.getDiameter());

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        CREATE (w:Well {
                            wellId: $wellId,
                            number: $number,
                            status: $status,
                            depth: $depth,
                            diameter: $diameter
                        })
                        """, params).consume();

                return null;
            });
        }

        return well.getWellId();
    }

    public Long create(Well well, Long fieldId, FieldWellRelation relation) {
        if (well.getWellId() == null) {
            throw new IllegalArgumentException("Well wellId must not be null");
        }

        if (fieldId == null) {
            throw new IllegalArgumentException("Field fieldId must not be null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("fieldId", fieldId);

        params.put("wellId", well.getWellId());
        params.put("number", well.getNumber());
        params.put("status", well.getStatus());
        params.put("depth", well.getDepth());
        params.put("diameter", well.getDiameter());

        params.put("assignedAt", relation.getAssignedAt().toString());
        params.put("relationStatus", relation.getRelationStatus());
        params.put("comment", relation.getComment());

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (f:Field {fieldId: $fieldId})
                        CREATE (w:Well {
                            wellId: $wellId,
                            number: $number,
                            status: $status,
                            depth: $depth,
                            diameter: $diameter
                        })
                        CREATE (f)-[:HAS_WELL {
                            assignedAt: date($assignedAt),
                            relationStatus: $relationStatus,
                            comment: $comment
                        }]->(w)
                        """, params).consume();

                return null;
            });
        }

        return well.getWellId();
    }

    @Override
    public Well getById(Long wellId) {
        Map<String, Object> params = new HashMap<>();
        params.put("wellId", wellId);

        try (Session session = Neo4jConnectionManager.getSession()) {
            return session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (w:Well {wellId: $wellId})
                        RETURN w
                        """, params);

                if (!result.hasNext()) {
                    return null;
                }

                return WellMapper.fromNode(result.single().get("w").asNode());
            });
        }
    }

    @Override
    public List<Well> getAll() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            return session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (w:Well)
                        RETURN w
                        ORDER BY w.wellId
                        """);

                List<Well> wells = new ArrayList<>();

                while (result.hasNext()) {
                    wells.add(WellMapper.fromNode(result.next().get("w").asNode()));
                }

                return wells;
            });
        }
    }

    @Override
    public void update(Well well) {
        if (well.getWellId() == null) {
            throw new IllegalArgumentException("Well wellId must not be null for update");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("wellId", well.getWellId());
        params.put("number", well.getNumber());
        params.put("status", well.getStatus());
        params.put("depth", well.getDepth());
        params.put("diameter", well.getDiameter());

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (w:Well {wellId: $wellId})
                        SET w.number = $number,
                            w.status = $status,
                            w.depth = $depth,
                            w.diameter = $diameter
                        """, params).consume();

                return null;
            });
        }
    }

    @Override
    public void delete(Long wellId) {
        Map<String, Object> params = new HashMap<>();
        params.put("wellId", wellId);

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (w:Well {wellId: $wellId})-[r:HAS_PRODUCTION]->()
                        DELETE r
                        """, params).consume();

                tx.run("""
                        MATCH ()-[r:HAS_WELL]->(w:Well {wellId: $wellId})
                        DELETE r
                        """, params).consume();

                tx.run("""
                        MATCH (w:Well {wellId: $wellId})
                        DELETE w
                        """, params).consume();

                return null;
            });
        }
    }
}