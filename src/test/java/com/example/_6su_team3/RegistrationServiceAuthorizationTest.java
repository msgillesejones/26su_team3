package com.example._6su_team3;

import com.team3.persistence.RegistrationPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceAuthorizationTest {

    private RegistrationService service;
    private RegistrationController controller;

    @BeforeEach
    void setUp() {
        String testFileName =
                "authorization-users-" + System.nanoTime() + ".json";

        service = new RegistrationService(
                new RegistrationPersistence(testFileName)
        );

        controller = new RegistrationController(service);
    }

    @Test
    void adminUserIsAllowed() {
        User admin = service.getUser("admin");

        assertDoesNotThrow(() -> service.requireAdmin(admin));
    }

    @Test
    void regularUserIsDenied() {
        User user = service.register("testuser", "password123");

        assertThrows(
                SecurityException.class,
                () -> service.requireAdmin(user)
        );
    }

    @Test
    void nullUserIsDenied() {
        assertThrows(
                SecurityException.class,
                () -> service.requireAdmin(null)
        );
    }

    @Test
    void controllerAllowsLoggedInAdministrator() {
        String loginResult =
                controller.loginUser("admin", "admin123");

        String accessResult =
                controller.accessAdminFeature();

        assertEquals("SUCCESS", loginResult);
        assertEquals("ADMIN ACCESS GRANTED", accessResult);
    }

    @Test
    void controllerRejectsLoggedInRegularUser() {
        controller.registerUser("gillese", "password123");

        String loginResult =
                controller.loginUser("gillese", "password123");

        String accessResult =
                controller.accessAdminFeature();

        assertEquals("SUCCESS", loginResult);
        assertEquals(
                "Access denied. Administrator privileges required.",
                accessResult
        );
    }
}