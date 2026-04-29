package dal.dao.abstracts;

import dal.dao.interfaces.Dao;
import dal.dao.mapper.RowMapper;
import dal.infrastructure.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T, ID> implements Dao<T, ID> {

    private final RowMapper<T> rowMapper;

    protected AbstractDao(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    protected abstract String getTableName();

    protected abstract String getIdColumnName();

    @Override
    public T getById(ID id) throws SQLException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rowMapper.mapRow(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<T> getAll() throws SQLException {
        String sql = "SELECT * FROM " + getTableName();

        List<T> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                result.add(rowMapper.mapRow(rs));
            }
        }

        return result;
    }

    @Override
    public void delete(ID id) throws SQLException {
        String sql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, id);
            int deletedRows = statement.executeUpdate();

            if (deletedRows == 0) {
                throw new SQLException("Entity with id " + id + " was not found in table " + getTableName());
            }
        }
    }
}
