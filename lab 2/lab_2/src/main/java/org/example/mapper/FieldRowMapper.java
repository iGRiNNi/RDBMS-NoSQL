package org.example.mapper;

import org.example.domain.model.Field;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldRowMapper implements RowMapper<Field> {

    @Override
    public Field mapRow(ResultSet rs) throws SQLException {
        return new Field(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("region"),
                getNullableDouble(rs, "latitude"),
                getNullableDouble(rs, "longitude")
        );
    }

    private Double getNullableDouble(ResultSet rs, String columnName) throws SQLException {
        double value = rs.getDouble(columnName);
        return rs.wasNull() ? null : value;
    }
}