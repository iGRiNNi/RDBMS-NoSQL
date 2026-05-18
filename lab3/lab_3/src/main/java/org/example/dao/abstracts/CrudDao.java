package org.example.dao.abstracts;

import java.util.List;

public interface CrudDao<T, ID> {

    void create(T entity);

    T getById(ID id);

    void update(T entity);

    void delete(ID id);
}
