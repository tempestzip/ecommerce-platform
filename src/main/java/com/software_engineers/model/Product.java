package com.software_engineers.model;

/**
 * Represents a stationery product sold in the store.
 * Acts as a simple data container passed between ProductDAO and the JavaFX UI.
 *
 * @version 1.0
 */
public class Product
{
    /**
     * Unique identifier for the product (matches the Products.id column).
     */
    private int id;

    /**
     * The product's display name.
     */
    private String name;

    /**
     * A short description of the product.
     */
    private String description;

    /**
     * The product's price, in dollars.
     */
    private double price;

    /**
     * The category the product belongs to (e.g. "Notebooks", "Pens").
     */
    private String category;

    /**
     * The number of units currently in stock.
     */
    private int stock;

    /**
     * Constructs an empty Product. Fields can be set individually via setters.
     */
    public Product()
    {
    }

    /**
     * Constructs a fully-populated Product.
     *
     * @param id          the product's database id
     * @param name        the product's display name
     * @param description a short description of the product
     * @param price       the product's price, in dollars
     * @param category    the product's category
     * @param stock       the number of units currently in stock
     */
    public Product(int id, String name, String description, double price, String category, int stock)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }
}