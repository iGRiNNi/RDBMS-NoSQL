package org.example.mapper;

import org.example.domain.model.ProductionCollapsing;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductionCollapsingRowMapper implements RowMapper<ProductionCollapsing> {

    @Override
    public ProductionCollapsing mapRow(ResultSet rs) throws SQLException {
        return new ProductionCollapsing(
                rs.getLong("id"),
                rs.getLong("well_id"),
                rs.getDouble("oil"),
                rs.getDouble("gas"),
                rs.getDouble("water"),
                rs.getInt("sign")
        );
    }
}