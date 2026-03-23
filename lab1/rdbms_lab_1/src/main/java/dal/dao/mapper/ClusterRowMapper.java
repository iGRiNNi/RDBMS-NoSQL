package dal.dao.mapper;

import model.Cluster;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClusterRowMapper implements RowMapper<Cluster> {

    @Override
    public Cluster mapRow(ResultSet rs) throws SQLException {
        return new Cluster(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("field_id"),
                ResultSetUtils.getNullableLong(rs, "parent_cluster_id")
        );
    }
}
