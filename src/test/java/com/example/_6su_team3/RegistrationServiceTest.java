package com.example._6su_team3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.team3.persistence.RegistrationPersistence;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    private RegistrationService service;

    @BeforeEach
    void setUp() {
        String testFileName =
                "test-users-" + System.nanoTime() + ".json";

        service = new RegistrationService(
                new RegistrationPersistence(testFileName)
        );
    }

    @Test
    void registerCreatesUserWithValidInformation() {
        User user = service.register("gillese", "password123");

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
                () -> service.register("", "password123")
        );

        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    void registerRejectsBlankUsername() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("   ", "password123")
        );

        assertEquals("Username is required", exception.getMessage());
    }

    @Test
    void registerRejectsMissingPassword() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("gillese", "")
        );

        assertEquals("Password is required", exception.getMessage());
    }

    @Test
    void registerRejectsPasswordShorterThanSixCharacters() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("gillese", "12345")
        );

        assertEquals(
                "Password must be at least 6 characters",
                exception.getMessage()
        );
    }

    @Test
    void registerAcceptsPasswordWithExactlySixCharacters() {
        User user = service.register("gillese", "123456");

        assertNotNull(user);
        assertTrue(service.userExists("gillese"));
    }

    @Test
    void registerRejectsDuplicateUsername() {
        service.register("gillese", "password123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.register("gillese", "anotherPassword")
        );

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void defaultAdministratorExists() {
        User administrator = service.getUser("admin");

        assertNotNull(administrator);
        assertEquals("admin", administrator.getUsername());
        assertEquals("admin123", administrator.getPassword());
        assertTrue(administrator.isAdmin());
    }
    @Test
    void loginReturnsUserWithValidCredentials() {
        service.register("gillese", "password123");

        User user = service.login("gillese", "password123");

        assertNotNull(user);
        assertEquals("gillese", user.getUsername());
    }

    @Test
    void loginRejectsInvalidCredentials() {
        service.register("gillese", "password123");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.login("gillese", "wrongPassword")
        );

        assertEquals(
                "Invalid username or password",
                exception.getMessage()
        );
    }
    @Test
    void loginStoresUserInSession() {
        service.register("gillese", "password123");
        SessionManager sessionManager = new SessionManager();

        User user = service.login(
                "gillese",
                "password123",
                sessionManager
        );

        assertTrue(sessionManager.isLoggedIn());
        assertEquals(user, sessionManager.getCurrentUser());
    }
    @Test
    void registeredUsersAreLoadedAfterRestart() {
        String testFileName =
                "persist-users-" + System.nanoTime() + ".json";

        RegistrationPersistence persistence =
                new RegistrationPersistence(testFileName);

        RegistrationService firstService =
                new RegistrationService(persistence);

        firstService.register("testuser", "password123");

        RegistrationService secondService =
                new RegistrationService(persistence);

        assertTrue(secondService.userExists("testuser"));
    }
    @Test
    void noSavedUsersLoadsOnlyDefaultAdministrator() {
        String testFileName =
                "empty-users-" + System.nanoTime() + ".json";

        RegistrationPersistence persistence =
                new RegistrationPersistence(testFileName);

        RegistrationService restartedService =
                new RegistrationService(persistence);

        assertNotNull(restartedService.getUser("admin"));
        assertFalse(restartedService.userExists("testuser"));
    }
}