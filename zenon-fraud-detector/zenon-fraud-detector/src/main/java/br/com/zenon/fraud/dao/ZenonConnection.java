package br.com.zenon.fraud.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ZenonConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/zenon?rewriteBatchedStatements=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private ZenonConnection() {

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
