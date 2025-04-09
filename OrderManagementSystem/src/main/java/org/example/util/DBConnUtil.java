package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnUtil {

    public static Connection getConnection(String propertyFilePath) {
        Connection conn = null;
        try {
            String connectionUrl = DBPropertyUtil.getConnectionString(propertyFilePath);
            conn = DriverManager.getConnection(connectionUrl);
        } catch (Exception e) {
            System.out.println("Error connecting to DB: " + e.getMessage());
        }
        return conn;
    }
}
