package com.example._6su_team3;

public class RegistrationController {

    private final RegistrationService service;

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
}

