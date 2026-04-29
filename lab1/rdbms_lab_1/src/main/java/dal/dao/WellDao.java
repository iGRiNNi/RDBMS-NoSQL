package dal.dao;

import dal.dao.mapper.RowMapper;
import dal.dao.mapper.WellAverageComparisonDtoRowMapper;
import dal.dao.mapper.WellWaterOilDtoRowMapper;
import dal.infrastructure.connection.ConnectionManager;
import dal.dao.abstracts.AbstractDao;
import dal.dao.mapper.WellRowMapper;
import dto.WellAverageComparisonDto;
import dto.WellWaterOilDto;
import model.Well;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WellDao extends AbstractDao<Well, Long> {

    public WellDao() {
        super(new WellRowMapper());
    }

    @Override
    protected String getTableName() {
        return "well";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    public void insert(Well well) throws SQLException {
        String sql = """
                INSERT INTO well (number, status, depth, diameter, cluster_id)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, well.getNumber());
            statement.setString(2, well.getStatus());
            statement.setObject(3, well.getDepth());
            statement.setObject(4, well.getDiameter());
            statement.setLong(5, well.getClusterId());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    well.setId(rs.getLong("id"));
                }
            }
        }
    }

    @Override
    public void update(Well well) throws SQLException {
        String sql = """
                UPDATE well
                SET number = ?, status = ?, depth = ?, diameter = ?, cluster_id = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, well.getNumber());
            statement.setString(2, well.getStatus());
            statement.setObject(3, well.getDepth());
            statement.setObject(4, well.getDiameter());
            statement.setLong(5, well.getClusterId());
            statement.setLong(6, well.getId());

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new SQLException("Well with id " + well.getId() + " was not found");
            }
        }
    }

    public List<WellWaterOilDto> getTopWateredActiveWellsLastMonth() {
        String sql = """
            SELECT c.name AS group_name
                 , w.number AS well_number
                 , AVG(p.oil) AS avg_oil
                 , AVG(p.water) AS avg_water
              FROM well w
              JOIN cluster c ON c.id = w.cluster_id
              JOIN production p ON p.well_id = w.id
             WHERE w.status = 'active'
               AND p.date >= CURRENT_DATE - INTERVAL '1 month'
               AND p.date <= CURRENT_DATE
          GROUP BY c.name, w.number
            HAVING AVG(p.water) > AVG(p.oil)
          ORDER BY avg_water DESC
             LIMIT 3;
          """;

        List<WellWaterOilDto> result = new ArrayList<>();
        RowMapper<WellWaterOilDto> rowMapper = new WellWaterOilDtoRowMapper();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute watered wells query", e);
        }

        return result;
    }

    public List<WellAverageComparisonDto> compareWellAverageOilWithGroupAverage() {
        String sql = """
            WITH well_avg AS (
                SELECT w.cluster_id
                     , c.name AS group_name
                     , w.number AS well_number
                     , AVG(p.oil) AS avg_oil_by_well
                  FROM well w
                  JOIN cluster c ON c.id = w.cluster_id
                  JOIN production p ON p.well_id = w.id
              GROUP BY w.cluster_id, c.name, w.number
            )
              SELECT group_name
                   , well_number
                   , avg_oil_by_well
                   , AVG(avg_oil_by_well) OVER (PARTITION BY cluster_id) AS avg_oil_in_group
                   , CASE
                       WHEN avg_oil_by_well > AVG(avg_oil_by_well) OVER (PARTITION BY cluster_id)
                         THEN 'Выше среднего'
                       WHEN avg_oil_by_well = AVG(avg_oil_by_well) OVER (PARTITION BY cluster_id)
                         THEN 'Равно среднему'
                       ELSE 'Ниже среднего'
                     END AS above_group_avg
                FROM well_avg
            ORDER BY group_name, well_number;
            """;

        List<WellAverageComparisonDto> result = new ArrayList<>();
        RowMapper<WellAverageComparisonDto> rowMapper = new WellAverageComparisonDtoRowMapper();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute well average comparison query", e);
        }

        return result;
    }
}
