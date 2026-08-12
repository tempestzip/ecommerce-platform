package com.software_engineers.ui.view;

import com.software_engineers.service.LoginRegService;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class RegistrationView extends GridPane {
    private final LoginRegService service;
    private final Runnable onRegister;

    public RegistrationView(LoginRegService service, Runnable onRegister) {
        this.service = service;
        this.onRegister = onRegister;

        Label errorLbl = new Label("ERROR");
        errorLbl.setVisible(false);

        this.add(errorLbl, 1, 0);

        Label usernameLbl = new Label("Username: ");
        gridPaneAlignRight(usernameLbl);

        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");

        this.add(usernameLbl, 0, 1);
        this.add(usernameTextField, 1, 1);

        Label passwordLbl = new Label("Password: ");
        gridPaneAlignRight(passwordLbl);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        this.add(passwordLbl, 0, 2);
        this.add(passwordField, 1, 2);

        Label emailLbl = new Label("Email: ");
        gridPaneAlignRight(emailLbl);

        TextField emailField = new TextField();
        emailField.setPromptText("email@example.com");

        this.add(emailLbl, 0, 3);
        this.add(emailField, 1, 3);

        Label addressLbl = new Label("Address: ");
        gridPaneAlignRight(addressLbl);

        TextField addressField = new TextField();
        addressField.setPromptText("123 Something St");

        this.add(addressLbl, 0, 4);
        this.add(addressField, 1, 4);

        Button registerButton = new Button("Complete registration");
        registerButton.setOnAction(
                evt -> {
                    handleRegistration(usernameTextField, passwordField, emailField, addressField, errorLbl);
                });
        GridPane.setHalignment(registerButton, HPos.CENTER);

        this.add(registerButton, 1, 5);

        this.setHgap(5);
        this.setVgap(5);
        this.setAlignment(Pos.CENTER);
    }

    private void gridPaneAlignRight(Label passwordLbl) {
        GridPane.setHalignment(passwordLbl, HPos.RIGHT);
    }

    private void handleRegistration(TextField usernameField, PasswordField passwordField,
            TextField emailField, TextField addressField, Label errorLabel) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String address = addressField.getText();

        errorLabel.setVisible(false);

        try {
            if (this.service.register(username, password, email, address) != null) {
                onRegister.run();
            }
        } catch (Exception e) {
            errorLabel.setText(e.getLocalizedMessage());
            errorLabel.setVisible(true);
        }
    }
}
