package com.software_engineers.database;

import com.software_engineers.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for the Users table. Handles all database operations
 * related to user accounts: registration, lookup, login verification, and updates.
 * @author Marcus
 * @version 1.0
 */
public class UserDAO
{
    /**
     * Hashes a plain-text password using SHA-256 and returns it as a hex string.
     * The password is never stored in plain text in the database.
     * We used SHA-256 because its already built into Java so we didnt need to add another dependency. Its not the strongest option
     * but its way better than storing plain text. 
     *
     * @param plainPassword the plain-text password to hash
     * @return the SHA-256 hash of the password, as a hex string
     */
    private String hashPassword(String plainPassword)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainPassword.getBytes());

            // Convert the raw hash bytes into a readable hex string.
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes)
            {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            // SHA-256 is guaranteed to be available on every standard JVM.
            throw new RuntimeException("SHA-256 algorithm not available.", e);
        }
    }

    /**
     * Registers a new user by inserting a row into the Users table.
     * The password is hashed before being stored.
     *
     * @param username the desired username
     * @param plainPassword the user's chosen password, in plain text
     * @param email the user's email address
     * @param address the user's shipping/contact address
     * @return the generated id of the new user, or -1 if creation failed
     */
    public int createUser(String username, String plainPassword, String email, String address)
    {
        String sql = "INSERT INTO Users (username, password, email, address) VALUES (?, ?, ?, ?);";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(plainPassword));
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.executeUpdate();

            // Retrieve the auto-generated id for the new user.
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
            throw new RuntimeException("Failed to create user: " + username, e);
        }
        return -1;
    }

    /**
     * Looks up a user by username.
     *
     * @param username the username to search for
     * @return the matching User, or null if no user with that username exists
     */
    public User getUserByUsername(String username)
    {
        String sql = "SELECT * FROM Users WHERE username = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("address")
                    );
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to look up user: " + username, e);
        }
        return null;
    }

    /**
     * Verifies a login attempt by comparing the hash of the given password
     * against the stored hash for the given username.
     *
     * @param username the username attempting to log in
     * @param plainPassword the plain-text password entered by the user
     * @return true if the username exists and the password matches, false otherwise
     */
    public boolean verifyLogin(String username, String plainPassword)
    {
        User user = getUserByUsername(username);
        if (user == null)
        {
            return false;
        }
        return user.getPassword().equals(hashPassword(plainPassword));
    }

    /**
     * Updates a user's username, email, and address. Does not change the password;
     * use a dedicated method for password changes to avoid accidental overwrites.
     *
     * @param user the User object containing the updated field values (id must be set)
     * @return true if a row was updated, false if no matching user was found
     */
    public boolean updateUser(User user)
    {
        String sql = "UPDATE Users SET username = ?, email = ?, address = ? WHERE id = ?;";
        Connection conn = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getAddress());
            stmt.setInt(4, user.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Failed to update user id: " + user.getId(), e);
        }
    }
}