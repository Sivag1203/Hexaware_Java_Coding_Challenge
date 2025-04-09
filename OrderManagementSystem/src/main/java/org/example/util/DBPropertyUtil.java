package org.example.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DBPropertyUtil {

    public static String getConnectionString(String fileName) {
        StringBuilder connStr = new StringBuilder();

        try (InputStream input = new FileInputStream(fileName)) {
            Properties prop = new Properties();
            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            connStr.append(url)
                    .append("?user=")
                    .append(user)
                    .append("&password=")
                    .append(password);

        } catch (Exception e) {
            System.out.println("Error reading properties file: " + e.getMessage());
        }

        return connStr.toString();
    }
}
