package com.example._6su_team3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    private RegistrationService service;

    @BeforeEach
    void setUp() {
        service = new RegistrationService();
    }

    @Test
    void registerCreatesUserWithValidInformation() {
        User user = service.register("gillese", "password123", false);

        assertNotNull(user);
        assertEquals("gillese", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertFalse(user.isAdmin());
        assertTrue(service.userExists("gillese"));
    }

    @Test
    void registerRejectsMissingUsername() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("", "password123", false)
        );

        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    void registerRejectsBlankUsername() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("   ", "password123", false)
        );

        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    void registerRejectsMissingPassword() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("gillese", "", false)
        );

        assertEquals("Password is required", exception.getMessage());
    }

    @Test
    void registerRejectsPasswordShorterThanSixCharacters() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("gillese", "12345", false)
        );

        assertEquals(
                "Password must be at least 6 characters",
                exception.getMessage()
        );
    }

    @Test
    void registerAcceptsPasswordWithExactlySixCharacters() {
        User user = service.register("gillese", "123456", false);

        assertNotNull(user);
        assertTrue(service.userExists("gillese"));
    }

    @Test
    void registerRejectsDuplicateUsername() {
        service.register("gillese", "password123", false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("gillese", "anotherPassword", false)
        );

        assertEquals("Username already exists", exception.getMessage());
    }
}