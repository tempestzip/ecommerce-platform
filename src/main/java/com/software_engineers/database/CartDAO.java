package com.software_engineers.database;

import com.software_engineers.model.Cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Cart table. Handles all database operations
 * related to a user's shopping cart: adding items, viewing, updating
 * quantities, and removing items.
 *
 * @version 1.0
 */
public class CartDAO
{
    /**
     * Adds a product to a user's cart. If the user already has this product
     * in their cart, increases the existing quantity instead of creating a
     * duplicate row.
     *
     * @param userId    the id of the user adding the item
     * @param productId the id of the product being added
     * @param quantity  the quantity to add
     * @return the id of the affected cart row, or -1 if the operation failed
     */
    public int addToCart(int userId, int productId, int quantity)
    {
        Cart existing = getCartItemByUserAndProduct(userId, productId);

        if (existing != null)
        {
            boolean updated = updateQuantity(existing.getId(), existing.getQuantity() + quantity);
            return updated ? existing.getId() : -1;
        }

        String sql = "INSERT INTO Cart (user_id, product_id, quantity) VALUES (?, ?, ?);";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
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
            throw new RuntimeException("Failed to add product " + productId + " to cart for user " + userId, e);
        }
        return -1;
    }

    /**
     * Looks up a specific cart entry by user and product, used internally to
     * detect whether a product is already in the user's cart.
     *
     * @param userId    the id of the user
     * @param productId the id of the product
     * @return the matching Cart entry, or null if the user doesn't have this product in their cart
     */
    public Cart getCartItemByUserAndProduct(int userId, int productId)
    {
        String sql = "SELECT * FROM Cart WHERE user_id = ? AND product_id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return mapRowToCart(rs);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to look up cart item for user " + userId + ", product " + productId, e);
        }
        return null;
    }

    /**
     * Returns every item currently in a user's cart.
     *
     * @param userId the id of the user whose cart to fetch
     * @return a list of the user's cart entries (empty if their cart is empty)
     */
    public List<Cart> getCartItemsByUser(int userId)
    {
        String sql = "SELECT * FROM Cart WHERE user_id = ?;";
        List<Cart> items = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    items.add(mapRowToCart(rs));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch cart items for user " + userId, e);
        }
        return items;
    }

    /**
     * Updates the quantity of an existing cart entry.
     *
     * @param cartId      the id of the cart entry to update
     * @param newQuantity the new quantity
     * @return true if a row was updated, false if no matching cart entry was found
     */
    public boolean updateQuantity(int cartId, int newQuantity)
    {
        String sql = "UPDATE Cart SET quantity = ? WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, cartId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to update quantity for cart id: " + cartId, e);
        }
    }

    /**
     * Removes a single item from a cart.
     *
     * @param cartId the id of the cart entry to remove
     * @return true if a row was deleted, false if no matching cart entry was found
     */
    public boolean removeFromCart(int cartId)
    {
        String sql = "DELETE FROM Cart WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, cartId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to remove cart item id: " + cartId, e);
        }
    }

    /**
     * Empties a user's entire cart. Typically called right after an order is
     * successfully placed.
     *
     * @param userId the id of the user whose cart should be cleared
     * @return true if at least one row was deleted, false if the cart was already empty
     */
    public boolean clearCart(int userId)
    {
        String sql = "DELETE FROM Cart WHERE user_id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to clear cart for user " + userId, e);
        }
    }

    /**
     * Maps the current row of a ResultSet to a Cart object.
     * Shared helper used by every query method above to avoid repeating the same mapping code.
     *
     * @param rs the ResultSet positioned at a valid row
     * @return a Cart populated from the current row
     * @throws SQLException if a column can't be read
     */
    private Cart mapRowToCart(ResultSet rs) throws SQLException
    {
        return new Cart(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("product_id"),
                rs.getInt("quantity")
        );
    }
}