package com.software_engineers.view;

import com.software_engineers.model.User;
import com.software_engineers.service.LoginRegService;

import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * LoginView for UI. Refactored and modified code written by
 *
 * @author Faith
 */
public class LoginView extends VBox {
    private final LoginRegService service;
    private final Runnable onLogin;

    public LoginView(LoginRegService service, Runnable onLogin) {
        this.service = service;
        this.onLogin = onLogin;

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        Label errorLbl = new Label("ERROR");
        errorLbl.setVisible(false);

        grid.add(errorLbl, 1, 0);

        Label usernameLbl = new Label("Username: ");
        usernameLbl.setAlignment(Pos.CENTER_RIGHT);

        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        GridPane.setHalignment(usernameTextField, HPos.RIGHT);

        grid.add(usernameLbl, 0, 1);
        grid.add(usernameTextField, 1, 1);

        Label passwordLbl = new Label("Password: ");
        passwordLbl.setAlignment(Pos.CENTER_RIGHT);
        GridPane.setHalignment(passwordLbl, HPos.RIGHT);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(passwordLbl, 0, 2);
        grid.add(passwordField, 1, 2);

        Button loginButton = new Button("Login");
        loginButton.setMinWidth(180);
        loginButton.setOnAction(event -> handleLogin(usernameTextField, passwordField, errorLbl));
        GridPane.setHalignment(loginButton, HPos.CENTER);

        grid.add(loginButton, 1, 3);

        Button registerButton = new Button("Register");
        registerButton.setMinWidth(180);
        GridPane.setHalignment(registerButton, HPos.CENTER);

        grid.add(registerButton, 1, 4);

        this.getChildren().add(grid);
        this.setAlignment(Pos.CENTER);
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField, Label errorLabel) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        errorLabel.setVisible(false);

        try {
            if (service.login(username, password)) {
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

    // @formatter:off
    // Original author: Faith
    // void rand() {
    //     GridPane loginGP = new GridPane();

    //     Label usernameLbl = new Label("Username: ");
    //     TextField usernameField = new TextField();

    //     Label passwordLbl = new Label("Password: ");
    //     PasswordField passwordField = new PasswordField();

    //     Button loginButton = new Button("Login");

    //     loginGP.add(usernameLbl, 0, 0);
    //     loginGP.add(usernameField, 1, 0);

    //     loginGP.add(passwordLbl, 0, 1);
    //     loginGP.add(passwordField, 1, 1);

    //     loginGP.add(loginButton, 0, 2);

    //     loginButton.setOnAction(e -> {
    //         String username = usernameField.getText();
    //         String password = passwordField.getText();

    //         userDao.verifyLogin(username, password);

    //         if (true) {
    //             User theUser = userDao.getUserByUsername(username);
    //             curUserId = theUser.getId();
    //             cartDAO.clearCart(curUserId);
    //             itemsScene = new Scene(gp, 640, 480);
    //             stage.setScene(itemsScene);

    //         }

    //     });

    //     Scene loginScene = new Scene(loginGP, 640, 480);
    //     stage.setScene(loginScene);
    //     stage.show();
    // }
    // @formatter:on
}
