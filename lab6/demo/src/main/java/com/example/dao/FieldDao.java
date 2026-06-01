package com.example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Row;
import com.example.domain.model.Field;
import com.example.infrastructure.cassandra.CassandraSessionManager;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class FieldDao implements CrudDao<Field, UUID> {

    private final CqlSession session;

    private final PreparedStatement insertStatement;
    private final PreparedStatement selectByIdStatement;
    private final PreparedStatement updateStatement;
    private final PreparedStatement deleteStatement;

    public FieldDao() {
        this.session = CassandraSessionManager.getSession();

        this.insertStatement = session.prepare("""
                INSERT INTO fields_by_id (
                    id, name, region, latitude, longitude, start_date
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """);

        this.selectByIdStatement = session.prepare("""
                SELECT id, name, region, latitude, longitude, start_date
                FROM fields_by_id
                WHERE id = ?
                """);

        this.updateStatement = session.prepare("""
                UPDATE fields_by_id
                SET name = ?,
                    region = ?,
                    latitude = ?,
                    longitude = ?,
                    start_date = ?
                WHERE id = ?
                """);

        this.deleteStatement = session.prepare("""
                DELETE FROM fields_by_id
                WHERE id = ?
                """);
    }

    @Override
    public void create(Field field) {
        try {
            session.execute(insertStatement.bind(
                    field.getId(),
                    field.getName(),
                    field.getRegion(),
                    field.getLatitude(),
                    field.getLongitude(),
                    field.getStartDate()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create field", e);
        }
    }

    @Override
    public Optional<Field> getById(UUID id) {
        try {
            Row row = session.execute(selectByIdStatement.bind(id)).one();

            if (row == null) {
                return Optional.empty();
            }

            return Optional.of(mapRow(row));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field by id", e);
        }
    }

    @Override
    public void update(Field field) {
        try {
            session.execute(updateStatement.bind(
                    field.getName(),
                    field.getRegion(),
                    field.getLatitude(),
                    field.getLongitude(),
                    field.getStartDate(),
                    field.getId()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to update field", e);
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            session.execute(deleteStatement.bind(id));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete field", e);
        }
    }

    private Field mapRow(Row row) {
        return new Field(
                row.getUuid("id"),
                row.getString("name"),
                row.getString("region"),
                getNullableDouble(row, "latitude"),
                getNullableDouble(row, "longitude"),
                getNullableDate(row, "start_date")
        );
    }

    private Double getNullableDouble(Row row, String columnName) {
        return row.isNull(columnName) ? null : row.getDouble(columnName);
    }

    private LocalDate getNullableDate(Row row, String columnName) {
        return row.isNull(columnName) ? null : row.getLocalDate(columnName);
    }
}