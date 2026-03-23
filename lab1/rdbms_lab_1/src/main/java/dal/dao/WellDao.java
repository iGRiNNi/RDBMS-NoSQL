package dal.dao;

import dal.connection.ConnectionManager;
import dal.dao.abstracts.AbstractDao;
import dal.dao.interfaces.Dao;
import dal.dao.mapper.WellRowMapper;
import model.Field;
import model.Well;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, well.getNumber());
            statement.setString(2, well.getStatus());
            statement.setObject(3, well.getDepth());
            statement.setObject(4, well.getDiameter());
            statement.setLong(5, well.getClusterId());

            statement.executeUpdate();
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

            statement.executeUpdate();
        }
    }
}
