package com.software_engineers.database;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.software_engineers.model.Cart;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Unit test for CartDAO. Covers class testing and independent path testing for
 * addToCart and getCartItemByUserAndProduct
 *
 * @author Rolando
 *
 */
public class CartDAOTest {
    private static final String TEST_USERNAME = "cart_test";
    private static final String TEST_EMAIL = "cart_test@example.com";
    private static final String TEST_PRODUCT_NAME = "Test Paper";

    private CartDAO cartDAO;
    private int testUserId;
    private int testProductId;

    @BeforeAll
    static void setupDatabase() {
        DatabaseSetup.initializeDatabase();
    }

    @BeforeEach
    void setUp() throws SQLException {
        cartDAO = new CartDAO();
        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();

        Connection conn = DatabaseConnection.getInstance().getConnection();

        // Clean up leftover test data in FK-safe order: cart -> products -> users.
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Cart WHERE user_id IN (SELECT id FROM Users WHERE username = ?) AND product_id IN (SELECT id FROM Products WHERE name = ?)")) {

            stmt.setString(1, TEST_USERNAME);
            stmt.setString(2, TEST_PRODUCT_NAME);
            stmt.executeUpdate();
        }
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Users WHERE username = ?")) {
            stmt.setString(1, TEST_USERNAME);
            stmt.executeUpdate();
        }

        testUserId = userDAO.createUser(TEST_USERNAME, "password123", TEST_EMAIL, "123 Test St");
        testProductId = productDAO.createProduct(TEST_PRODUCT_NAME, "Paper for testing", 0.10, "Paper", 1000);
    }

    @Test
    void testAddToCart_succeedsDoesNotExist() {
        int cartID = cartDAO.addToCart(testUserId, testProductId, 100);
        assertNotEquals(-1, cartID);

        Cart cart = cartDAO.getCartItemByUserAndProduct(testUserId, testProductId);
        assertEquals(cart.getId(), cartID);
    }

    @Test
    void testAddToCart_succeedsDoesExist() {
        final int INITIAL = 100;
        final int SUBSEQUENT = 20;
        int cartID = cartDAO.addToCart(testUserId, testProductId, INITIAL);
        boolean isSameCart = cartID == cartDAO.addToCart(testUserId, testProductId, SUBSEQUENT);
        assertTrue(isSameCart);

        Cart cartItem = cartDAO.getCartItemByUserAndProduct(testUserId, testProductId);
        assertEquals(cartItem.getQuantity(), INITIAL + SUBSEQUENT);
    }
}
