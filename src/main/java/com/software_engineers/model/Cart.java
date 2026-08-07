package com.software_engineers.model;

/**
 * Represents a single line item in a user's shopping cart.
 * Acts as a simple data container passed between CartDAO and the JavaFX UI.
 *
 * @version 1.0
 */
public class Cart
{
    /**
     * Unique identifier for the cart entry (matches the Cart.id column).
     */
    private int id;

    /**
     * The id of the user this cart entry belongs to.
     */
    private int userId;

    /**
     * The id of the product this cart entry refers to.
     */
    private int productId;

    /**
     * The quantity of the product the user wants to purchase.
     */
    private int quantity;

    /**
     * Constructs an empty Cart entry. Fields can be set individually via setters.
     */
    public Cart()
    {
    }

    /**
     * Constructs a fully-populated Cart entry.
     *
     * @param id        the cart entry's database id
     * @param userId    the id of the user this entry belongs to
     * @param productId the id of the product this entry refers to
     * @param quantity  the quantity of the product
     */
    public Cart(int id, int userId, int productId, int quantity)
    {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}