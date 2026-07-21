package com.example._6su_team3;

import java.util.HashMap;

public class RegistrationService {

    private HashMap<String, User> users = new HashMap<>();

    public User register(String username, String password, boolean isAdmin) {

        // Missing fields
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // Minimum password length
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // Duplicate usernames
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
}
