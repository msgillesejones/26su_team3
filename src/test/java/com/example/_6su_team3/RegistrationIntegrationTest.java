package com.example._6su_team3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationIntegrationTest {

    private RegistrationService service;
    private RegistrationController controller;

    @BeforeEach
    void setUp() {
        service = new RegistrationService();
        controller = new RegistrationController(service);
    }

    // US-13, Acceptance Test:
    // Given valid registration information, when the user registers,
    // then the account is created successfully.
    @Test
    void validRegistrationCreatesAccountThroughController() {
        String result = controller.registerUser("newuser", "password123");

        assertEquals("SUCCESS", result);
        assertTrue(service.userExists("newuser"));

        User createdUser = service.getUser("newuser");
        assertNotNull(createdUser);
        assertFalse(createdUser.isAdmin());
    }

    // US-13, Acceptance Test:
    // Given a duplicate username, when registration is attempted,
    // then the account is rejected.
    @Test
    void duplicateUsernameIsRejectedThroughController() {
        assertEquals(
                "SUCCESS",
                controller.registerUser("duplicate", "password123")
        );

        String secondResult =
                controller.registerUser("duplicate", "anotherPassword");

        assertEquals("Username already exists", secondResult);
    }

    // US-13, Acceptance Test:
    // Given an invalid password, when registration is attempted,
    // then the account is not created.
    @Test
    void shortPasswordIsRejectedThroughController() {
        String result = controller.registerUser("shorttest", "12345");

        assertEquals(
                "Password must be at least 6 characters",
                result
        );
        assertFalse(service.userExists("shorttest"));
    }
}
