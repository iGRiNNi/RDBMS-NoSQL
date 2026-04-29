package org.example.dao.abstracts;

import java.util.List;

public interface CrudDao<T, ID> {

    void create(T entity);

    T getById(ID id);

    List<T> getAll();

    void update(T entity);

    void delete(ID id);
}
