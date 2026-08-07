package com.software_engineers.model;

/**
 * Represents a customer review left on a product.
 * Acts as a simple data container passed between ReviewDAO and the JavaFX UI.
 *
 * @version 1.0
 */
public class Review
{
    /**
     * Unique identifier for the review (matches the Reviews.id column).
     */
    private int id;

    /**
     * The id of the user who wrote the review.
     */
    private int userId;

    /**
     * The id of the product being reviewed.
     */
    private int productId;

    /**
     * The star rating given, from 1 to 5.
     */
    private int rating;

    /**
     * The written comment left by the user.
     */
    private String comment;

    /**
     * The date the review was submitted, stored as an ISO-8601 string.
     */
    private String date;

    /**
     * Constructs an empty Review. Fields can be set individually via setters.
     */
    public Review()
    {
    }

    /**
     * Constructs a fully-populated Review.
     *
     * @param id        the review's database id
     * @param userId    the id of the user who wrote the review
     * @param productId the id of the product being reviewed
     * @param rating    the star rating, from 1 to 5
     * @param comment   the written comment
     * @param date      the date the review was submitted
     */
    public Review(int id, int userId, int productId, int rating, String comment, String date)
    {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
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

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
}