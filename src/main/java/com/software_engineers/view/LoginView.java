package com.software_engineers.view;

import com.software_engineers.model.User;
import com.software_engineers.service.LoginRegService;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
