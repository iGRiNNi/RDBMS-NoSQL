package dal.dao.mapper;


import dto.WellWaterOilDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WellWaterOilDtoRowMapper implements RowMapper<WellWaterOilDto> {

    @Override
    public WellWaterOilDto mapRow(ResultSet rs) throws SQLException {
        return new WellWaterOilDto(
                rs.getString("group_name"),
                rs.getString("well_number"),
                rs.getDouble("avg_oil"),
                rs.getDouble("avg_water")
        );
    }
}
