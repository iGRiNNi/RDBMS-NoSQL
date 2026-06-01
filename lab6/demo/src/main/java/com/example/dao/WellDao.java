package com.example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.example.domain.model.Well;
import com.example.infrastructure.cassandra.CassandraSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WellDao implements CrudDao<Well, UUID> {

    private final CqlSession session;

    private final PreparedStatement insertStatement;
    private final PreparedStatement selectByIdStatement;
    private final PreparedStatement selectByFieldIdStatement;
    private final PreparedStatement updateStatement;
    private final PreparedStatement deleteStatement;

    public WellDao() {
        this.session = CassandraSessionManager.getSession();

        this.insertStatement = session.prepare("""
                INSERT INTO wells_by_id (
                    id,
                    field_id,
                    number,
                    status,
                    well_type,
                    depth,
                    diameter,
                    producer_pump_type,
                    producer_target_oil,
                    injector_injection_pressure,
                    injector_target_water,
                    exploration_core_depth,
                    exploration_seismic_quality
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """);

        this.selectByIdStatement = session.prepare("""
                SELECT *
                FROM wells_by_id
                WHERE id = ?
                """);

        this.selectByFieldIdStatement = session.prepare("""
                SELECT *
                FROM wells_by_id
                WHERE field_id = ?
                """);

        this.updateStatement = session.prepare("""
                UPDATE wells_by_id
                SET field_id = ?,
                    number = ?,
                    status = ?,
                    well_type = ?,
                    depth = ?,
                    diameter = ?,
                    producer_pump_type = ?,
                    producer_target_oil = ?,
                    injector_injection_pressure = ?,
                    injector_target_water = ?,
                    exploration_core_depth = ?,
                    exploration_seismic_quality = ?
                WHERE id = ?
                """);

        this.deleteStatement = session.prepare("""
                DELETE FROM wells_by_id
                WHERE id = ?
                """);
    }

    @Override
    public void create(Well well) {
        try {
            session.execute(insertStatement.bind(
                    well.getId(),
                    well.getFieldId(),
                    well.getNumber(),
                    well.getStatus(),
                    well.getWellType(),
                    well.getDepth(),
                    well.getDiameter(),
                    well.getProducerPumpType(),
                    well.getProducerTargetOil(),
                    well.getInjectorInjectionPressure(),
                    well.getInjectorTargetWater(),
                    well.getExplorationCoreDepth(),
                    well.getExplorationSeismicQuality()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create well", e);
        }
    }

    @Override
    public Optional<Well> getById(UUID id) {
        try {
            Row row = session.execute(selectByIdStatement.bind(id)).one();

            if (row == null) {
                return Optional.empty();
            }

            return Optional.of(mapRow(row));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get well by id", e);
        }
    }

    public List<Well> findByFieldId(UUID fieldId) {
        try {
            List<Well> result = new ArrayList<>();
            ResultSet resultSet = session.execute(selectByFieldIdStatement.bind(fieldId));

            for (Row row : resultSet) {
                result.add(mapRow(row));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get wells by field id", e);
        }
    }

    @Override
    public void update(Well well) {
        try {
            session.execute(updateStatement.bind(
                    well.getFieldId(),
                    well.getNumber(),
                    well.getStatus(),
                    well.getWellType(),
                    well.getDepth(),
                    well.getDiameter(),
                    well.getProducerPumpType(),
                    well.getProducerTargetOil(),
                    well.getInjectorInjectionPressure(),
                    well.getInjectorTargetWater(),
                    well.getExplorationCoreDepth(),
                    well.getExplorationSeismicQuality(),
                    well.getId()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to update well", e);
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            session.execute(deleteStatement.bind(id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete well", e);
        }
    }

    private Well mapRow(Row row) {
        return new Well(
                row.getUuid("id"),
                row.getUuid("field_id"),
                row.getString("number"),
                row.getString("status"),
                row.getString("well_type"),
                getNullableDouble(row, "depth"),
                getNullableDouble(row, "diameter"),
                row.getString("producer_pump_type"),
                getNullableDouble(row, "producer_target_oil"),
                getNullableDouble(row, "injector_injection_pressure"),
                getNullableDouble(row, "injector_target_water"),
                getNullableDouble(row, "exploration_core_depth"),
                row.getString("exploration_seismic_quality")
        );
    }

    private Double getNullableDouble(Row row, String columnName) {
        return row.isNull(columnName) ? null : row.getDouble(columnName);
    }
}