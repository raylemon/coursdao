package org.github.raylemon.tuto.dao.dao.h2;

import java.util.Collection;

public interface CollectionDAO<T> {
    Collection<T> findAll();

    void saveAll(Collection<T> collection);
}
