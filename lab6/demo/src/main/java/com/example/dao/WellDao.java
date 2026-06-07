package com.example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
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

    private static final String INSERT_BY_ID_CQL = """
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

    private static final String INSERT_BY_FIELD_CQL = """
            INSERT INTO wells_by_field (
                field_id,
                id,
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
                :field_id,
                :id,
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

    private static final String INSERT_BY_STATUS_DEPTH_CQL = """
            INSERT INTO wells_by_status_depth (
                status,
                depth,
                id,
                field_id,
                number,
                well_type,
                diameter,
                producer_pump_type,
                producer_target_oil,
                injector_injection_pressure,
                injector_target_water,
                exploration_core_depth,
                exploration_seismic_quality
            )
            VALUES (
                :status,
                :depth,
                :id,
                :field_id,
                :number,
                :well_type,
                :diameter,
                :producer_pump_type,
                :producer_target_oil,
                :injector_injection_pressure,
                :injector_target_water,
                :exploration_core_depth,
                :exploration_seismic_quality
            )
            """;

    private static final String INSERT_BY_TYPE_STATUS_CQL = """
            INSERT INTO wells_by_type_status (
                well_type,
                status,
                id,
                field_id,
                number,
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
                :well_type,
                :status,
                :id,
                :field_id,
                :number,
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

    private final CqlSession session;

    private final PreparedStatement insertByIdStatement;
    private final PreparedStatement insertByFieldStatement;
    private final PreparedStatement insertByStatusDepthStatement;
    private final PreparedStatement insertByTypeStatusStatement;

    private final PreparedStatement selectByIdStatement;
    private final PreparedStatement selectByFieldIdStatement;
    private final PreparedStatement selectByStatusAndDepthStatement;
    private final PreparedStatement selectByTypeAndStatusStatement;

    private final PreparedStatement deleteByIdStatement;
    private final PreparedStatement deleteByFieldStatement;
    private final PreparedStatement deleteByStatusDepthStatement;
    private final PreparedStatement deleteByTypeStatusStatement;

    private final PreparedStatement selectByProducerPumpTypeStatement;
    private final PreparedStatement selectByMinInjectionPressureStatement;
    private final PreparedStatement selectByExplorationSeismicQualityStatement;

    public WellDao() {
        this.session = CassandraSessionManager.getSession();

        this.insertByIdStatement = session.prepare(INSERT_BY_ID_CQL);
        this.insertByFieldStatement = session.prepare(INSERT_BY_FIELD_CQL);
        this.insertByStatusDepthStatement = session.prepare(INSERT_BY_STATUS_DEPTH_CQL);
        this.insertByTypeStatusStatement = session.prepare(INSERT_BY_TYPE_STATUS_CQL);

        this.selectByIdStatement = session.prepare("""
                SELECT *
                FROM wells_by_id
                WHERE id = ?
                """);

        this.selectByFieldIdStatement = session.prepare("""
                SELECT *
                FROM wells_by_field
                WHERE field_id = ?
                """);

        this.selectByStatusAndDepthStatement = session.prepare("""
                SELECT *
                FROM wells_by_status_depth
                WHERE status = ?
                  AND depth > ?
                """);

        this.selectByTypeAndStatusStatement = session.prepare("""
                SELECT *
                FROM wells_by_type_status
                WHERE well_type = ?
                  AND status = ?
                """);

        this.deleteByIdStatement = session.prepare("""
                DELETE FROM wells_by_id
                WHERE id = ?
                """);

        this.deleteByFieldStatement = session.prepare("""
                DELETE FROM wells_by_field
                WHERE field_id = ?
                  AND id = ?
                """);

        this.deleteByStatusDepthStatement = session.prepare("""
                DELETE FROM wells_by_status_depth
                WHERE status = ?
                  AND depth = ?
                  AND id = ?
                """);

        this.deleteByTypeStatusStatement = session.prepare("""
                DELETE FROM wells_by_type_status
                WHERE well_type = ?
                  AND status = ?
                  AND id = ?
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
        validateRequiredFields(well);

        try {
            session.execute(buildInsertStatement(insertByIdStatement, well));
            session.execute(buildInsertStatement(insertByFieldStatement, well));
            session.execute(buildInsertStatement(insertByStatusDepthStatement, well));
            session.execute(buildInsertStatement(insertByTypeStatusStatement, well));
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
            ResultSet resultSet = session.execute(selectByFieldIdStatement.bind(fieldId));
            return mapRows(resultSet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get wells by field id", e);
        }
    }

    public List<Well> findByStatusAndDepthGreaterThan(String status, double depth) {
        try {
            ResultSet resultSet = session.execute(
                    selectByStatusAndDepthStatement.bind(status, depth)
            );

            return mapRows(resultSet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by status and depth", e);
        }
    }

    public List<Well> findByTypeAndStatus(String wellType, String status) {
        try {
            ResultSet resultSet = session.execute(
                    selectByTypeAndStatusStatement.bind(wellType, status)
            );

            return mapRows(resultSet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by type and status", e);
        }
    }

    public int updateStatusByStatusAndDepthGreaterThan(
            String currentStatus,
            double depth,
            String newStatus
    ) {
        List<Well> wells = findByStatusAndDepthGreaterThan(currentStatus, depth);

        for (Well well : wells) {
            Well updatedWell = new Well(
                    well.getId(),
                    well.getFieldId(),
                    well.getNumber(),
                    newStatus,
                    well.getWellType(),
                    well.getDepth(),
                    well.getDiameter(),
                    well.getProducerPumpType(),
                    well.getProducerTargetOil(),
                    well.getInjectorInjectionPressure(),
                    well.getInjectorTargetWater(),
                    well.getExplorationCoreDepth(),
                    well.getExplorationSeismicQuality()
            );

            update(updatedWell);
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

    public List<Well> findByProducerPumpType(String pumpType) {
        try {
            ResultSet resultSet = session.execute(
                    selectByProducerPumpTypeStatement.bind(pumpType)
            );

            return mapRows(resultSet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by producer pump type", e);
        }
    }

    public List<Well> findByMinInjectionPressure(double minPressure) {
        try {
            ResultSet resultSet = session.execute(
                    selectByMinInjectionPressureStatement.bind(minPressure)
            );

            return mapRows(resultSet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by injection pressure", e);
        }
    }

    public List<Well> findByExplorationSeismicQuality(String seismicQuality) {
        try {
            ResultSet resultSet = session.execute(
                    selectByExplorationSeismicQualityStatement.bind(seismicQuality)
            );

            return mapRows(resultSet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find wells by exploration seismic quality", e);
        }
    }

    @Override
    public void update(Well well) {
        validateRequiredFields(well);

        try {
            Optional<Well> currentWell = getById(well.getId());

            if (currentWell.isEmpty()) {
                throw new IllegalStateException("Well with id " + well.getId() + " not found");
            }

            deleteExistingWell(currentWell.get());
            create(well);

        } catch (Exception e) {
            throw new RuntimeException("Failed to update well", e);
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            Optional<Well> currentWell = getById(id);

            if (currentWell.isEmpty()) {
                return;
            }

            deleteExistingWell(currentWell.get());
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete well", e);
        }
    }

    private BoundStatement buildInsertStatement(PreparedStatement statement, Well well) {
        BoundStatementBuilder builder = statement.boundStatementBuilder();

        builder.setUuid("id", well.getId());
        builder.setUuid("field_id", well.getFieldId());
        builder.setString("number", well.getNumber());
        builder.setString("status", well.getStatus());
        builder.setString("well_type", well.getWellType());
        builder.setDouble("depth", well.getDepth());

        setNullableDouble(builder, "diameter", well.getDiameter());

        setNullableString(builder, "producer_pump_type", well.getProducerPumpType());
        setNullableDouble(builder, "producer_target_oil", well.getProducerTargetOil());

        setNullableDouble(builder, "injector_injection_pressure", well.getInjectorInjectionPressure());
        setNullableDouble(builder, "injector_target_water", well.getInjectorTargetWater());

        setNullableDouble(builder, "exploration_core_depth", well.getExplorationCoreDepth());
        setNullableString(builder, "exploration_seismic_quality", well.getExplorationSeismicQuality());

        return builder.build();
    }

    private void deleteExistingWell(Well well) {
        session.execute(deleteByIdStatement.bind(
                well.getId()
        ));

        session.execute(deleteByFieldStatement.bind(
                well.getFieldId(),
                well.getId()
        ));

        session.execute(deleteByStatusDepthStatement.bind(
                well.getStatus(),
                well.getDepth(),
                well.getId()
        ));

        session.execute(deleteByTypeStatusStatement.bind(
                well.getWellType(),
                well.getStatus(),
                well.getId()
        ));
    }

    private List<Well> mapRows(ResultSet resultSet) {
        List<Well> result = new ArrayList<>();

        for (Row row : resultSet) {
            result.add(mapRow(row));
        }

        return result;
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

    private void validateRequiredFields(Well well) {
        if (well.getId() == null) {
            throw new IllegalArgumentException("Well id is required");
        }

        if (well.getFieldId() == null) {
            throw new IllegalArgumentException("Field id is required");
        }

        if (well.getNumber() == null || well.getNumber().isBlank()) {
            throw new IllegalArgumentException("Well number is required");
        }

        if (well.getStatus() == null || well.getStatus().isBlank()) {
            throw new IllegalArgumentException("Well status is required");
        }

        if (well.getWellType() == null || well.getWellType().isBlank()) {
            throw new IllegalArgumentException("Well type is required");
        }

        if (well.getDepth() == null) {
            throw new IllegalArgumentException("Well depth is required");
        }
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