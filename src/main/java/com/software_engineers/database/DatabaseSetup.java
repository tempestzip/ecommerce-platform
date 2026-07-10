package com.software_engineers.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

 // Creates all application tables in the SQLite database if they do not already exist.
 
public class DatabaseSetup {

     // Runs all CREATE TABLE statements. Safe to call every time the app starts,
     // since each statement uses IF NOT EXISTS.
     
    public static void initializeDatabase() {
        Connection conn = DatabaseConnection.getInstance().getConnection();

        String createUsers = "CREATE TABLE IF NOT EXISTS Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "address TEXT" +
                ");";

        String createProducts = "CREATE TABLE IF NOT EXISTS Products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "price REAL NOT NULL," +
                "category TEXT," +
                "stock INTEGER NOT NULL DEFAULT 0" +
                ");";

        String createOrders = "CREATE TABLE IF NOT EXISTS Orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "total_price REAL NOT NULL," +
                "date TEXT NOT NULL," +
                "status TEXT NOT NULL," +
                "shipping_address TEXT," +
                "FOREIGN KEY (user_id) REFERENCES Users(id)" +
                ");";

        String createCart = "CREATE TABLE IF NOT EXISTS Cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL DEFAULT 1," +
                "FOREIGN KEY (user_id) REFERENCES Users(id)," +
                "FOREIGN KEY (product_id) REFERENCES Products(id)" +
                ");";

        String createOrderItems = "CREATE TABLE IF NOT EXISTS Order_Items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY (order_id) REFERENCES Orders(id)," +
                "FOREIGN KEY (product_id) REFERENCES Products(id)" +
                ");";

        String createReviews = "CREATE TABLE IF NOT EXISTS Reviews (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "product_id INTEGER NOT NULL," +
                "rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5)," +
                "comment TEXT," +
                "date TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES Users(id)," +
                "FOREIGN KEY (product_id) REFERENCES Products(id)" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createProducts);
            stmt.execute(createOrders);
            stmt.execute(createCart);
            stmt.execute(createOrderItems);
            stmt.execute(createReviews);
            System.out.println("Database tables verified/created successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database tables.", e);
        }
    }
}