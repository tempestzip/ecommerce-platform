package com.software_engineers.ui.view;

import com.software_engineers.service.LoginRegService;
import com.software_engineers.service.ProductService;
import com.software_engineers.ui.components.NavBarBuilder;

import javafx.scene.layout.VBox;

public class BrowseView extends VBox {
    private final LoginRegService loginRegService;
    private final ProductService productService;

    public BrowseView(LoginRegService loginRegService, ProductService productService, Runnable onActionTitle) {
        this.loginRegService = loginRegService;
        this.productService = productService;

        NavBarBuilder builder = new NavBarBuilder(this.loginRegService);

        this.getChildren().add(builder.defaultNavBar("Stationery Shop", onActionTitle));
    }
}
