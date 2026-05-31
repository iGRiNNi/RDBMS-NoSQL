package org.example.dao;

import org.example.domain.model.ProductionCollapsing;
import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;
import org.example.mapper.ProductionCollapsingRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CollapsingMergeTreeDao implements CrudDao<ProductionCollapsing, Long> {

    private final ProductionCollapsingRowMapper rowMapper = new ProductionCollapsingRowMapper();

    @Override
    public void create(ProductionCollapsing production) {
        if (getById(production.getId()) != null) {
            throw new IllegalStateException("Active production with id " + production.getId() + " already exists");
        }

        insertRow(new ProductionCollapsing(
                production.getId(),
                production.getWellId(),
                production.getOil(),
                production.getGas(),
                production.getWater(),
                1
        ));
    }

    @Override
    public ProductionCollapsing getById(Long id) {
        String sql = """
                SELECT id,
                       sum(well_id * sign) AS well_id,
                       sum(oil * sign) AS oil,
                       sum(gas * sign) AS gas,
                       sum(water * sign) AS water,
                       sum(sign) AS sign_sum
                  FROM production_collapsing
                 WHERE id = ?
              GROUP BY id
                HAVING sign_sum > 0
                 LIMIT 1
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapActualRow(rs);
                }
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get actual production_collapsing by id", e);
        }
    }

    @Override
    public List<ProductionCollapsing> getAll() {
        String sql = """
                SELECT id,
                       sum(well_id * sign) AS well_id,
                       sum(oil * sign) AS oil,
                       sum(gas * sign) AS gas,
                       sum(water * sign) AS water,
                       sum(sign) AS sign_sum
                  FROM production_collapsing
              GROUP BY id
                HAVING sign_sum > 0
              ORDER BY id
                """;

        List<ProductionCollapsing> result = new ArrayList<>();

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                result.add(mapActualRow(rs));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get actual production_collapsing rows", e);
        }
    }

    @Override
    public void update(ProductionCollapsing production) {
        ProductionCollapsing current = getById(production.getId());

        if (current == null) {
            throw new IllegalStateException("Active production with id " + production.getId() + " not found");
        }

        insertNegativeFor(current);

        insertRow(new ProductionCollapsing(
                production.getId(),
                production.getWellId(),
                production.getOil(),
                production.getGas(),
                production.getWater(),
                1
        ));
    }

    @Override
    public void delete(Long id) {
        ProductionCollapsing current = getById(id);

        if (current == null) {
            throw new IllegalStateException("Active production with id " + id + " not found");
        }

        insertNegativeFor(current);
    }

    public void truncate() {
        String sql = "TRUNCATE TABLE production_collapsing";

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to truncate production_collapsing", e);
        }
    }

    public List<ProductionCollapsing> getAllRows() {
        String sql = """
                SELECT id,
                       well_id,
                       oil,
                       gas,
                       water,
                       sign
                  FROM production_collapsing
              ORDER BY id, sign DESC
                """;

        return executePhysicalRowsQuery(sql);
    }

    private void insertNegativeFor(ProductionCollapsing current) {
        insertRow(new ProductionCollapsing(
                current.getId(),
                current.getWellId(),
                current.getOil(),
                current.getGas(),
                current.getWater(),
                -1
        ));
    }

    private void insertRow(ProductionCollapsing production) {
        String sql = """
                INSERT INTO production_collapsing
                    (id, well_id, oil, gas, water, sign)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, production.getId());
            statement.setLong(2, production.getWellId());
            statement.setDouble(3, production.getOil());
            statement.setDouble(4, production.getGas());
            statement.setDouble(5, production.getWater());
            statement.setInt(6, production.getSign());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to insert row into production_collapsing", e);
        }
    }

    private List<ProductionCollapsing> executePhysicalRowsQuery(String sql) {
        List<ProductionCollapsing> result = new ArrayList<>();

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute query for physical production_collapsing rows", e);
        }
    }

    private ProductionCollapsing mapActualRow(ResultSet rs) throws Exception {
        return new ProductionCollapsing(
                rs.getLong("id"),
                rs.getLong("well_id"),
                rs.getDouble("oil"),
                rs.getDouble("gas"),
                rs.getDouble("water"),
                rs.getInt("sign_sum")
        );
    }
}