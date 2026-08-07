package com.software_engineers;
import com.Product;

import java.util.ArrayList;
import java.util.List;

import com.Product;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
//import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import com.software_engineers.database.DatabaseSetup;

import java.util.List;
import java.util.ArrayList;

public class App extends Application {

    @Override
    public void start(Stage stage) {
       /* String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();*/

       /*  GridPane gridPane = new GridPane();
        Label label1 = new Label("Notebook - $4.99");
        gridPane.add(label1, 0, 0);

        Scene scene = new Scene(gridPane, 640, 480);
        stage.setScene(scene);
        stage.show();
        */

        List<Product> products = new ArrayList<>();

        products.add(new Product("Pen", 2.99, "Used for writing on paper", 4.7));
        products.add(new Product("Planner", 9.99, "Used for planning events", 4.5));
        products.add(new Product("Eraser", 0.50, "Used for erasing pencil marks", 4.2));
        products.add(new Product("Pencil", 1.99, "Used for writing on paper and can be erased with an eraser", 4.1));

        GridPane gp = new GridPane();

        int row = 0;
        
        for(Product p : products)
        {
            Label newLab = new Label(p.getName() + "- $" + p.getPrice());
            gp.add(newLab, 0, row);   
            row++;
        }
        Scene scene2 = new Scene(gp, 640, 480);
        stage.setScene(scene2);
        stage.show();


    }

    public static void main(String[] args) {
        DatabaseSetup.initializeDatabase();
        launch();
    }

}

// ./mvnw clean javafx:run
