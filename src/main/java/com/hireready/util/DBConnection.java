package com.hireready.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class to manage MySQL database connections via JDBC.
 * Reads configuration from db.properties with safe fallback values.
 */
public class DBConnection {

    // Default configuration (can be overridden via src/main/resources/db.properties)
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/hireready_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static String username = "root";
    private static String password = "root";

    static {
        loadProperties();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("CRITICAL: MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void loadProperties() {
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                if (props.getProperty("db.driver") != null) {
                    driver = props.getProperty("db.driver").trim();
                }
                if (props.getProperty("db.url") != null) {
                    url = props.getProperty("db.url").trim();
                }
                if (props.getProperty("db.username") != null) {
                    username = props.getProperty("db.username").trim();
                }
                if (props.getProperty("db.password") != null) {
                    password = props.getProperty("db.password").trim();
                }
            } else {
                System.out.println("db.properties not found in classpath. Using default MySQL configuration.");
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not read db.properties, using fallback defaults: " + e.getMessage());
        }
    }

    /**
     * Obtains a new database connection.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Closes AutoCloseable resources silently without throwing checked exceptions.
     */
    public static void close(AutoCloseable... resources) {
        for (AutoCloseable res : resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * Tests if the database connection can be established.
     * @return true if successful, false otherwise.
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static void main(String[] args) {
        System.out.println("Testing MySQL connection to " + url + " with user: " + username + "...");
        if (testConnection()) {
            System.out.println("SUCCESS: Database connection established successfully!");
        } else {
            System.out.println("FAILED: Could not connect to MySQL. Check db.properties credentials.");
        }
    }
}
