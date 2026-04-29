package dal.dao.mapper;

import dto.ClusterHierarchyDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClusterHierarchyDtoRowMapper implements RowMapper<ClusterHierarchyDto> {

    @Override
    public ClusterHierarchyDto mapRow(ResultSet rs) throws SQLException {
        return new ClusterHierarchyDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("level")
        );
    }
}
