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
    @Test
    void controllerAllowsLoggedInAdministrator() {
        RegistrationController controller = new RegistrationController();

        String loginResult = controller.loginUser("admin", "admin123");
        String accessResult = controller.accessAdminFeature();

        assertEquals("SUCCESS", loginResult);
        assertEquals("ADMIN ACCESS GRANTED", accessResult);
    }
    @Test
    void controllerRejectsLoggedInRegularUser() {
        RegistrationController controller = new RegistrationController();

        controller.registerUser("gillese", "password123");
        String loginResult = controller.loginUser("gillese", "password123");
        String accessResult = controller.accessAdminFeature();

        assertEquals("SUCCESS", loginResult);
        assertEquals(
                "Access denied. Administrator privileges required.",
                accessResult
        );
    }
}
