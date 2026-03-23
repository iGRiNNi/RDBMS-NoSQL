package dal.dao;

import dal.infrastructure.connection.ConnectionManager;
import dal.dao.abstracts.AbstractDao;
import dal.dao.mapper.FieldRowMapper;
import model.Field;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FieldDao extends AbstractDao<Field, Long> {

    public FieldDao() {
        super(new FieldRowMapper());
    }

    @Override
    protected String getTableName() {
        return "field";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    public void insert(Field field) throws SQLException {
        String sql = """
INSERT INTO field (name, region, latitude, longitude, start_date, oil_reserves)
//VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, field.getName());
            statement.setString(2, field.getRegion());
            statement.setObject(3, field.getLatitude());
            statement.setObject(4, field.getLongitude());
            statement.setObject(5, field.getStartDate() != null ? Date.valueOf(field.getStartDate()) : null);
            statement.setObject(6, field.getOilReserves());

            statement.executeUpdate();
        }
    }

    @Override
    public void update(Field field) throws SQLException {
        String sql = """
                UPDATE field
                SET name = ?, region = ?, latitude = ?, longitude = ?, start_date = ?, oil_reserves = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, field.getName());
            statement.setString(2, field.getRegion());
            statement.setObject(3, field.getLatitude());
            statement.setObject(4, field.getLongitude());
            statement.setObject(5, field.getStartDate() != null ? Date.valueOf(field.getStartDate()) : null);
            statement.setObject(6, field.getOilReserves());
            statement.setLong(7, field.getId());

            statement.executeUpdate();
        }
    }
}