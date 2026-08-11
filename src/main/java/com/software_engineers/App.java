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

public class App extends Application {

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
        //int testUserID = userDAO.createUser("newcustomertest", "password123", "customer@test.com", "123 Test St");

        UserDAO userDao = new UserDAO();
        User testUser = userDao.getUserByUsername("newcustomertest");
        int testUserID = testUser.getId();
        

        CartDAO cartDAO = new CartDAO();
        //cartDAO.addToCart(testUserID, 1, 2);
       // cartDAO.addToCart(testUserID, 4, 1);
      //  cartDAO.addToCart(testUserID, 7, 1);
        List<Cart> cartItems = cartDAO.getCartItemsByUser(testUserID);

        GridPane cartGridPane = new GridPane();

        int cartRow = 0;
        double totalPrice = 0.0;
        for(Cart c : cartItems)
        {
            Product p = dao.getProductById(c.getProductId());
            Label cartLabel = new Label(p.getName() + " x" + c.getQuantity() + "- $" + (p.getPrice()*c.getQuantity()));
            totalPrice = totalPrice + (p.getPrice()*c.getQuantity());
            cartGridPane.add(cartLabel, 0, cartRow);
            cartRow++;
        }
        Label totalLabel = new Label("Total price of cart: $" + totalPrice);
        cartGridPane.add(totalLabel, 0, cartRow);


        GridPane gp = new GridPane();


        int row = 0;
                
        for(Product p : products)
        {
            //int id, String name, String description, double price, String category, int stock
            Label newLab = new Label(p.getName() + " - $" + p.getPrice() + " | " 
            + p.getCategory() + " | " + p.getDescription());
            gp.add(newLab, 0, row);   
            row++;
        }

        


        
        //Scene scene2 = new Scene(gp, 640, 480);
        //stage.setScene(scene2);

        Scene cartScene = new Scene(cartGridPane, 640, 480);
        stage.setScene(cartScene);
        stage.show();


    }

  

}

// ./mvnw clean javafx:run
