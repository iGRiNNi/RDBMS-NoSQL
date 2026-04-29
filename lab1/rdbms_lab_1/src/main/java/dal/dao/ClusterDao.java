package dal.dao;

import dal.dao.mapper.ClusterHierarchyDtoRowMapper;
import dal.dao.mapper.RowMapper;
import dal.infrastructure.connection.ConnectionManager;
import dal.dao.abstracts.AbstractDao;
import dal.dao.mapper.ClusterRowMapper;
import dto.ClusterHierarchyDto;
import model.Cluster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                RETURNING id
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cluster.getName());
            statement.setLong(2, cluster.getFieldId());
            statement.setObject(3, cluster.getParentClusterId());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    cluster.setId(rs.getLong("id"));
                }
            }
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

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new SQLException("Cluster with id " + cluster.getId() + " was not found");
            }
        }
    }

    public List<ClusterHierarchyDto> getClusterHierarchyByFieldId(long fieldId) {
        String sql = """
            WITH RECURSIVE rec(id, name, level) AS (
                SELECT id, name, 1 as level
                  FROM cluster
                 WHERE field_id = ? AND parent_cluster_id IS NULL
                 UNION ALL
                SELECT c.id, c.name, rec.level + 1 as level
                  FROM cluster c
                  JOIN rec ON rec.id = c.parent_cluster_id
            )
            SELECT * FROM rec;
            """;

        List<ClusterHierarchyDto> result = new ArrayList<>();
        RowMapper<ClusterHierarchyDto> rowMapper = new ClusterHierarchyDtoRowMapper();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, fieldId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(rowMapper.mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute cluster hierarchy query", e);
        }

        return result;
    }
}
