package com;


public class Product {
    String name;
    double price;
    String description;
    double review;

    public Product(String theName, double thePrice, String theDescription, double theReview)
    {
        name = theName;
        price = thePrice;
        description = theDescription;
        review = theReview;
    }

    public String getName()
    {
        return this.name;
    }

    public Double getPrice()
    {
        return this.price;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Double getReview()
    {
        return this.review;
    }


}
