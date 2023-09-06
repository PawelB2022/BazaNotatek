package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import models.UserService;
import session.SessionInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangeUsernameController implements Initializable
{
    private UserService userService = new UserService();
    @FXML
    private Label currentUsernameLabel;

    private Stage stage;
    private Scene scene;
    @FXML
    private Parent root;

    private String username;

    @FXML
    private TextField txtNewUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label inputNotifier;

    public void setCurrentUsernameText(String username)
    {
        this.username = username;
        currentUsernameLabel.setText(username);
    }

    @FXML
    protected void changeUsernameAction(ActionEvent event)
    {
        try
        {
            String txtNewUsernameString = txtNewUsername.getText();
            if(txtNewUsernameString.isBlank() == false && txtPassword.getText().isBlank() == false)
            {

                if (userService.isLogin(username, txtPassword.getText()))
                {
                    //Sprawdzanie czy nazwa użytkownika nie jest zajęta
                    boolean doesUserExist = userService.doesUserExist(txtNewUsername.getText());
                    if (doesUserExist)
                    {
                        inputNotifier.setText("Ta nazwa użytkownika już istnieje.");
                    }
                    else
                    {
                        //Zmienianie nazwy uzytkownika
                        if(userService.changeUsername(username,txtNewUsernameString))
                        {
                            SessionInfo.getInstance().setUsername(txtNewUsernameString);
                            setCurrentUsernameText(SessionInfo.getInstance().getUsername());
                            inputNotifier.setText("Zmieniono nazwę na: " + txtNewUsernameString);
                        }
                        else inputNotifier.setText("Nie udało się zmienić nazwy użytkownika.");
//                        switchToUserMenu(event);
                    }

                } else
                {
                    inputNotifier.setText("Niewłaściwe hasło.");
                }
            }
            else
            {
                inputNotifier.setText("Nowa nazwa użytkownika i hasło nie mogą być puste.");
            }
        } catch (SQLException sqlE)
        {
            throw new RuntimeException(sqlE);
        }
    }

    @FXML
    protected void switchToUserMenu(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML files/UserMenu.fxml"));
        Parent newRoot = loader.load();
        scene = new Scene(newRoot);
        stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        setCurrentUsernameText(SessionInfo.getInstance().getUsername());
        //Ograniczenie znakow dla nazwy uzytkownika
        txtNewUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\w*"))
            {
                txtNewUsername.setText(oldValue); // Revert to the previous value
            }
        });

        inputNotifier.setText("");
    }
}
