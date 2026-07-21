package com.example._6su_team3;

import java.util.HashMap;
import java.util.Map;

public class RegistrationService {

    private final Map<String, User> users = new HashMap<>();

    public RegistrationService() {
        // US-13 requires at least one administrator account.
        users.put("admin", new User("admin", "admin123", true));
    }

    /**
     * Registers a normal system user.
     * Public registration must not create administrators.
     */
    public User register(String username, String password) {
        return createUser(username, password, false);
    }

    private User createUser(String username, String password, boolean isAdmin) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must be at least 6 characters"
            );
        }

        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User(username, password, isAdmin);
        users.put(username, newUser);
        return newUser;
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public User getUser(String username) {
        return users.get(username);
    }
    public void requireAdmin(User user) {
        if (user == null || !user.isAdmin()) {
            throw new SecurityException("Access denied. Administrator privileges required.");
        }
    }

}