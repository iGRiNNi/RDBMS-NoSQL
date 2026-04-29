package dal.dao;

import dal.infrastructure.connection.ConnectionManager;
import dal.dao.abstracts.AbstractDao;
import dal.dao.mapper.ProductionRowMapper;
import model.Production;

import java.sql.*;

public class ProductionDao extends AbstractDao<Production, Long> {

    public ProductionDao() {
        super(new ProductionRowMapper());
    }

    @Override
    protected String getTableName() {
        return "production";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    public void insert(Production production) throws SQLException {
        if (production == null) {
            throw new IllegalArgumentException("Production cannot be null");
        }

        if (production.getDate() != null) {
            insertWithDate(production);
        } else {
            insertWithoutDate(production);
        }
    }

    private void insertWithDate(Production production) throws SQLException {
        String sql = """
            INSERT INTO production (well_id, date, oil, gas, water)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
            """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, production.getWellId());
            statement.setDate(2, Date.valueOf(production.getDate()));
            statement.setDouble(3, production.getOil());
            statement.setObject(4, production.getGas());
            statement.setObject(5, production.getWater());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    production.setId(rs.getLong("id"));
                }
            }
        }
    }

    private void insertWithoutDate(Production production) throws SQLException {
        String sql = """
            INSERT INTO production (well_id, oil, gas, water)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, production.getWellId());
            statement.setDouble(2, production.getOil());
            statement.setObject(3, production.getGas());
            statement.setObject(4, production.getWater());

            statement.executeUpdate();
        }
    }

    @Override
    public void update(Production production) throws SQLException {
        if (production == null) {
            throw new IllegalArgumentException("Production cannot be null");
        }

        if (production.getDate() == null) {
            throw new IllegalArgumentException("Production date cannot be null for update");
        }

        String sql = """
            UPDATE production
            SET well_id = ?, date = ?, oil = ?, gas = ?, water = ?
            WHERE id = ?
            """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, production.getWellId());
            statement.setDate(2, Date.valueOf(production.getDate()));
            statement.setDouble(3, production.getOil());
            statement.setObject(4, production.getGas());
            statement.setObject(5, production.getWater());
            statement.setLong(6, production.getId());

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new SQLException("Cluster with id " + production.getId() + " was not found");
            }
        }
    }
}
