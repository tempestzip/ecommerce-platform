package com.software_engineers.database;

import com.software_engineers.model.Order;
import com.software_engineers.model.OrderItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderDAO. Covers class testing (2.1) for placeOrder() and
 * updateOrderStatus(), and independent path testing (2.2) for placeOrder()'s
 * successful order case and failed order case.
 *
 * @author Marcus
 * @since 2026-08-07
 * @version 1.0
 */
class OrderDAOTest
{
    private static final String TEST_USERNAME = "order_test_user";
    private static final String TEST_EMAIL = "order_test_user@example.com";

    private OrderDAO orderDAO;
    private int testUserId;
    private int testProductId;

    @BeforeAll
    static void setupDatabase()
    {
        DatabaseSetup.initializeDatabase();
    }

    /**
     * Creates a fresh test user and test product before each test, since
     * placeOrder() needs real foreign-key-valid ids to succeed. Cleans up
     * any leftover test user from a previous run first.
     */
    @BeforeEach
    void setUp() throws SQLException
    {
        orderDAO = new OrderDAO();
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();

        Connection conn = DatabaseConnection.getInstance().getConnection();

        // Clean up leftover test data in FK-safe order: order items -> orders -> user.
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Order_Items WHERE order_id IN (SELECT id FROM Orders WHERE user_id IN (SELECT id FROM Users WHERE username = ?));"))
        {
            stmt.setString(1, TEST_USERNAME);
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Orders WHERE user_id IN (SELECT id FROM Users WHERE username = ?);"))
        {
            stmt.setString(1, TEST_USERNAME);
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Users WHERE username = ?;"))
        {
            stmt.setString(1, TEST_USERNAME);
            stmt.executeUpdate();
        }

        testUserId = userDAO.createUser(TEST_USERNAME, "password123", TEST_EMAIL, "123 Test St");
        testProductId = productDAO.createProduct("Test Notebook", "A notebook for testing", 5.99, "Notebooks", 100);
    }

    /**
     * Test Case 1 (Successful Order): everything succeeds, so the order
     * should be created with the correct total price and be retrievable.
     */
    @Test
    void testPlaceOrder_successfulOrder()
    {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(0, 0, testProductId, 2, 5.99));

        int orderId = orderDAO.placeOrder(testUserId, "123 Test St", items);

        assertTrue(orderId > 0, "placeOrder should return a valid generated id");

        Order order = orderDAO.getOrderById(orderId);
        assertNotNull(order);
        assertEquals(11.98, order.getTotalPrice(), 0.001);
        assertEquals(1, order.getItems().size());
    }

    /**
     * Test Case 2 (Failed Order): an item referencing a nonexistent product
     * violates the foreign key constraint, so the whole order should fail
     * completely and no order should be left behind.
     */
    @Test
    void testPlaceOrder_failedOrder()
    {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(0, 0, 9999999, 1, 5.99)); // nonexistent product id

        assertThrows(RuntimeException.class, () ->
                orderDAO.placeOrder(testUserId, "123 Test St", items));

        List<Order> orders = orderDAO.getOrdersByUser(testUserId);
        assertTrue(orders.isEmpty(), "no order should be left behind after a failed placeOrder call");
    }

    /**
     * Tests updateOrderStatus() as the second required method for 2.1.
     */
    @Test
    void testUpdateOrderStatus()
    {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(0, 0, testProductId, 1, 5.99));
        int orderId = orderDAO.placeOrder(testUserId, "123 Test St", items);

        boolean updated = orderDAO.updateOrderStatus(orderId, "Shipped");

        assertTrue(updated);
        assertEquals("Shipped", orderDAO.getOrderById(orderId).getStatus());
    }

    /**
     * updateOrderStatus() should return false for an order id that doesn't exist.
     */
    @Test
    void testUpdateOrderStatus_nonexistentOrder()
    {
        boolean updated = orderDAO.updateOrderStatus(999999, "Shipped");
        assertFalse(updated);
    }
}
