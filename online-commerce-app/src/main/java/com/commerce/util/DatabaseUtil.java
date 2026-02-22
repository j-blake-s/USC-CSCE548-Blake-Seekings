package com.commerce.util;

import java.sql.*;


public class DatabaseUtil {
    // private static final String url = "jdbc:mysql://localhost:3306/online_commerce";
    // private static final String user = "root";
    // private static final String password = "password";

    public static Connection getConnection() throws SQLException {
        // Railway provides the full URL in this variable
        String dbUrl = System.getenv("DATABASE_URL");
        
        // If we are running locally and the variable is missing, use your local setup
        if (dbUrl == null) {
            String url = "jdbc:mysql://localhost:3306/online_commerce?allowPublicKeyRetrieval=true&useSSL=false";
            String user = "root";
            String password = "password";
            return DriverManager.getConnection(url, user, password);
        }
        
        // In the cloud, the DATABASE_URL usually includes user/pass
        return DriverManager.getConnection("jdbc:" + dbUrl);
    }
}