package com.software_engineers.db;

import java.sql.*;

public class DatabaseDemo {
    public static void main (String[] args)
    {
        String url = "jdbc:sqlite:stationary.db";
    

        try {
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connected!");

            String createTable = "CREATE TABLE IF NOT EXISTS products (" +
            "id INTEGER PRIMARY KEY, " +
            "name TEXT NOT NULL, " +
            "price REAL NOT NULL, " +
            "description TEXT NOT NULL, " +
            "review REAL NULL " +
            ")";

            Statement stm = conn.createStatement();
            stm.execute(createTable);
            System.out.println("Table created!");

            String insert = "INSERT INTO products (name, price, description, review) VALUES (?,?,?,?)";
            PreparedStatement pstm = conn.prepareStatement(insert);
            pstm.setString(1, "Pen");
            pstm.setDouble(2, 1.99);
            pstm.setString(3, "Used for writing on paper");
            pstm.setDouble(4, 4.2);
            pstm.executeUpdate();
            System.out.println("Product inserted!");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
