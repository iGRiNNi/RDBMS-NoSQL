package dal.dao.mapper;

import dto.FieldContractorDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldContractorDtoRowMapper implements RowMapper<FieldContractorDto> {

    @Override
    public FieldContractorDto mapRow(ResultSet rs) throws SQLException {
        return new FieldContractorDto(
                rs.getString("field_name"),
                rs.getString("region"),
                rs.getString("contractor_name")
        );
    }
}
