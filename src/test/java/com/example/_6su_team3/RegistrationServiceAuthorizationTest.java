package com.example._6su_team3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceAuthorizationTest {

    @Test
    void adminUserIsAllowed() {
        RegistrationService service = new RegistrationService();

        User admin = service.getUser("admin");

        assertDoesNotThrow(() -> service.requireAdmin(admin));
    }

    @Test
    void regularUserIsDenied() {
        RegistrationService service = new RegistrationService();

        User user = service.register("testuser", "password123");

        assertThrows(SecurityException.class,
                () -> service.requireAdmin(user));
    }

    @Test
    void nullUserIsDenied() {
        RegistrationService service = new RegistrationService();

        assertThrows(SecurityException.class,
                () -> service.requireAdmin(null));
    }
}
