package com.software_engineers.service;

import com.software_engineers.model.Cart;
import com.software_engineers.model.Product;

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
     * @param userID    the id of the user adding the item
     * @param productID id of the product being added
     * @param quantity  the quantity to add
     *
     * @return true if the specified quantity of the product was added, false if
     *         it could not be added.
     */
    public boolean addToCart(int userID, int productID, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cannot have a non-positive quantity");
        }

        Product product = productDAO.getProductById(productID);
        if (product == null) {
            throw new IllegalArgumentException("Product does not exist");
        }

        if (product.getStock() < quantity) {
            return false;
        }

        int cartID = cartDAO.addToCart(userID, productID, quantity);
        return cartID != -1;
    }

    /**
     * Returns every item currently in a user's cart.
     *
     * @param userId the id of the user whose cart to fetch
     * @return a list of the user's cart entries (empty if their cart is empty)
     * @throws RuntimeException if userID is not bound to a user, that is the userID
     *                          does not exist in the database.
     */
    public List<Cart> getCartItems(int userID) throws RuntimeException {
        return cartDAO.getCartItemsByUser(userID);
    }
}
