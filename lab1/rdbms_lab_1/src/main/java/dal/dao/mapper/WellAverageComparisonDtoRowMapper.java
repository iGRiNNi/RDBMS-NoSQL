package dal.dao.mapper;

import dto.WellAverageComparisonDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WellAverageComparisonDtoRowMapper implements RowMapper<WellAverageComparisonDto> {

    @Override
    public WellAverageComparisonDto mapRow(ResultSet rs) throws SQLException {
        return new WellAverageComparisonDto(
                rs.getString("group_name"),
                rs.getString("well_number"),
                rs.getDouble("avg_oil_by_well"),
                rs.getDouble("avg_oil_in_group"),
                rs.getString("above_group_avg")
        );
    }
}
