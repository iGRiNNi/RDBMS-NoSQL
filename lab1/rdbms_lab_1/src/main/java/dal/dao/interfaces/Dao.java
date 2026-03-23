package dal.dao.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T, ID> {

    T getById(ID id) throws SQLException;

    List<T> getAll() throws SQLException;

    void insert(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(ID id) throws SQLException;
}
