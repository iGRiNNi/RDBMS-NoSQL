package org.example.dao;

import org.example.dto.ProductionAnalyticsDto;
import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductionAnalyticsDao {

    public List<ProductionAnalyticsDto> findTopActiveWellsByOilProduction() {
        String sql = """
                SELECT f.id AS field_id,
                       f.name AS field_name,
                       w.id AS well_id,
                       w.number AS well_number,
                       count() AS measurements_count,
                       avg(p.oil) AS avg_oil,
                       avg(p.water) AS avg_water,
                       sum(p.oil) AS total_oil
                  FROM productions p
                  JOIN wells w ON w.id = p.well_id
                  JOIN fields f ON f.id = w.field_id
                 WHERE w.status = 'active'
                   AND p.production_date >= '2026-03-01'
                   AND p.production_date <= '2026-03-31'
              GROUP BY f.id,
                       f.name,
                       w.id,
                       w.number
                HAVING avg_oil > 50
              ORDER BY total_oil DESC
                 LIMIT 2 BY field_id
                 LIMIT 10
                """;

        List<ProductionAnalyticsDto> result = new ArrayList<>();

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute production analytics query", e);
        }
    }

    private ProductionAnalyticsDto mapRow(ResultSet rs) throws Exception {
        return new ProductionAnalyticsDto(
                rs.getLong("field_id"),
                rs.getString("field_name"),
                rs.getLong("well_id"),
                rs.getString("well_number"),
                rs.getLong("measurements_count"),
                rs.getDouble("avg_oil"),
                rs.getDouble("avg_water"),
                rs.getDouble("total_oil")
        );
    }
}