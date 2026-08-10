package com.software_engineers.service;

import com.software_engineers.model.Cart;
import com.software_engineers.model.Product;

import java.sql.SQLException;
import java.util.List;

import com.software_engineers.database.CartDAO;
import com.software_engineers.database.ProductDAO;

/**
 * Cart Service, handles application layer logic related to a given user's cart,
 * with validation: adding items to a user's cart
 *
 * @author Rolando Juarez
 * @version 1.0
 */
public class CartService {
    CartDAO cartDAO;
    ProductDAO productDAO;

    public CartService() {
        cartDAO = new CartDAO();
        productDAO = new ProductDAO();
    }

    /**
     * Adds a product to a user's cart with data validation. If a user's cart
     * already contains the product, the final quantity is increased by quantity
     * rather than a
     * new listing being created.
     *
     * @param userId    the id of the user adding the item
     * @param productId id of the product being added
     * @param quantity  the quantity to add
     *
     * @return true if the specified quantity of the product was added, false if
     *         it could not be added.
     * @throws IllegalArgumentException if {@code quantity} is less than or equal to
     *                                  zero
     */
    public boolean addToCart(int userId, int productId, int quantity)
            throws IllegalArgumentException, RuntimeException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cannot have a non-positive quantity");
        }

        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Product does not exist");
        }

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock. Current stock: " + product.getStock());
        }

        int cartId = cartDAO.addToCart(userId, productId, quantity);
        return cartId != -1;
    }

    /**
     * Updates the quantity of an existing cart entry with validation
     *
     * @param cartId   the id of the cart entry to update
     * @param quantity the new quantity
     * @return true if the cart entry was updated, false if no matching cart entry
     *         was found
     * @throws IllegalArgumentException if {@code quantity} is less than or equal to
     *                                  zero
     */
    public boolean setQuantity(int cartId, int quantity) throws IllegalArgumentException, RuntimeException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cannot have a non-positive quantity");
        }

        return cartDAO.updateQuantity(cartId, quantity);
    }

    /**
     * Returns every item currently in a user's cart.
     *
     * @param userId the id of the user whose cart to fetch
     * @return a list of the user's cart entries (empty if their cart is empty)
     * @throws RuntimeException if {@code userId} is not bound to a user, that is
     *                          the userId
     *                          does not exist in the database.
     */
    public List<Cart> getCartItems(int userId) throws RuntimeException {
        return cartDAO.getCartItemsByUser(userId);
    }

    /**
     * Returns the subtotal of the user's cart
     *
     * @param userId the id of the user whose cart to calculate the subtotal
     * @return the user's subtotal
     */
    public double getCartSubtotal(int userId) throws RuntimeException {
        List<Cart> cartItems = this.getCartItems(userId);

        double sum = 0;
        for (var cartItem : cartItems) {
            sum += cartToProd(cartItem).getPrice() * cartItem.getQuantity();
        }

        return sum;
    }

    public int getCartItemCount(int userId) throws RuntimeException {
        return this.getCartItems(userId).size();
    }

    private Product cartToProd(Cart cart) throws RuntimeException {
        return productDAO.getProductById(cart.getProductId());
    }
}
