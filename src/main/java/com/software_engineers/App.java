package com.software_engineers;

import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.software_engineers.database.ProductDAO;
import com.software_engineers.model.Product;
import com.software_engineers.database.DatabaseSetup;

public class App extends Application {
    private int curUserId;
    private Scene itemsScene;
    private Scene checkoutScene;

    Stage primaryStage;

    // private Region createProductPreview() {
    // }

    private Region createBrowsingRegion(List<Product> prodList) {
        VBox vBox = new VBox();

        for (int i = 0; i < (int) Math.ceil(prodList.size() / 5.0); ++i) {
            HBox hBox = new HBox();
            for (int j = 0; j < 5 && (i * 5) + j < prodList.size(); ++j) {
                hBox.getChildren().add(new TextField("" + ((i * 5) + j)));
            }
            vBox.getChildren().add(hBox);
        }
        return vBox;
    }

    private Region createTitleNav() {
        Hyperlink titleNav = new Hyperlink("Stationery Shop");

        return titleNav;
    }

    private Region createSearchNav() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search Stationery Shop...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchButton = new Button("Search");

        HBox searchNav = new HBox(searchField, searchButton);
        HBox.setHgrow(searchNav, Priority.ALWAYS);

        return searchNav;
    }

    private Region createSignInNav() {
        Hyperlink signIn = new Hyperlink("Hello, Sign in");

        return signIn;
    }

    private Region createCartNav() {
        Hyperlink cartNav = new Hyperlink("Cart");

        return cartNav;
    }

    private Region createNavBar() {
        HBox navBar = new HBox(createTitleNav(), createSearchNav(), createSignInNav(), createCartNav());

        navBar.setSpacing(5);

        return navBar;
    }

    public Region createStartScreen() {
        VBox startScreen = new VBox(createNavBar());

        return startScreen;
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        DatabaseSetup.initializeDatabase();

        ProductDAO productDAO = new ProductDAO();

        // @formatter:off
        // productDAO.createProduct("Pencil", "Used for writing on paper and can be dissolved with an eraser", 1.99, "Writing utensil", 100);
        // productDAO.createProduct("Ballpoint pen", "Everyday writing", 2.99, "Writing Utensils", 100);
        // productDAO.createProduct("Highlighter set (Pack of 6)", "Used to highlight on paper", 6.00, "Writing Utensils", 40);
        // productDAO.createProduct("Gel pen", "Used for writing on paper", 2.99, "Writing Utensils", 50);
        // productDAO.createProduct("Spiral Notebook", "Contains 100 pages and wide ruled", 2.99, "Notebooks", 70);
        // productDAO.createProduct("Composition Notebook", "Contains 200 pages and college ruled", 3.99, "Notebooks", 50);
        // productDAO.createProduct("Sticky Notes (Pack of 5)", "Sticks to paper", 2.99, "Paper Goods", 85);
        // productDAO.createProduct("Index Cards (100 count)", "3x5 inch and ruled", 2.99, "Paper Goods", 55);
        // productDAO.createProduct("Printer Paper (100 sheets)", "Used for printer", 3.99, "Paper Goods", 50);
        // productDAO.createProduct("Weekly Planner", "Track events and goals", 9.99, "Planners", 30);
        // productDAO.createProduct("File Folders (Pack of 5)", "Letter size", 3.99, "Folders", 50);
        // productDAO.createProduct("Stapler", "Puts multiple papers together", 5.99, "Desk Supplies", 35);
        // productDAO.createProduct("Scissors", "Stainless steel and cuts paper", 2.99, "Desk Supplies", 40);
        // productDAO.createProduct("Tape dispenser", "Holds tape", 3.99, "Desk Supplies", 35);
        // productDAO.createProduct("Eraser (Pack of 2)", "Used to erase pencil marks", 1.00, "Writing Utensils", 65);
        // productDAO.createProduct("Dry-erase Marker", "Used for whiteboards", 1.99, "Writing Utensils", 50);
        // @formatter:on

        List<Product> products = productDAO.getAllProducts();

        stage.setScene(new Scene(createStartScreen(), 1000, 1000));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// ./mvnw clean javafx:run
