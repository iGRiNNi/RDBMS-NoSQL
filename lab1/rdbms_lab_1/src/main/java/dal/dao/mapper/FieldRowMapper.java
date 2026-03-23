package dal.dao.mapper;

import model.Field;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldRowMapper implements RowMapper<Field> {

    @Override
    public Field mapRow(ResultSet rs) throws SQLException {
        return new Field(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("region"),
                ResultSetUtils.getNullableDouble(rs, "latitude"),
                ResultSetUtils.getNullableDouble(rs, "longitude"),
                rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                ResultSetUtils.getNullableDouble(rs, "oil_reserves")
        );
    }
}
