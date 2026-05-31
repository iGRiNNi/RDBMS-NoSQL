package org.example.mapper;

import org.example.domain.model.WellVersioned;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WellVersionedRowMapper implements RowMapper<WellVersioned> {

    @Override
    public WellVersioned mapRow(ResultSet rs) throws SQLException {
        return new WellVersioned(
                rs.getLong("id"),
                rs.getString("number"),
                rs.getString("status"),
                getNullableDouble(rs, "depth"),
                getNullableDouble(rs, "diameter"),
                getNullableLong(rs, "field_id"),
                rs.getLong("version")
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