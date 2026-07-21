package com.example._6su_team3;

public class RegistrationController {

    private final RegistrationService service;
    private final SessionManager sessionManager = new SessionManager();

    public RegistrationController() {
        this(new RegistrationService());
    }

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    public String registerUser(String username, String password) {
        try {
            service.register(username, password);
            return "SUCCESS";
        } catch (IllegalArgumentException exception) {
            return exception.getMessage();
        }
    }
    public String loginUser(String username, String password) {
        try {
            service.login(username, password, sessionManager);
            return "SUCCESS";
        } catch (IllegalArgumentException exception) {
            return exception.getMessage();
        }
    }
    public void logoutUser() {
        sessionManager.logout();
    }

    public boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn();
    }
    public String accessAdminFeature() {
        try {
            service.requireAdmin(sessionManager.getCurrentUser());
            return "ADMIN ACCESS GRANTED";
        } catch (SecurityException exception) {
            return exception.getMessage();
        }
    }
}

