package com.software_engineers.view;

import com.software_engineers.service.LoginRegService;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {
    private final LoginRegService service;

    public LoginView(LoginRegService service) {
        this.service = service;

        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

    }
}
