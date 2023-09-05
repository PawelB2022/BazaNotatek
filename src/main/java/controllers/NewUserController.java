package controllers;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewUserController implements Initializable
{
    private UserService userService = new UserService();
    private Stage stage;
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Label inputNotifier;

    public void setInputNotifierText(String text)
    {
        inputNotifier.setText(text);
    }

    @FXML
    protected void addUserProcess()
    {
        try
        {
            //Sprawdzanie czy pola nie sa puste
            if(txtUsername.getText().isBlank() == false && txtPassword.getText().isBlank() == false)
            {
                //Sprawdzanie czy nazwa użytkownika nie jest zajęta
                boolean doesUserExist = userService.doesUserExist(txtUsername.getText());
                if (!doesUserExist)
                {
                    //Sprawdzanie czy pola z hasłami mają te same wartości
                    inputNotifier.setText("Tworzenie nowego użytkownika...");
                    boolean isPasswordsMatch = txtPassword.getText().equals(txtConfirmPassword.getText());
                    if (isPasswordsMatch)
                    {
                        User newUser = new User();
                        newUser.setName(txtUsername.getText());
                        newUser.setPassword(txtPassword.getText());
                        boolean isUserCreated = userService.createUser(newUser);
                        if (isUserCreated)  {inputNotifier.setText("Utworzono nowego użytkownika.");}
                        else                {inputNotifier.setText("Nie udało się utworzyć użytkownika. Spróbuj ponownie.");}
                    }
                    else
                    {
                        inputNotifier.setText("Wprowadzone hasła nie są takie same.");
                    }
                }
                else
                {
                    inputNotifier.setText("Użytkownik już istnieje. Wybierz inną nazwę.");
                }
            }
            else
            {
                inputNotifier.setText("Nazwa użytkownika i hasło nie mogą być puste.");
            }
        } catch (SQLException sqlE)
        {
            throw new RuntimeException(sqlE);
        }
    }
    @FXML
    protected void switchToLogInScene(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML files/LogInStart.fxml"));
        Parent newRoot = loader.load();
        scene = new Scene(newRoot);
        stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\w*"))
            {
                txtUsername.setText(oldValue); // Revert to the previous value
            }
        });
        inputNotifier.setText("");
    }
}
