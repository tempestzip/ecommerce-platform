package com.software_engineers.database;

import com.software_engineers.model.Order;
import com.software_engineers.model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Orders and Order_Items tables. Handles placing
 * orders, viewing order history, updating order status, and basic sales
 * reporting for managers.
 *
 * @version 1.0
 */
public class OrderDAO
{
    /**
     * Places a new order: inserts the Orders row and all of its Order_Items
     * rows as a single transaction. If any part fails, the entire order is
     * rolled back so no partial order is ever left in the database.
     *
     * @param userId          the id of the user placing the order
     * @param shippingAddress the address to ship the order to
     * @param items           the items being purchased (id/orderId on each item are ignored and assigned automatically)
     * @return the generated id of the new order, or -1 if the order failed
     */
    public int placeOrder(int userId, String shippingAddress, List<OrderItem> items)
    {
        double totalPrice = 0.0;
        for (OrderItem item : items)
        {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        Connection conn = DatabaseConnection.getInstance().getConnection();
        String insertOrderSql = "INSERT INTO Orders (user_id, total_price, date, status, shipping_address) VALUES (?, ?, ?, ?, ?);";
        String insertItemSql = "INSERT INTO Order_Items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?);";

        try
        {
            conn.setAutoCommit(false);

            int orderId;
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS))
            {
                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, totalPrice);
                orderStmt.setString(3, LocalDateTime.now().toString());
                orderStmt.setString(4, "Pending");
                orderStmt.setString(5, shippingAddress);
                orderStmt.executeUpdate();

                try (ResultSet keys = orderStmt.getGeneratedKeys())
                {
                    if (!keys.next())
                    {
                        throw new SQLException("Order insert did not return a generated id.");
                    }
                    orderId = keys.getInt(1);
                }
            }

            try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSql))
            {
                for (OrderItem item : items)
                {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, item.getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getPrice());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            conn.commit();
            return orderId;
        }
        catch (SQLException e)
        {
            try
            {
                conn.rollback();
            }
            catch (SQLException rollbackEx)
            {
                throw new RuntimeException("Failed to roll back failed order for user " + userId, rollbackEx);
            }
            throw new RuntimeException("Failed to place order for user " + userId, e);
        }
        finally
        {
            try
            {
                conn.setAutoCommit(true);
            }
            catch (SQLException e)
            {
                throw new RuntimeException("Failed to restore auto-commit after placing order.", e);
            }
        }
    }

    /**
     * Looks up a single order by id, including its list of order items.
     *
     * @param orderId the id of the order to fetch
     * @return the matching Order with its items populated, or null if no order with that id exists
     */
    public Order getOrderById(int orderId)
    {
        String sql = "SELECT * FROM Orders WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    Order order = mapRowToOrder(rs);
                    order.setItems(getOrderItems(orderId));
                    return order;
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to look up order id: " + orderId, e);
        }
        return null;
    }

    /**
     * Returns all order items belonging to a given order.
     *
     * @param orderId the id of the order
     * @return a list of the order's items (empty if the order has none)
     */
    public List<OrderItem> getOrderItems(int orderId)
    {
        String sql = "SELECT * FROM Order_Items WHERE order_id = ?;";
        List<OrderItem> items = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    items.add(new OrderItem(
                            rs.getInt("id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    ));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch items for order id: " + orderId, e);
        }
        return items;
    }

    /**
     * Returns every order placed by a given user, ordered most recent first.
     * Does not populate each order's items (call getOrderItems separately if needed)
     * to keep order history listing lightweight.
     *
     * @param userId the id of the user whose orders to fetch
     * @return a list of the user's orders (empty if they have none)
     */
    public List<Order> getOrdersByUser(int userId)
    {
        String sql = "SELECT * FROM Orders WHERE user_id = ? ORDER BY date DESC;";
        List<Order> orders = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    orders.add(mapRowToOrder(rs));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch orders for user " + userId, e);
        }
        return orders;
    }

    /**
     * Returns every order in the system, ordered most recent first.
     * Intended for the manager-facing sales report screen.
     *
     * @return a list of all orders (empty if none exist)
     */
    public List<Order> getAllOrders()
    {
        String sql = "SELECT * FROM Orders ORDER BY date DESC;";
        List<Order> orders = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                orders.add(mapRowToOrder(rs));
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch all orders.", e);
        }
        return orders;
    }

    /**
     * Updates an order's status (e.g. "Pending", "Shipped", "Cancelled").
     *
     * @param orderId   the id of the order to update
     * @param newStatus the new status value
     * @return true if a row was updated, false if no matching order was found
     */
    public boolean updateOrderStatus(int orderId, String newStatus)
    {
        String sql = "UPDATE Orders SET status = ? WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to update status for order id: " + orderId, e);
        }
    }

    /**
     * Cancels an order by setting its status to "Cancelled".
     * Convenience wrapper around updateOrderStatus for the customer-facing cancel action.
     *
     * @param orderId the id of the order to cancel
     * @return true if the order was found and cancelled, false otherwise
     */
    public boolean cancelOrder(int orderId)
    {
        return updateOrderStatus(orderId, "Cancelled");
    }

    /**
     * Computes total sales volume across all non-cancelled orders.
     * Used by the manager-facing sales report.
     *
     * @return the sum of total_price for every order not marked "Cancelled"
     */
    public double getTotalSalesVolume()
    {
        String sql = "SELECT SUM(total_price) AS total FROM Orders WHERE status != 'Cancelled';";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {
            if (rs.next())
            {
                return rs.getDouble("total");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to compute total sales volume.", e);
        }
        return 0.0;
    }

    /**
     * Maps the current row of a ResultSet to an Order object (without items).
     * Shared helper used by every query method above to avoid repeating the same mapping code.
     *
     * @param rs the ResultSet positioned at a valid row
     * @return an Order populated from the current row
     * @throws SQLException if a column can't be read
     */
    private Order mapRowToOrder(ResultSet rs) throws SQLException
    {
        return new Order(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getDouble("total_price"),
                rs.getString("date"),
                rs.getString("status"),
                rs.getString("shipping_address")
        );
    }
}