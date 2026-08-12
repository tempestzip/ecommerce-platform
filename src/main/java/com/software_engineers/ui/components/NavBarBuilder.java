package com.software_engineers.ui.components;

import com.software_engineers.service.CartService;
import com.software_engineers.service.LoginRegService;
import com.software_engineers.ui.view.BrowseView;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class NavBarBuilder {
    private final LoginRegService loginRegService;
    private final CartService cartService;
    private final BrowseView browseView;

    public NavBarBuilder(LoginRegService loginRegService, CartService cartService, BrowseView browseView) {
        this.loginRegService = loginRegService;
        this.cartService = cartService;
        this.browseView = browseView;
    }

    public Region defaultNavBar(String title, Runnable onActionTitle, Runnable onActionCart) {
        Region titleNav = buildTitleNav("Stationery Shop", onActionTitle);
        Region searchNav = createSearchNav(title);
        Region accountNav = createAccountNav();
        Region cartNav = createCartNav(onActionCart);

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
        searchButton.setOnAction(event -> browseView.updateToSearch(searchField.getText()));

        HBox searchNav = new HBox(searchField, searchButton);
        HBox.setHgrow(searchNav, Priority.ALWAYS);

        return searchNav;
    }

    private Region createAccountNav() {
        Hyperlink accountNav = new Hyperlink("Hello, " + loginRegService.getCurrentUsername());

        // Maybe implement Account screen
        // accountNav.setOnAction(null);

        return accountNav;
    }

    private Region createCartNav(Runnable onActionCart) {
        Hyperlink cartNav = new Hyperlink("Cart");
        cartNav.setOnAction(event -> onActionCart.run());

        return cartNav;
    }
}
