package com.example.dao;

import java.util.List;

public interface CrudDao<T, ID> {

    ID create(T entity);

    T getById(ID id);

    List<T> getAll();

    void update(T entity);

    void delete(ID id);
}
