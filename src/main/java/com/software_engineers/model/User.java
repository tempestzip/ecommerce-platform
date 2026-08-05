package com.software_engineers.model;

/**
 * Represents a registered user of the application.
 * Acts as a simple data container passed between UserDAO and the JavaFX UI.
 *
 * @version 1.0
 */
public class User
{
    /**
     * Unique identifier for the user (matches the Users.id column).
     */
    private int id;

    /**
     * The user's login username. Must be unique.
     */
    private String username;

    /**
     * SHA-256 hash of the user's password. Never stores the plain-text password.
     */
    private String password;

    /**
     * The user's email address. Must be unique.
     */
    private String email;

    /**
     * The user's shipping/contact address.
     */
    private String address;

    /**
     * Constructs an empty User. Fields can be set individually via setters.
     */
    public User()
    {
    }

    /**
     * Constructs a fully-populated User.
     *
     * @param id       the user's database id
     * @param username the user's login username
     * @param password the user's hashed password
     * @param email    the user's email address
     * @param address  the user's shipping/contact address
     */
    public User(int id, String username, String password, String email, String address)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}