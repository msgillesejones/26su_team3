package com.example._6su_team3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    private SessionManager sessionManager;
    private User user;

    @BeforeEach
    void setUp() {
        sessionManager = new SessionManager();
        user = new User("gillese", "password123", false);
    }

    @Test
    void userIsNotLoggedInAtStart() {
        assertFalse(sessionManager.isLoggedIn());
        assertNull(sessionManager.getCurrentUser());
    }

    @Test
    void loginStoresCurrentUser() {
        sessionManager.login(user);

        assertTrue(sessionManager.isLoggedIn());
        assertEquals(user, sessionManager.getCurrentUser());
    }

    @Test
    void logoutClearsCurrentUser() {
        sessionManager.login(user);

        sessionManager.logout();

        assertFalse(sessionManager.isLoggedIn());
        assertNull(sessionManager.getCurrentUser());
    }
}