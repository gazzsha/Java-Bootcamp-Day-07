package edu.school21.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private final HikariConfig config = new HikariConfig("/hikari.properties");
    private final javax.sql.DataSource dataSource = new HikariDataSource(config);

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public javax.sql.DataSource getDataSource() {
        return dataSource;
    }
}

