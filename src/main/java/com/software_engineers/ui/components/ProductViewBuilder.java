package com.software_engineers.ui.components;

import com.software_engineers.model.Product;
import com.software_engineers.service.CartService;
import com.software_engineers.service.LoginRegService;
import com.software_engineers.service.ProductService;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductViewBuilder {
    private final ProductService productService;
    private final CartService cartService;
    private final LoginRegService loginRegService;

    public ProductViewBuilder(ProductService productService, CartService cartService, LoginRegService loginRegService) {
        this.productService = productService;
        this.cartService = cartService;
        this.loginRegService = loginRegService;
    }

    public VBox defaultProductView(String search) {
        VBox vBox = new VBox();

        for (Product product : productService.searchProductsByName(search)) {
            Label newLab = new Label(product.getName() + " - $" + product.getPrice() + " | "
                    + product.getCategory() + " | " + product.getDescription());

            Button addToCartButton = new Button("Add to Cart");
            addToCartButton.setOnAction(
                    event -> cartService.addToCart(loginRegService.getCurrentUserId(), product.getId(), 1));

            HBox hBox = new HBox(newLab, addToCartButton);

            vBox.getChildren().add(hBox);
        }

        return vBox;
    }
}
