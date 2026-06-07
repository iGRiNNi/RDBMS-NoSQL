package org.example.dao;

import java.util.Optional;

public interface CrudDao<T, ID> {

    void create(T document);

    Optional<T> getById(ID id);

    void update(T document);

    void delete(ID id);
}
