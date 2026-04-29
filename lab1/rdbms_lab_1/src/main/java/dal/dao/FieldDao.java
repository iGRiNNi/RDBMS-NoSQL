package dal.dao;

import dal.dao.mapper.FieldContractorDtoRowMapper;
import dal.dao.mapper.RowMapper;
import dal.infrastructure.connection.ConnectionManager;
import dal.dao.abstracts.AbstractDao;
import dal.dao.mapper.FieldRowMapper;
import model.Field;
import dto.FieldContractorDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FieldDao extends AbstractDao<Field, Long> {

    private final RowMapper<FieldContractorDto> fieldContractorMapper =
            new FieldContractorDtoRowMapper();

    public FieldDao() {
        super(new FieldRowMapper());
    }

    @Override
    protected String getTableName() {
        return "field";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    public void insert(Field field) throws SQLException {
        String sql = """
            INSERT INTO field (name, region, latitude, longitude, start_date, oil_reserves)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id
            """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, field.getName());
            statement.setString(2, field.getRegion());
            statement.setObject(3, field.getLatitude());
            statement.setObject(4, field.getLongitude());
            statement.setObject(5, field.getStartDate() != null ? Date.valueOf(field.getStartDate()) : null);
            statement.setObject(6, field.getOilReserves());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    field.setId(rs.getLong("id"));
                }
            }
        }
    }

    @Override
    public void update(Field field) throws SQLException {
        String sql = """
                UPDATE field
                SET name = ?, region = ?, latitude = ?, longitude = ?, start_date = ?, oil_reserves = ?
                WHERE id = ?
                """;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, field.getName());
            statement.setString(2, field.getRegion());
            statement.setObject(3, field.getLatitude());
            statement.setObject(4, field.getLongitude());
            statement.setObject(5, field.getStartDate() != null ? Date.valueOf(field.getStartDate()) : null);
            statement.setObject(6, field.getOilReserves());
            statement.setLong(7, field.getId());

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new SQLException("Field with id " + field.getId() + " was not found");
            }
        }
    }

    public List<FieldContractorDto> getFieldsWithContractorsInnerJoin() {
        String sql = """
                	 SELECT f.name AS "field_name"
                	      , f.region AS "region"
                		  , c.name AS "contractor_name"
                	   FROM field f
                 INNER JOIN contractor c ON c.field_id = f.id
                """;

        return executeFieldContractorQuery(sql);
    }

    public List<FieldContractorDto> getFieldsWithContractorsLeftJoin() {
        String sql = """
                SELECT f.name AS "field_name"
                     , f.region AS "region"
                     , c.name AS "contractor_name"
                  FROM field f
             LEFT JOIN contractor c ON c.field_id = f.id
             """;

        return executeFieldContractorQuery(sql);
    }

    public List<FieldContractorDto> getFieldsWithContractorsRightJoin() {
        String sql = """
                 SELECT f.name AS "field_name"
                      , f.region AS "region"
                	  , c.name AS "contractor_name"
                   FROM field f
             RIGHT JOIN contractor c ON c.field_id = f.id
             """;

        return executeFieldContractorQuery(sql);
    }

    public List<FieldContractorDto> getFieldsWithContractorsFullJoin() {
        String sql = """
                SELECT f.name AS "field_name"
                     , f.region AS "region"
                     , c.name AS "contractor_name"
                  FROM field f
             FULL JOIN contractor c ON c.field_id = f.id
             """;

        return executeFieldContractorQuery(sql);
    }

    public List<FieldContractorDto> getFieldsWithContractorsCrossJoin() {
        String sql = """
                 SELECT f.name AS "field_name"
                      , f.region AS "region"
                	  , c.name AS "contractor_name"
                   FROM field f
             CROSS JOIN contractor c
             """;

        return executeFieldContractorQuery(sql);
    }

    private List<FieldContractorDto> executeFieldContractorQuery(String sql) {
        List<FieldContractorDto> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                result.add(fieldContractorMapper.mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to execute field-contractor query", e);
        }

        return result;
    }
}