package com.example._6su_team3;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final RegistrationController controller = new RegistrationController();

    @FXML
    private void handleRegister() {

        String result = controller.registerUser(
                usernameField.getText(),
                passwordField.getText()
        );

        messageLabel.setText(result);

        if ("SUCCESS".equals(result)) {
            usernameField.clear();
            passwordField.clear();
        }
    }
    @FXML
    private void handleLogin() {

        String result = controller.loginUser(
                usernameField.getText(),
                passwordField.getText()
        );

        messageLabel.setText(result);

        if ("SUCCESS".equals(result)) {
            usernameField.clear();
            passwordField.clear();
        }
    }

    @FXML
    private void handleLogout() {
        controller.logoutUser();
        messageLabel.setText("Logged out successfully.");
    }
    @FXML
    private void handleAdminFeature() {
        String result = controller.accessAdminFeature();
        messageLabel.setText(result);
    }
}