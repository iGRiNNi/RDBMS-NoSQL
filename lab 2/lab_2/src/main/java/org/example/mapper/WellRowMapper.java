package org.example.mapper;

import org.example.domain.model.Well;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WellRowMapper implements RowMapper<Well> {

    @Override
    public Well mapRow(ResultSet rs) throws SQLException {
        return new Well(
                rs.getLong("id"),
                rs.getString("number"),
                rs.getString("status"),
                getNullableDouble(rs, "depth"),
                getNullableDouble(rs, "diameter"),
                getNullableLong(rs, "field_id")
        );
    }

    private Double getNullableDouble(ResultSet rs, String columnName) throws SQLException {
        double value = rs.getDouble(columnName);
        return rs.wasNull() ? null : value;
    }

    private Long getNullableLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }
}