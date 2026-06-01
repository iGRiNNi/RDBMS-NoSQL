package com.example.dao;

import java.util.Optional;

public interface CrudDao<T, ID> {

    void create(T entity);

    Optional<T> getById(ID id);

    void update(T entity);

    void delete(ID id);
}
