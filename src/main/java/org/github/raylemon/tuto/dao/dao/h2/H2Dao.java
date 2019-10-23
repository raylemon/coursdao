package org.github.raylemon.tuto.dao.dao.h2;

import org.github.raylemon.tuto.dao.dao.DAO;

import java.sql.Connection;

public abstract class H2Dao<T> implements DAO<T> {
    Connection connection = ConnectionSingleton.getConnection();
}
