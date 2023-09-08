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
            Persistence.createEntityManagerFactory("BazaDanychNotatek");
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
        launch();
    }

    public static EntityManagerFactory getFactory() { return FACTORY; }
}
