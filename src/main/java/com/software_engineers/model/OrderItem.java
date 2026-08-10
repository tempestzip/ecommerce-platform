package com.software_engineers.model;

/**
 * Represents a single product line within an order (matches a row in the
 * Order_Items table). Acts as a simple data container passed between
 * OrderDAO and the JavaFX UI.
 * @author Marcus
 * @version 1.0
 */
public class OrderItem
{
    /**
     * Unique identifier for the order item (matches the Order_Items.id column).
     */
    private int id;

    /**
     * The id of the order this item belongs to.
     */
    private int orderId;

    /**
     * The id of the product purchased.
     */
    private int productId;

    /**
     * The quantity of the product purchased.
     */
    private int quantity;

    /**
     * The price of the product at the time of purchase (per unit).
     * Stored separately from Products.price so that later price changes
     * don't alter the historical record of what was actually paid.
     */
    private double price;

    /**
     * Constructs an empty OrderItem. Fields can be set individually via setters.
     */
    public OrderItem()
    {
    }

    /**
     * Constructs a fully-populated OrderItem.
     *
     * @param id        the order item's database id
     * @param orderId   the id of the order this item belongs to
     * @param productId the id of the product purchased
     * @param quantity  the quantity purchased
     * @param price     the price per unit at the time of purchase
     */
    public OrderItem(int id, int orderId, int productId, int quantity, double price)
    {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
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

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }
}