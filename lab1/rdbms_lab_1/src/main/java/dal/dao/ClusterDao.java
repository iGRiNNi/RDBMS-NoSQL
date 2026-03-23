package dal.dao;

import dal.connection.ConnectionManager;
import dal.dao.abstracts.AbstractDao;
import dal.dao.mapper.ClusterRowMapper;
import model.Cluster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClusterDao extends AbstractDao<Cluster, Long> {

    public ClusterDao() {
        super(new ClusterRowMapper());
    }

    @Override
    protected String getTableName() {
        return "cluster";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    public void insert(Cluster cluster) throws SQLException {
        String sql = """
                INSERT INTO cluster (name, field_id, parent_cluster_id)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cluster.getName());
            statement.setLong(2, cluster.getFieldId());
            statement.setObject(3, cluster.getParentClusterId());

            statement.executeUpdate();
        }
    }

    @Override
    public void update(Cluster cluster) throws SQLException {
        String sql = """
                UPDATE cluster
                SET name = ?, field_id = ?, parent_cluster_id = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cluster.getName());
            statement.setLong(2, cluster.getFieldId());
            statement.setObject(3, cluster.getParentClusterId());
            statement.setLong(4, cluster.getId());

            statement.executeUpdate();
        }
    }
}
