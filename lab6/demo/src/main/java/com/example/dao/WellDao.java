package com.example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
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

    private static final String INSERT_CQL = """
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
        VALUES (
            :id,
            :field_id,
            :number,
            :status,
            :well_type,
            :depth,
            :diameter,
            :producer_pump_type,
            :producer_target_oil,
            :injector_injection_pressure,
            :injector_target_water,
            :exploration_core_depth,
            :exploration_seismic_quality
        )
        """;

    private final PreparedStatement insertStatement;
    private final PreparedStatement selectByIdStatement;
    private final PreparedStatement selectByFieldIdStatement;
    private final PreparedStatement updateStatement;
    private final PreparedStatement deleteStatement;
    private final PreparedStatement selectByStatusAndDepthStatement;
    private final PreparedStatement selectByTypeAndStatusStatement;
    private final PreparedStatement updateStatusByIdStatement;
    private final PreparedStatement selectByProducerPumpTypeStatement;
    private final PreparedStatement selectByMinInjectionPressureStatement;
    private final PreparedStatement selectByExplorationSeismicQualityStatement;

    public WellDao() {
        this.session = CassandraSessionManager.getSession();

        this.insertStatement = session.prepare(INSERT_CQL);

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
        
        this.selectByStatusAndDepthStatement = session.prepare("""
                SELECT *
                FROM wells_by_id
                WHERE status = ?
                AND depth > ?
                """);

        this.selectByTypeAndStatusStatement = session.prepare("""
                SELECT *
                FROM wells_by_id
                WHERE well_type = ?
                AND status = ?
                """);

        this.updateStatusByIdStatement = session.prepare("""
                UPDATE wells_by_id
                SET status = ?
                WHERE id = ?
                """);

        this.selectByProducerPumpTypeStatement = session.prepare("""
                SELECT *
                FROM wells_by_id
                WHERE producer_pump_type = ?
                """);

        this.selectByMinInjectionPressureStatement = session.prepare("""
                SELECT *
                FROM wells_by_id
                WHERE injector_injection_pressure >= ?
                """);

        this.selectByExplorationSeismicQualityStatement = session.prepare("""
                SELECT *
                FROM wells_by_id
                WHERE exploration_seismic_quality = ?
                """);
    }

    @Override
    public void create(Well well) {
        try {
            BoundStatementBuilder builder = insertStatement.boundStatementBuilder();

            builder.setUuid("id", well.getId());
            builder.setUuid("field_id", well.getFieldId());
            builder.setString("number", well.getNumber());
            builder.setString("status", well.getStatus());
            builder.setString("well_type", well.getWellType());

            setNullableDouble(builder, "depth", well.getDepth());
            setNullableDouble(builder, "diameter", well.getDiameter());

            setNullableString(builder, "producer_pump_type", well.getProducerPumpType());
            setNullableDouble(builder, "producer_target_oil", well.getProducerTargetOil());

            setNullableDouble(builder, "injector_injection_pressure", well.getInjectorInjectionPressure());
            setNullableDouble(builder, "injector_target_water", well.getInjectorTargetWater());

            setNullableDouble(builder, "exploration_core_depth", well.getExplorationCoreDepth());
            setNullableString(builder, "exploration_seismic_quality", well.getExplorationSeismicQuality());

            session.execute(builder.build());
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

    public List<Well> findByStatusAndDepthGreaterThan(String status, double depth) {
        try {
            List<Well> result = new ArrayList<>();

            ResultSet resultSet = session.execute(
                    selectByStatusAndDepthStatement.bind(status, depth)
            );

            for (Row row : resultSet) {
                result.add(mapRow(row));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by status and depth", e);
        }
    }

    public List<Well> findByTypeAndStatus(String wellType, String status) {
        try {
            List<Well> result = new ArrayList<>();

            ResultSet resultSet = session.execute(
                    selectByTypeAndStatusStatement.bind(wellType, status)
            );

            for (Row row : resultSet) {
                result.add(mapRow(row));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by type and status", e);
        }
    }

    public int updateStatusByStatusAndDepthGreaterThan(
            String currentStatus,
            double depth,
            String newStatus) {

        List<Well> wells = findByStatusAndDepthGreaterThan(currentStatus, depth);

        for (Well well : wells) {
            updateStatusById(well.getId(), newStatus);
        }

        return wells.size();
    }

    public int deleteByTypeAndStatus(String wellType, String status) {
        List<Well> wells = findByTypeAndStatus(wellType, status);

        for (Well well : wells) {
            delete(well.getId());
        }

        return wells.size();
    }

    private void updateStatusById(UUID id, String newStatus) {
        try {
            session.execute(updateStatusByIdStatement.bind(newStatus, id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to update well status by id", e);
        }
    }

    public List<Well> findByProducerPumpType(String pumpType) {
        try {
            List<Well> result = new ArrayList<>();

            ResultSet resultSet = session.execute(
                    selectByProducerPumpTypeStatement.bind(pumpType)
            );

            for (Row row : resultSet) {
                result.add(mapRow(row));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by producer pump type", e);
        }
    }

    public List<Well> findByMinInjectionPressure(double minPressure) {
        try {
            List<Well> result = new ArrayList<>();

            ResultSet resultSet = session.execute(
                    selectByMinInjectionPressureStatement.bind(minPressure)
            );

            for (Row row : resultSet) {
                result.add(mapRow(row));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by injection pressure", e);
        }
    }

    public List<Well> findByExplorationSeismicQuality(String seismicQuality) {
        try {
            List<Well> result = new ArrayList<>();

            ResultSet resultSet = session.execute(
                    selectByExplorationSeismicQualityStatement.bind(seismicQuality)
            );

            for (Row row : resultSet) {
                result.add(mapRow(row));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by exploration seismic quality", e);
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

    private void setNullableString(BoundStatementBuilder builder, String columnName, String value) {
        if (value != null) {
            builder.setString(columnName, value);
        }
    }

    private void setNullableDouble(BoundStatementBuilder builder, String columnName, Double value) {
        if (value != null) {
            builder.setDouble(columnName, value);
        }
    }
}