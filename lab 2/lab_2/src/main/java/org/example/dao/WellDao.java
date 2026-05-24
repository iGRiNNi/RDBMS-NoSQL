package org.example.dao;

import org.example.domain.model.Well;
import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;
import org.example.mapper.WellRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class WellDao extends AbstractClickHouseDao<Well> {

    public WellDao() {
        super(new WellRowMapper());
    }

    @Override
    protected String getTableName() {
        return "wells";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    public void create(Well well) {
        String sql = """
                INSERT INTO wells (id, number, status, depth, diameter, field_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, well.getId());
            statement.setString(2, well.getNumber());
            statement.setString(3, well.getStatus());
            statement.setObject(4, well.getDepth());
            statement.setObject(5, well.getDiameter());
            statement.setObject(6, well.getFieldId());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create well", e);
        }
    }

    @Override
    public void update(Well well) {
        String sql = """
                ALTER TABLE wells
                UPDATE number = ?,
                       status = ?,
                       depth = ?,
                       diameter = ?,
                       field_id = ?
                WHERE id = ?
                SETTINGS mutations_sync = 1
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, well.getNumber());
            statement.setString(2, well.getStatus());
            statement.setObject(3, well.getDepth());
            statement.setObject(4, well.getDiameter());
            statement.setObject(5, well.getFieldId());
            statement.setLong(6, well.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update well", e);
        }
    }
}