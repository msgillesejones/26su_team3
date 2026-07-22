package com.example._6su_team3;

public class SessionManager {

    private User currentUser;

    public void login(User user) {
        currentUser = user;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
