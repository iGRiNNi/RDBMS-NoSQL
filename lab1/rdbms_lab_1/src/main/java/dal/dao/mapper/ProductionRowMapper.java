package dal.dao.mapper;

import model.Production;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductionRowMapper implements RowMapper<Production> {

    @Override
    public Production mapRow(ResultSet rs) throws SQLException {
        return new Production(
                rs.getLong("id"),
                rs.getLong("well_id"),
                ResultSetUtils.getNullableLocalDate(rs, "date"),
                rs.getDouble("oil"),
                ResultSetUtils.getNullableDouble(rs, "gas"),
                ResultSetUtils.getNullableDouble(rs, "water")
        );
    }
}
