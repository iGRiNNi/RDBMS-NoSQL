package org.example.dao;

import org.example.domain.model.Field;
import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;
import org.example.mapper.FieldRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FieldDao extends AbstractClickHouseDao<Field> {

    public FieldDao() {
        super(new FieldRowMapper());
    }

    @Override
    protected String getTableName() {
        return "fields";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    public void create(Field field) {
        String sql = """
                INSERT INTO fields (id, name, region, latitude, longitude)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, field.getId());
            statement.setString(2, field.getName());
            statement.setObject(3, field.getRegion());
            statement.setObject(4, field.getLatitude());
            statement.setObject(5, field.getLongitude());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create field", e);
        }
    }

    @Override
    public void update(Field field) {
        String sql = """
                ALTER TABLE fields
                UPDATE name = ?,
                       region = ?,
                       latitude = ?,
                       longitude = ?
                WHERE id = ?
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, field.getName());
            statement.setObject(2, field.getRegion());
            statement.setObject(3, field.getLatitude());
            statement.setObject(4, field.getLongitude());
            statement.setLong(5, field.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update field", e);
        }
    }
}