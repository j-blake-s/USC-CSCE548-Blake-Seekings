package com.commerce.util;

import java.sql.*;


public class DatabaseUtil {
    private static final String url = "jdbc:mysql://localhost:3306/online_commerce";
    private static final String user = "root";
    private static final String password = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}