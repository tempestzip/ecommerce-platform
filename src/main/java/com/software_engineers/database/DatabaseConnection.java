package com.software_engineers.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton that connects to our SQLite database (ecommerce.db).
 *
 * We made this a singleton so we're not opening a new connection every time we
 * need to talk to the database. SQLite also doesn't handle multiple connections
 * writing at once very well, so keeping it to one shared connection made sense.
 *
 * @author Marcus
 * @since 2026-07-09
 * @version 1.0
 */

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