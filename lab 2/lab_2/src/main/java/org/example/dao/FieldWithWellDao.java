package org.example.dao;

import org.example.dto.FieldWithWellDto;
import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FieldWithWellDao {

    public List<FieldWithWellDto> innerJoin() {
        String sql = """
                SELECT f.id AS field_id,
                       f.name AS field_name,
                       w.id AS well_id,
                       w.number AS well_number,
                       w.status AS well_status
                  FROM fields f
            INNER JOIN wells w ON w.field_id = f.id
              ORDER BY f.id, w.id
                SETTINGS join_use_nulls = 1
                """;

        return executeQuery(sql);
    }

    public List<FieldWithWellDto> leftJoin() {
        String sql = """
                SELECT f.id AS field_id,
                       f.name AS field_name,
                       w.id AS well_id,
                       w.number AS well_number,
                       w.status AS well_status
                  FROM fields f
             LEFT JOIN wells w ON w.field_id = f.id
              ORDER BY f.id, w.id
                SETTINGS join_use_nulls = 1
                """;

        return executeQuery(sql);
    }

    public List<FieldWithWellDto> rightJoin() {
        String sql = """
                SELECT f.id AS field_id,
                       f.name AS field_name,
                       w.id AS well_id,
                       w.number AS well_number,
                       w.status AS well_status
                  FROM fields f
            RIGHT JOIN wells w ON w.field_id = f.id
              ORDER BY w.id, f.id
                SETTINGS join_use_nulls = 1
                """;

        return executeQuery(sql);
    }

    public List<FieldWithWellDto> fullJoin() {
        String sql = """
                SELECT f.id AS field_id,
                       f.name AS field_name,
                       w.id AS well_id,
                       w.number AS well_number,
                       w.status AS well_status
                  FROM fields f
             FULL JOIN wells w ON w.field_id = f.id
              ORDER BY field_id, well_id
                SETTINGS join_use_nulls = 1
                """;

        return executeQuery(sql);
    }

    public List<FieldWithWellDto> crossJoin() {
        String sql = """
                SELECT f.id AS field_id,
                       f.name AS field_name,
                       w.id AS well_id,
                       w.number AS well_number,
                       w.status AS well_status
                  FROM fields f
            CROSS JOIN wells w
                 WHERE w.field_id = f.id
                """;

        return executeQuery(sql);
    }

    private List<FieldWithWellDto> executeQuery(String sql) {
        List<FieldWithWellDto> result = new ArrayList<>();

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute field with well query", e);
        }
    }

    private FieldWithWellDto mapRow(ResultSet rs) throws Exception {
        return new FieldWithWellDto(
                getNullableLong(rs, "field_id"),
                rs.getString("field_name"),
                getNullableLong(rs, "well_id"),
                rs.getString("well_number"),
                rs.getString("well_status")
        );
    }

    private Long getNullableLong(ResultSet rs, String columnName) throws Exception {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }
}
