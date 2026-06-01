package com.example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.example.domain.key.ProductionKey;
import com.example.domain.model.Production;
import com.example.infrastructure.cassandra.CassandraSessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductionDao implements CrudDao<Production, ProductionKey> {

    private final CqlSession session;

    private final PreparedStatement insertStatement;
    private final PreparedStatement selectByIdStatement;
    private final PreparedStatement selectByWellIdStatement;
    private final PreparedStatement updateStatement;
    private final PreparedStatement deleteStatement;

    public ProductionDao() {
        this.session = CassandraSessionManager.getSession();

        this.insertStatement = session.prepare("""
                INSERT INTO productions_by_well (
                    well_id,
                    production_date,
                    id,
                    oil,
                    gas,
                    water
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """);

        this.selectByIdStatement = session.prepare("""
                SELECT well_id, production_date, id, oil, gas, water
                FROM productions_by_well
                WHERE well_id = ?
                  AND production_date = ?
                  AND id = ?
                """);

        this.selectByWellIdStatement = session.prepare("""
                SELECT well_id, production_date, id, oil, gas, water
                FROM productions_by_well
                WHERE well_id = ?
                """);

        this.updateStatement = session.prepare("""
                UPDATE productions_by_well
                SET oil = ?,
                    gas = ?,
                    water = ?
                WHERE well_id = ?
                  AND production_date = ?
                  AND id = ?
                """);

        this.deleteStatement = session.prepare("""
                DELETE FROM productions_by_well
                WHERE well_id = ?
                  AND production_date = ?
                  AND id = ?
                """);
    }

    @Override
    public void create(Production production) {
        try {
            session.execute(insertStatement.bind(
                    production.getWellId(),
                    production.getProductionDate(),
                    production.getId(),
                    production.getOil(),
                    production.getGas(),
                    production.getWater()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create production", e);
        }
    }

    @Override
    public Optional<Production> getById(ProductionKey key) {
        try {
            Row row = session.execute(selectByIdStatement.bind(
                    key.getWellId(),
                    key.getProductionDate(),
                    key.getId()
            )).one();

            if (row == null) {
                return Optional.empty();
            }

            return Optional.of(mapRow(row));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get production by key", e);
        }
    }

    public List<Production> findByWellId(UUID wellId) {
        try {
            List<Production> result = new ArrayList<>();
            ResultSet resultSet = session.execute(selectByWellIdStatement.bind(wellId));

            for (Row row : resultSet) {
                result.add(mapRow(row));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get productions by well id", e);
        }
    }

    public List<Production> findByWellIdAndDateBetween(UUID wellId, LocalDate from, LocalDate to) {
        String cql = """
                SELECT well_id, production_date, id, oil, gas, water
                FROM productions_by_well
                WHERE well_id = ?
                  AND production_date >= ?
                  AND production_date <= ?
                """;

        try {
            PreparedStatement statement = session.prepare(cql);
            List<Production> result = new ArrayList<>();

            ResultSet resultSet = session.execute(statement.bind(wellId, from, to));

            for (Row row : resultSet) {
                result.add(mapRow(row));
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get productions by date range", e);
        }
    }

    @Override
    public void update(Production production) {
        try {
            session.execute(updateStatement.bind(
                    production.getOil(),
                    production.getGas(),
                    production.getWater(),
                    production.getWellId(),
                    production.getProductionDate(),
                    production.getId()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to update production", e);
        }
    }

    @Override
    public void delete(ProductionKey key) {
        try {
            session.execute(deleteStatement.bind(
                    key.getWellId(),
                    key.getProductionDate(),
                    key.getId()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete production", e);
        }
    }

    private Production mapRow(Row row) {
        return new Production(
                row.getUuid("well_id"),
                row.getLocalDate("production_date"),
                row.getUuid("id"),
                row.getDouble("oil"),
                row.getDouble("gas"),
                row.getDouble("water")
        );
    }
}