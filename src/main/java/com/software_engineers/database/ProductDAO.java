package com.software_engineers.database;

import com.software_engineers.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Products table. Handles all database operations
 * related to products: creation, lookup, searching, listing, and stock/updates.
 *
 * @version 1.0
 */
public class ProductDAO
{
    /**
     * Adds a new product to the catalog.
     *
     * @param name        the product's display name
     * @param description a short description of the product
     * @param price       the product's price, in dollars
     * @param category    the product's category
     * @param stock       the initial number of units in stock
     * @return the generated id of the new product, or -1 if creation failed
     */
    public int createProduct(String name, String description, double price, String category, int stock)
    {
        String sql = "INSERT INTO Products (name, description, price, category, stock) VALUES (?, ?, ?, ?, ?);";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setString(4, category);
            stmt.setInt(5, stock);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys())
            {
                if (keys.next())
                {
                    return keys.getInt(1);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to create product: " + name, e);
        }
        return -1;
    }

    /**
     * Looks up a single product by its id.
     *
     * @param id the product's database id
     * @return the matching Product, or null if no product with that id exists
     */
    public Product getProductById(int id)
    {
        String sql = "SELECT * FROM Products WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return mapRowToProduct(rs);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to look up product id: " + id, e);
        }
        return null;
    }

    /**
     * Returns every product in the catalog.
     *
     * @return a list of all products (empty if none exist)
     */
    public List<Product> getAllProducts()
    {
        String sql = "SELECT * FROM Products;";
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                products.add(mapRowToProduct(rs));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch all products.", e);
        }
        return products;
    }

    /**
     * Searches for products whose name contains the given keyword (case-insensitive).
     * Used for the customer-facing "search for an item" feature.
     *
     * @param keyword the search term to match against product names
     * @return a list of matching products (empty if none match)
     */
    public List<Product> searchProductsByName(String keyword)
    {
        String sql = "SELECT * FROM Products WHERE name LIKE ? COLLATE NOCASE;";
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    products.add(mapRowToProduct(rs));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to search products by name: " + keyword, e);
        }
        return products;
    }

    /**
     * Returns every product belonging to the given category.
     *
     * @param category the category to filter by
     * @return a list of matching products (empty if none match)
     */
    public List<Product> getProductsByCategory(String category)
    {
        String sql = "SELECT * FROM Products WHERE category = ?;";
        List<Product> products = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    products.add(mapRowToProduct(rs));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch products by category: " + category, e);
        }
        return products;
    }

    /**
     * Updates a product's name, description, price, category, and stock.
     *
     * @param product the Product object containing the updated field values (id must be set)
     * @return true if a row was updated, false if no matching product was found
     */
    public boolean updateProduct(Product product)
    {
        String sql = "UPDATE Products SET name = ?, description = ?, price = ?, category = ?, stock = ? WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getCategory());
            stmt.setInt(5, product.getStock());
            stmt.setInt(6, product.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to update product id: " + product.getId(), e);
        }
    }

    /**
     * Adjusts a product's stock by the given amount (negative to decrease, e.g. after a sale).
     *
     * @param productId the id of the product to adjust
     * @param amount    the amount to add to current stock (use a negative number to subtract)
     * @return true if the stock was updated, false if no matching product was found
     */
    public boolean adjustStock(int productId, int amount)
    {
        String sql = "UPDATE Products SET stock = stock + ? WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, amount);
            stmt.setInt(2, productId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to adjust stock for product id: " + productId, e);
        }
    }

    /**
     * Deletes a product from the catalog.
     *
     * @param id the id of the product to delete
     * @return true if a row was deleted, false if no matching product was found
     */
    public boolean deleteProduct(int id)
    {
        String sql = "DELETE FROM Products WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to delete product id: " + id, e);
        }
    }

    /**
     * Maps the current row of a ResultSet to a Product object.
     * Shared helper used by every query method above to avoid repeating the same mapping code.
     *
     * @param rs the ResultSet positioned at a valid row
     * @return a Product populated from the current row
     * @throws SQLException if a column can't be read
     */
    private Product mapRowToProduct(ResultSet rs) throws SQLException
    {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getString("category"),
                rs.getInt("stock")
        );
    }
}