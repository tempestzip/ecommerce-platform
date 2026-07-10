package com.software_engineers.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

 // Singleton that manages a single JDBC connection to the SQLite database file "ecommerce.db".

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:ecommerce.db";
    private static DatabaseConnection instance;
    private Connection connection;

    // Private constructor prevents external instantiation.
    private DatabaseConnection() {
        try {
            // Explicitly load the SQLite JDBC driver.
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);

            // SQLite ignores foreign key constraints by default; enable them per connection.
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + DB_URL, e);
        }
    }

    
     // Returns the single shared instance of DatabaseConnection, creating it if necessary.
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Returns the active JDBC connection, reconnecting first if it was closed.
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to re-establish database connection.", e);
        }
        return connection;
    }

    // Closes the underlying connection. Call this when the application shuts down.

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database connection.", e);
        }
    }
}