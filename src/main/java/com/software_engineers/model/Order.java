package com.software_engineers.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer order, including its shipping/status details and
 * the list of items purchased. Acts as a simple data container passed
 * between OrderDAO and the JavaFX UI.
 *
 * @version 1.0
 */
public class Order
{
    /**
     * Unique identifier for the order (matches the Orders.id column).
     */
    private int id;

    /**
     * The id of the user who placed this order.
     */
    private int userId;

    /**
     * The total price of the order, summed across all order items.
     */
    private double totalPrice;

    /**
     * The date the order was placed, stored as an ISO-8601 string.
     */
    private String date;

    /**
     * The order's current status (e.g. "Pending", "Shipped", "Cancelled").
     */
    private String status;

    /**
     * The address the order should be shipped to.
     */
    private String shippingAddress;

    /**
     * The items included in this order. Not a column on the Orders table itself;
     * populated separately by OrderDAO from the Order_Items table when needed.
     */
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Constructs an empty Order. Fields can be set individually via setters.
     */
    public Order()
    {
    }

    /**
     * Constructs a fully-populated Order (without items).
     *
     * @param id              the order's database id
     * @param userId          the id of the user who placed the order
     * @param totalPrice      the total price of the order
     * @param date            the date the order was placed
     * @param status          the order's current status
     * @param shippingAddress the shipping address for the order
     */
    public Order(int id, int userId, double totalPrice, String date, String status, String shippingAddress)
    {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.date = date;
        this.status = status;
        this.shippingAddress = shippingAddress;
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

    public double getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getShippingAddress()
    {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress)
    {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItem> getItems()
    {
        return items;
    }

    public void setItems(List<OrderItem> items)
    {
        this.items = items;
    }
}