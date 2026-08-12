package com.software_engineers.ui.view;

import com.software_engineers.service.CartService;
import com.software_engineers.service.LoginRegService;
import com.software_engineers.service.ProductService;
import com.software_engineers.ui.components.NavBarBuilder;
import com.software_engineers.ui.components.ProductViewBuilder;

import javafx.scene.layout.VBox;

public class BrowseView extends VBox {
    private final LoginRegService loginRegService;
    private final ProductService productService;
    private final CartService cartService;

    public BrowseView(LoginRegService loginRegService, ProductService productService, CartService cartService,
            Runnable onActionTitle, Runnable onActionCart) {
        this.loginRegService = loginRegService;
        this.productService = productService;
        this.cartService = cartService;

        NavBarBuilder builder = new NavBarBuilder(this.loginRegService, this.cartService, this);
        ProductViewBuilder prodBuilder = new ProductViewBuilder(this.productService, this.cartService,
                this.loginRegService);

        this.getChildren().add(builder.defaultNavBar("Stationery Shop", onActionTitle, onActionCart));
        this.getChildren().add(prodBuilder.defaultProductView(""));

    }

    public void updateToSearch(String search) {
        this.getChildren().remove(1);

        ProductViewBuilder prodBuilder = new ProductViewBuilder(this.productService, this.cartService,
                this.loginRegService);
        this.getChildren().add(prodBuilder.defaultProductView(search));
    }
}
