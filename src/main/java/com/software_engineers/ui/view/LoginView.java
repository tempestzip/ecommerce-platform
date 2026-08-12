package com.software_engineers.ui.view;

import com.software_engineers.service.LoginRegService;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * LoginView for UI. Refactored and modified code written by
 *
 * @author Faith
 */
public class LoginView extends GridPane {
    private final LoginRegService service;
    private final Runnable onLogin;
    private final Runnable onSelectRegister;

    public LoginView(LoginRegService service, Runnable onLogin, Runnable onSelectRegister) {
        this.service = service;
        this.onLogin = onLogin;
        this.onSelectRegister = onSelectRegister;

        Label errorLbl = new Label("ERROR");
        errorLbl.setVisible(false);

        this.add(errorLbl, 1, 0);

        Label usernameLbl = new Label("Username:");
        GridPane.setHalignment(usernameLbl, HPos.RIGHT);

        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");

        this.add(usernameLbl, 0, 1);
        this.add(usernameTextField, 1, 1);

        Label passwordLbl = new Label("Password:");
        GridPane.setHalignment(passwordLbl, HPos.RIGHT);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        this.add(passwordLbl, 0, 2);
        this.add(passwordField, 1, 2);

        Button loginButton = new Button("Login");
        loginButton.setMinWidth(180);
        loginButton.setOnAction(event -> handleLogin(usernameTextField, passwordField, errorLbl));
        GridPane.setHalignment(loginButton, HPos.CENTER);

        this.add(loginButton, 1, 3);

        Button registerButton = new Button("Register");
        registerButton.setMinWidth(180);
        registerButton.setOnAction(event -> onSelectRegister.run());
        GridPane.setHalignment(registerButton, HPos.CENTER);

        this.add(registerButton, 1, 4);

        this.setHgap(5);
        this.setVgap(5);
        this.setAlignment(Pos.CENTER);
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField, Label errorLabel) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        errorLabel.setVisible(false);

        try {
            if (this.service.login(username, password)) {
                onLogin.run();
            } else {
                errorLabel.setText("Incorrect login information");
                errorLabel.setVisible(true);
            }
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }
}
