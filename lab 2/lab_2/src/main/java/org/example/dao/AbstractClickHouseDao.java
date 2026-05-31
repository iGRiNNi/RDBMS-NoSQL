package org.example.dao;

import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;
import org.example.mapper.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractClickHouseDao<T> implements CrudDao<T, Long> {

    private final RowMapper<T> rowMapper;

    protected AbstractClickHouseDao(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    protected abstract String getTableName();

    protected abstract String getIdColumnName();

    @Override
    public T getById(Long id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get entity by id from table " + getTableName(), e);
        }
    }

    @Override
    public List<T> getAll() {
        String sql = "SELECT * FROM " + getTableName() + " ORDER BY " + getIdColumnName();

        List<T> result = new ArrayList<>();

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get all entities from table " + getTableName(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = """
                ALTER TABLE %s
                DELETE WHERE %s = ?
                """.formatted(getTableName(), getIdColumnName());

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete entity from table " + getTableName(), e);
        }
    }
}