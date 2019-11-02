package org.github.raylemon.tuto.dao.dao.h2;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.esotericsoftware.minlog.Log.*;

class ConnectionSingleton {
    private static Connection connection;
    private static JdbcDataSource dataSource = new JdbcDataSource();

    static Connection getConnection() {
        if (connection == null) {
            dataSource.setUrl("jdbc:h2:file:./data"); //db name is "data" located in app folder
            dataSource.setUser("SA"); // by default
            dataSource.setPassword(""); //by default
        }
        try {
            connection = dataSource.getConnection();
            ResultSet rs = connection.getMetaData().getTables(null, null, "SOCIETIES", null);
            if (!rs.next()) {
                info("No DB found! Creating database…");
                createDB();
            }
        } catch (SQLException e) {
            error("No connection", e);
        }
        debug("Connecting to db");
        return connection;
    }

    private static void createDB() {
        try (Statement statement = connection.createStatement()) {
            String sqlTableSocieties = "create table if not exists SOCIETIES (" +
                    "SOC_ID identity primary key," +
                    "NAME varchar(100)" +
                    ");";

            String sqlTableEmployees = "create table if not exists EMPLOYEES (" +
                    "EMP_ID identity primary key," +
                    "FIRSTNAME varchar(50)," +
                    "LASTNAME varchar(50)," +
                    "BIRTHDAY date," +
                    "FK_SOCIETY bigint," +
                    "foreign key (FK_SOCIETY) references SOCIETIES (SOC_ID)" +
                    ");";

            String sqlTableLanguages = "create table if not exists LANGUAGES (" +
                    "LANG_ID identity primary key," +
                    "NAME varchar(30)" +
                    ");";

            String sqlJunctionTable = "create table if not exists J_EMP_LANG (" +
                    "JEL_ID identity primary key," +
                    "FK_EMPLOYEE bigint," +
                    "FK_LANGUAGE bigint," +
                    "foreign key (FK_EMPLOYEE) references EMPLOYEES (EMP_ID) on delete cascade," +
                    "foreign key (FK_LANGUAGE) references LANGUAGES (LANG_ID)" +
                    ");";

            statement.addBatch(sqlTableLanguages);
            statement.addBatch(sqlTableSocieties);
            statement.addBatch(sqlTableEmployees);
            statement.addBatch(sqlJunctionTable);

            statement.executeBatch();

        } catch (SQLException e) {
            error("error when creating database", e);
        }
        info("Database created");
    }
}
