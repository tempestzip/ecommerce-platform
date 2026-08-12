package com.software_engineers;

import java.util.List;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
//import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.software_engineers.database.ProductDAO;
import com.software_engineers.model.Cart;
import com.software_engineers.model.Product;
import com.software_engineers.model.User;
import com.software_engineers.database.CartDAO;
import com.software_engineers.database.DatabaseSetup;
//import com.software_engineers.database.CartDAO;
import com.software_engineers.database.UserDAO;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class App extends Application {
    private int curUserId;
    private Scene itemsScene;
    private Scene checkoutScene;

    @Override
    public void start(Stage stage) {
       
       // products.add(new Product("Pen", 2.99, "Used for writing on paper", 4.7));
       // products.add(new Product("Planner", 9.99, "Used for planning events", 4.5));
       // products.add(new Product("Eraser", 0.50, "Used for erasing pencil marks", 4.2));
       // products.add(new Product("Pencil", 1.99, "Used for writing on paper and can be erased with an eraser", 4.1));

       
        DatabaseSetup.initializeDatabase();
        

        ProductDAO dao = new ProductDAO();
       /*  dao.createProduct("Pencil", "Used for writing on paper and can be dissolved with an eraser", 1.99, "Writing utensil", 100);
        dao.createProduct("Ballpoint pen", "Everyday writing", 2.99, "Writing Utensils", 100);
        dao.createProduct("Highlighter set (Pack of 6)", "Used to highlight on paper", 6.00, "Writing Utensils", 40);
        dao.createProduct("Gel pen", "Used for writing on paper", 2.99, "Writing Utensils", 50);
        dao.createProduct("Spiral Notebook", "Contains 100 pages and wide ruled", 2.99, "Notebooks", 70);
        dao.createProduct("Composition Notebook", "Contains 200 pages and college ruled", 3.99, "Notebooks", 50);
        dao.createProduct("Sticky Notes (Pack of 5)", "Sticks to paper",  2.99, "Paper Goods", 85);
        dao.createProduct("Index Cards (100 count)", "3x5 inch and ruled", 2.99, "Paper Goods", 55);
        dao.createProduct("Printer Paper (100 sheets)", "Used for printer", 3.99, "Paper Goods", 50);
        dao.createProduct("Weekly Planner", "Track events and goals", 9.99, "Planners", 30);
        dao.createProduct("File Folders (Pack of 5)", "Letter size", 3.99, "Folders", 50);
        dao.createProduct("Stapler", "Puts multiple papers together", 5.99, "Desk Supplies", 35);
        dao.createProduct("Scissors", "Stainless steel and cuts paper", 2.99, "Desk Supplies", 40);
        dao.createProduct("Tape dispenser", "Holds tape", 3.99, "Desk Supplies", 35);
        dao.createProduct("Eraser (Pack of 2)", "Used to erase pencil marks", 1.00, "Writing Utensils", 65);
        dao.createProduct("Dry-erase Marker", "Used for whiteboards", 1.99, "Writing Utensils", 50);
        */
        List<Product> products = dao.getAllProducts();

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
            Button checkoutButton = new Button("Proceed to Checkout");
            cartGridPane.add(checkoutButton, 1, cartRow);
            checkoutButton.setOnAction(f2 -> {
                GridPane checkoutGridPane = new GridPane();

                Label checkoutTitle = new Label ("Checkout");
                checkoutGridPane.add(checkoutTitle, 0, 0);

                Label addressLabel = new Label("Shipping Address:");
                Label userAddressLabel = new Label(testUser.getAddress());
                checkoutGridPane.add(addressLabel, 0 , 1);
                checkoutGridPane.add(userAddressLabel, 1, 1);
 
                Label totalCheckoutLabel = new Label("Total: $" + roundTotal);
                checkoutGridPane.add(totalCheckoutLabel, 0, 2);

                Label cardInfoLabel = new Label("Card Number + CCV");
                checkoutGridPane.add(cardInfoLabel, 0, 3);

                TextField cardInfoBox1 = new TextField();
                checkoutGridPane.add(cardInfoBox1, 1, 3);
           
                TextField cardInfoBox2 = new TextField();
                checkoutGridPane.add(cardInfoBox2, 2, 3);

                Button placeOrderButton = new Button("Place Order");
                placeOrderButton.setOnAction(e2 -> {
                    GridPane orderConfirmationGridPane = new GridPane();

                    Label orderConfirmationLabel = new Label("Order Confirmed");
                    orderConfirmationGridPane.add(orderConfirmationLabel, 0, 0);

                    Label confirmationEmailLabel = new Label("Email sent to:" + testUser.getEmail());
                    orderConfirmationGridPane.add(confirmationEmailLabel, 0, 1);

                    Button cancelOrderButton = new Button("Cancel order");
                    orderConfirmationGridPane.add(cancelOrderButton, 0, 2);
                    cancelOrderButton.setOnAction(e3 -> {
                        Label orderCanceledLabel = new Label("Order has been canceled and refund has been issued! ");
                        orderConfirmationGridPane.add(orderCanceledLabel, 0, 3);
                        orderConfirmationGridPane.add(prevButton, 1, 2);
                    });


                    Scene orderConfirmation = new Scene(orderConfirmationGridPane, 640, 480);
                    stage.setScene(orderConfirmation);
                });
            checkoutGridPane.add(placeOrderButton, 0, 4);

            Button backButton = new Button("Back to Items");
            backButton.setOnAction(e4 -> stage.setScene(itemsScene));
            checkoutGridPane.add(backButton, 1, 4);
           
            Scene checkoutScene = new Scene(checkoutGridPane, 640, 480);
            stage.setScene(checkoutScene);
       
            });
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


    }

  

}

// ./mvnw clean javafx:run
