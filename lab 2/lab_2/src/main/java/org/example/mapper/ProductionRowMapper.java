package org.example.mapper;

import org.example.domain.model.Production;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductionRowMapper implements RowMapper<Production> {

    @Override
    public Production mapRow(ResultSet rs) throws SQLException {
        Date productionDate = rs.getDate("production_date");

        return new Production(
                rs.getLong("id"),
                rs.getLong("well_id"),
                productionDate.toLocalDate(),
                rs.getDouble("oil"),
                getNullableDouble(rs, "gas"),
                getNullableDouble(rs, "water")
        );
    }

    private Double getNullableDouble(ResultSet rs, String columnName) throws SQLException {
        double value = rs.getDouble(columnName);
        return rs.wasNull() ? null : value;
    }
}