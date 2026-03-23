package dal.dao.mapper;

import model.Well;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WellRowMapper implements RowMapper<Well> {

    @Override
    public Well mapRow(ResultSet rs) throws SQLException {
        return new Well(
                rs.getLong("id"),
                rs.getString("number"),
                rs.getString("status"),
                ResultSetUtils.getNullableDouble(rs, "depth"),
                ResultSetUtils.getNullableDouble(rs, "diameter"),
                rs.getLong("cluster_id")
        );
    }
}
