package com.software_engineers.service;

import com.software_engineers.database.UserDAO;
import com.software_engineers.model.User;

public class LoginRegService {

    private UserDAO userDAO;
    private User currentUser;

    public LoginRegService() {
        this.userDAO = new UserDAO();
        this.currentUser = null;
    }

    public LoginRegService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.currentUser = null;
    }

    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (userDAO.verifyLogin(username, password)) {
            this.currentUser = userDAO.getUserByUsername(username);
        }

        return this.currentUser != null;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User register(String username, String plainPassword, String email, String address) {
        validateUserData(username, plainPassword, email);

        User existingUser = userDAO.getUserByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("Username already taken: " + username);
        }

        int userId = userDAO.createUser(username, plainPassword, email, address);

        if (userId == -1) {
            throw new RuntimeException("Failed to create user");
        }

        return userDAO.getUserByUsername(username);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public int getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : -1;
    }

    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : "Guest";
    }

    private void validateUserData(String username, String plainPassword, String email) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be between 3 and 20 characters");
        }

        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
