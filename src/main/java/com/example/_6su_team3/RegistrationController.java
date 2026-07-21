package com.example._6su_team3;


public class RegistrationController {

    private RegistrationService service = new RegistrationService();

    public String registerUser(String username, String password, boolean isAdmin) {
        try {
            service.register(username, password, isAdmin);
            return "SUCCESS";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}

