package br.com.zenon.fraud.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ZenonConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/zenon";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public Connection zenonConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
