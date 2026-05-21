package org.example.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.domain.model.Field;
import org.example.infrastructure.Neo4jConnectionManager;
import org.example.mapper.FieldMapper;

import org.neo4j.driver.Session;

public class FieldDao implements CrudDao<Field, Long> {

    @Override
    public Long create(Field field) {
        Long fieldId = field.getFieldId();

        if (fieldId == null) {
            throw new IllegalArgumentException("Field fieldId must not be null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("fieldId", fieldId);
        params.put("name", field.getName());
        params.put("region", field.getRegion());
        params.put("latitude", field.getLatitude());
        params.put("longitude", field.getLongitude());
        params.put("oilReserves", field.getOilReserves());
        

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        CREATE (f:Field {
                            fieldId: $fieldId,
                            name: $name,
                            region: $region,
                            latitude: $latitude,
                            longitude: $longitude,
                            oilReserves: $oilReserves
                        })
                        """,
                        params
                ).consume();

                return null;
            });
        }

        return fieldId;
    }

    @Override
    public Field getById(Long fieldId) {
        Map<String, Object> params = new HashMap<>();
        params.put("fieldId", fieldId);

        try (Session session = Neo4jConnectionManager.getSession()) {
            return session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (f:Field {fieldId: $fieldId})
                        RETURN f
                        """, params);

                if (!result.hasNext()) {
                    return null;
                }

                return FieldMapper.fromNode(result.single().get("f").asNode());
            });
        }
    }

    @Override
    public List<Field> getAll() {
        try (Session session = Neo4jConnectionManager.getSession()) {
            return session.executeRead(tx -> {
                var result = tx.run("""
                        MATCH (f:Field)
                        RETURN f
                        ORDER BY f.name
                        """);

                List<Field> fields = new ArrayList<>();

                while (result.hasNext()) {
                    fields.add(FieldMapper.fromNode(result.next().get("f").asNode()));
                }

                return fields;
            });
        }
    }

    @Override
    public void update(Field field) {
        
        if (field.getFieldId() == null) {
            throw new IllegalArgumentException("Field fieldId must not be null for update");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("fieldId", field.getFieldId());
        params.put("name", field.getName());
        params.put("region", field.getRegion());
        params.put("latitude", field.getLatitude());
        params.put("longitude", field.getLongitude());
        params.put("oilReserves", field.getOilReserves());

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {
                tx.run("""
                        MATCH (f:Field {fieldId: $fieldId})
                        SET f.name = $name,
                            f.region = $region,
                            f.latitude = $latitude,
                            f.longitude = $longitude,
                            f.oilReserves = $oilReserves
                        """, params).consume();

                return null;
            });
        }
    }

    @Override
    public void delete(Long fieldId) {
        Map<String, Object> params = new HashMap<>();
        params.put("fieldId", fieldId);

        try (Session session = Neo4jConnectionManager.getSession()) {
            session.executeWrite(tx -> {

                tx.run("""
                        MATCH (f:Field {fieldId: $fieldId})-[r:HAS_WELL]->()
                        DELETE r
                        """,
                        params
                ).consume();

                tx.run("""
                        MATCH (f:Field {fieldId: $fieldId})
                        DELETE f
                        """,
                        params
                ).consume();

                return null;
            });
        }
    }
}
