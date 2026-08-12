package com.software_engineers.ui.components;

import com.software_engineers.service.LoginRegService;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class NavBarBuilder {
    private final LoginRegService service;

    public NavBarBuilder(LoginRegService service) {
        this.service = service;
    }

    public Region defaultNavBar(String title, Runnable onActionTitle) {
        Region titleNav = buildTitleNav("Stationery Shop", onActionTitle);
        Region searchNav = createSearchNav(title);
        Region accountNav = createAccountNav();
        Region cartNav = createCartNav();

        HBox navBar = new HBox(titleNav, searchNav, accountNav, cartNav);
        navBar.setSpacing(5);

        return navBar;
    }

    private Region buildTitleNav(String title, Runnable onActionTitle) {
        Hyperlink titleNav = new Hyperlink(title);
        titleNav.setOnAction(event -> onActionTitle.run());

        return titleNav;
    }

    private Region createSearchNav(String title) {
        TextField searchField = new TextField();
        searchField.setPromptText("Search " + title + "...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchButton = new Button("Search");

        HBox searchNav = new HBox(searchField, searchButton);
        HBox.setHgrow(searchNav, Priority.ALWAYS);

        return searchNav;
    }

    private Region createAccountNav() {
        Hyperlink accountNav = new Hyperlink("Hello, " + service.getCurrentUsername());

        // Maybe implement Account screen
        // accountNav.setOnAction(null);

        return accountNav;
    }

    private Region createCartNav() {
        Hyperlink cartNav = new Hyperlink("Cart");

        // TODO: Implement click

        return cartNav;
    }
}
