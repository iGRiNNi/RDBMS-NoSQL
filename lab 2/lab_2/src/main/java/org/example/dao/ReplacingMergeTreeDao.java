package org.example.dao;

import org.example.domain.model.WellVersioned;
import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;
import org.example.mapper.WellVersionedRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReplacingMergeTreeDao {

    private final WellVersionedRowMapper rowMapper = new WellVersionedRowMapper();

    public void truncate() {
        String sql = "TRUNCATE TABLE well_versioned";

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to truncate well_versioned", e);
        }
    }

    public void insertVersion(WellVersioned well) {
        String sql = """
                INSERT INTO well_versioned
                    (id, number, status, depth, diameter, field_id, version)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, well.getId());
            statement.setString(2, well.getNumber());
            statement.setString(3, well.getStatus());
            statement.setObject(4, well.getDepth());
            statement.setObject(5, well.getDiameter());
            statement.setObject(6, well.getFieldId());
            statement.setLong(7, well.getVersion());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to insert version into well_versioned", e);
        }
    }

    public List<WellVersioned> getAllVersions() {
        String sql = """
                SELECT id,
                       number,
                       status,
                       depth,
                       diameter,
                       field_id,
                       version
                  FROM well_versioned
              ORDER BY id, version
                """;

        return executeQuery(sql);
    }

    public List<WellVersioned> getLatestVersions() {
        String sql = """
                SELECT id,
                       number,
                       status,
                       depth,
                       diameter,
                       field_id,
                       version
                  FROM well_versioned
              ORDER BY id ASC, version DESC
                 LIMIT 1 BY id
                """;

        return executeQuery(sql);
    }

    private List<WellVersioned> executeQuery(String sql) {
        List<WellVersioned> result = new ArrayList<>();

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute query for well_versioned", e);
        }
    }
}