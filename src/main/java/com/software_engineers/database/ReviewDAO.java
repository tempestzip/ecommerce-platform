package com.software_engineers.database;

import com.software_engineers.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Reviews table. Handles all database operations
 * related to product reviews: creation, lookup, updates, and deletion.
 *
 * @version 1.0
 */
public class ReviewDAO
{
    /**
     * Adds a new review for a product. The rating must be between 1 and 5
     * (also enforced by a CHECK constraint on the Reviews table).
     *
     * @param userId    the id of the user writing the review
     * @param productId the id of the product being reviewed
     * @param rating    the star rating, from 1 to 5
     * @param comment   the written comment
     * @return the generated id of the new review, or -1 if creation failed
     */
    public int createReview(int userId, int productId, int rating, String comment)
    {
        String sql = "INSERT INTO Reviews (user_id, product_id, rating, comment, date) VALUES (?, ?, ?, ?, ?);";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            stmt.setString(5, LocalDateTime.now().toString());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys())
            {
                if (keys.next())
                {
                    return keys.getInt(1);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to create review for product " + productId + " by user " + userId, e);
        }
        return -1;
    }

    /**
     * Returns every review left for a given product, most recent first.
     *
     * @param productId the id of the product to fetch reviews for
     * @return a list of the product's reviews (empty if it has none)
     */
    public List<Review> getReviewsByProduct(int productId)
    {
        String sql = "SELECT * FROM Reviews WHERE product_id = ? ORDER BY date DESC;";
        List<Review> reviews = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    reviews.add(mapRowToReview(rs));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch reviews for product " + productId, e);
        }
        return reviews;
    }

    /**
     * Returns every review written by a given user, most recent first.
     *
     * @param userId the id of the user to fetch reviews for
     * @return a list of the user's reviews (empty if they have none)
     */
    public List<Review> getReviewsByUser(int userId)
    {
        String sql = "SELECT * FROM Reviews WHERE user_id = ? ORDER BY date DESC;";
        List<Review> reviews = new ArrayList<>();
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    reviews.add(mapRowToReview(rs));
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to fetch reviews for user " + userId, e);
        }
        return reviews;
    }

    /**
     * Computes the average rating for a product across all its reviews.
     * Used to display a product's overall rating in the UI.
     *
     * @param productId the id of the product
     * @return the average rating, or 0.0 if the product has no reviews
     */
    public double getAverageRating(int productId)
    {
        String sql = "SELECT AVG(rating) AS avg_rating FROM Reviews WHERE product_id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getDouble("avg_rating");
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to compute average rating for product " + productId, e);
        }
        return 0.0;
    }

    /**
     * Updates the rating and comment of an existing review.
     *
     * @param review the Review object containing the updated field values (id must be set)
     * @return true if a row was updated, false if no matching review was found
     */
    public boolean updateReview(Review review)
    {
        String sql = "UPDATE Reviews SET rating = ?, comment = ? WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, review.getRating());
            stmt.setString(2, review.getComment());
            stmt.setInt(3, review.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to update review id: " + review.getId(), e);
        }
    }

    /**
     * Deletes a review.
     *
     * @param id the id of the review to delete
     * @return true if a row was deleted, false if no matching review was found
     */
    public boolean deleteReview(int id)
    {
        String sql = "DELETE FROM Reviews WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to delete review id: " + id, e);
        }
    }

    /**
     * Maps the current row of a ResultSet to a Review object.
     * Shared helper used by every query method above to avoid repeating the same mapping code.
     *
     * @param rs the ResultSet positioned at a valid row
     * @return a Review populated from the current row
     * @throws SQLException if a column can't be read
     */
    private Review mapRowToReview(ResultSet rs) throws SQLException
    {
        return new Review(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("product_id"),
                rs.getInt("rating"),
                rs.getString("comment"),
                rs.getString("date")
        );
    }
}