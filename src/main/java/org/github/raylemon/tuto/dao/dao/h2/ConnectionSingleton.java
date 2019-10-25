package org.github.raylemon.tuto.dao.dao.h2;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static com.esotericsoftware.minlog.Log.debug;
import static com.esotericsoftware.minlog.Log.error;

public class ConnectionSingleton {
    private static Connection connection;
    private static JdbcDataSource dataSource = new JdbcDataSource();

    public static Connection getConnection() {
        if (connection == null) {
            dataSource.setUrl("jdbc:h2:file:./data"); //db name is "data" located in app folder
            dataSource.setUser("SA"); // by default
            dataSource.setPassword(""); //by default
        }
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            error("No connection", e);
        }
        debug("Connecting to db");
        return connection;
    }
}
