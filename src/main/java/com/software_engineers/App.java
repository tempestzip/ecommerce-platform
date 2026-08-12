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
<<<<<<< HEAD
=======
//import com.software_engineers.database.CartDAO;
import com.software_engineers.database.UserDAO;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
>>>>>>> ProductService

public class App extends Application {
    private int curUserId;
    private Scene itemsScene;

    Stage primaryStage;

    // private Region createProductPreview() {
    // }

    private Region createBrowsingRegion(List<Product> prodList) {
        VBox vBox = new VBox();

<<<<<<< HEAD
        for (int i = 0; i < (int) Math.ceil(prodList.size() / 5.0); ++i) {
            HBox hBox = new HBox();
            for (int j = 0; j < 5 && (i * 5) + j < prodList.size(); ++j) {
                hBox.getChildren().add(new TextField("" + ((i * 5) + j)));
            }
            vBox.getChildren().add(hBox);
        }
        return vBox;
=======
        //UserDAO userDAO = new UserDAO();
        //int curUserId = userDAO.createUser("newcustomertest", "password123", "customer@test.com", "123 Test St");

        UserDAO userDao = new UserDAO();
        User testUser = userDao.getUserByUsername("newcustomertest");
        curUserId = testUser.getId();
        

        CartDAO cartDAO = new CartDAO();
        //cartDAO.addToCart(curUserId, 1, 2);
       // cartDAO.addToCart(curUserId, 4, 1);
      //  cartDAO.addToCart(curUserId, 7, 1);
        //cartDAO.addToCart(curUserId, 13, 1);
        //cartDAO.addToCart(curUserId, 1, 1);
        
        

        GridPane gp = new GridPane();
        

        Button viewCartButton = new Button("View Cart");
        //(e -> stage.setScene(cartScene));
        viewCartButton.setOnAction(e -> {
            List<Cart> cartItems = cartDAO.getCartItemsByUser(curUserId);
            GridPane cartGridPane = new GridPane();

            int cartRow = 0;
            double totalPrice = 0.0;
            for(Cart c : cartItems)
            {
                Product p = dao.getProductById(c.getProductId());
                Label cartLabel = new Label(p.getName() + " (x" + c.getQuantity() + ")- $" + (p.getPrice()*c.getQuantity()));
                totalPrice = totalPrice + (p.getPrice()*c.getQuantity());
                cartGridPane.add(cartLabel, 0, cartRow);
                cartRow++;
            }
            double roundTotal = Math.round(totalPrice * 100.0)/100.0;
            Label totalLabel = new Label("Total price of cart: $" + roundTotal);
            cartGridPane.add(totalLabel, 0, cartRow);

            Button prevButton = new Button("Back to Products");
            prevButton.setOnAction(f -> stage.setScene(itemsScene));
            cartRow++;
            cartGridPane.add(prevButton, 0, cartRow);
            Scene cartScene = new Scene(cartGridPane, 640, 480);
            stage.setScene(cartScene);

        
        });


        int row = 0;
                
        for(Product p : products)
        {
            //int id, String name, String description, double price, String category, int stock
            Label newLab = new Label(p.getName() + " - $" + p.getPrice() + " | " 
            + p.getCategory() + " | " + p.getDescription());

            Button addToCartButton = new Button("Add to Cart");
            addToCartButton.setOnAction(e ->{
                cartDAO.addToCart(curUserId, p.getId(), 1);

            });
            gp.add(newLab, 0, row);   
            gp.add(addToCartButton, 1, row);
            row++;
        }
        gp.add(viewCartButton, 0, row);

       // for (Cart c : cartItems) {
       //     System.out.println(c.getId() + ": product " + c.getProductId() + ", qty " + c.getQuantity());
       // }

       GridPane loginGP = new GridPane();

       Label usernameLbl = new Label("Username: ");
       TextField usernameField = new TextField();

       Label passwordLbl = new Label("Password: ");
       PasswordField passwordField = new PasswordField();

       Button loginButton = new Button("Login");

       loginGP.add(usernameLbl, 0, 0);
       loginGP.add(usernameField, 1, 0);

       loginGP.add(passwordLbl, 0, 1);
       loginGP.add(passwordField,1,1);
        
       loginGP.add(loginButton, 0,2);

       loginButton.setOnAction(e ->{
        String username = usernameField.getText();
        String password = passwordField.getText();

        userDao.verifyLogin(username, password);

        if (true)
        {
            User theUser = userDao.getUserByUsername(username);
            curUserId = theUser.getId();
            cartDAO.clearCart(curUserId);
            itemsScene = new Scene(gp, 640, 480);
            stage.setScene(itemsScene);

        }
        
       });


        Scene loginScene = new Scene(loginGP, 640, 480);
        stage.setScene(loginScene);
        stage.show();


>>>>>>> ProductService
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
