package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class Main extends Application
{

    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory("TestUnit");
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/FXML files/LogInStart.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Baza notatek.");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
//        System.out.println("Lista produktów:");
//        ProductService productService = new ProductService();
//        for(Product p : productService.findAll())
//        {
//            System.out.println(p);
//        }
//        System.out.println("\nLista produktów z ceną pomiędzy 5 a 15:");
//        for(Product p : productService.search(5,15))
//        {
//            System.out.println(p);
//        }
//
//        System.out.println("\nProdukt z id 3:");
//        System.out.println(productService.findOne(3));
//
//        UserService userService = new UserService();
//        System.out.println("\nUzytkownik z id 1:");
//        System.out.println(userService.findOne(1));

        launch();
    }

    public static EntityManagerFactory getFactory() { return FACTORY; }
}
