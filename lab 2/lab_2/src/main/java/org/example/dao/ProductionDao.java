package org.example.dao;

import org.example.domain.model.Production;
import org.example.infrastructure.clickhouse.ClickHouseConnectionManager;
import org.example.mapper.ProductionRowMapper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class ProductionDao extends AbstractClickHouseDao<Production> {

    public ProductionDao() {
        super(new ProductionRowMapper());
    }

    @Override
    protected String getTableName() {
        return "productions";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    public void create(Production production) {
        String sql = """
                INSERT INTO productions (id, well_id, production_date, oil, gas, water)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, production.getId());
            statement.setLong(2, production.getWellId());
            statement.setDate(3, Date.valueOf(production.getProductionDate()));
            statement.setDouble(4, production.getOil());
            statement.setObject(5, production.getGas());
            statement.setObject(6, production.getWater());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create production", e);
        }
    }

    @Override
    public void update(Production production) {
        String sql = """
            ALTER TABLE productions
            UPDATE oil = ?,
                   gas = ?,
                   water = ?
            WHERE id = ?
            SETTINGS mutations_sync = 1
            """;

        try (Connection connection = ClickHouseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, production.getOil());
            statement.setObject(2, production.getGas());
            statement.setObject(3, production.getWater());
            statement.setLong(4, production.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update production", e);
        }
    }
}