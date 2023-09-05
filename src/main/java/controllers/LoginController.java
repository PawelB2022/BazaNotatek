package controllers;

import entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.UserService;
import session.SessionInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    public UserService userService = new UserService();
    private Stage stage;
    private Scene scene;

    @FXML
    private Parent root;
    @FXML
    private Pane rightPane;
    @FXML
    private Label inputNotifier;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Hyperlink hyperlinkNewUser;
    @FXML
    private Button logInButton;

    public void setInputNotifierText(String text)
    {
        inputNotifier.setText(text);
    }
    @FXML
    protected void checkLogin(ActionEvent event)
    {
        try
        {
            String txtUsernameString = txtUsername.getText();
            if(txtUsernameString.isBlank() == false && txtPassword.getText().isBlank() == false)
            {
                if (userService.isLogin(txtUsernameString, txtPassword.getText()))
                {
                    User userInfo = userService.findOneByUsername(txtUsernameString);
                    SessionInfo.getInstance().setUserID(userInfo.getId());
                    SessionInfo.getInstance().setUsername(userInfo.getName());
                    switchToUserMenu(event);
                } else
                {
                    inputNotifier.setText("Niewłaściwy użytkownik lub hasło albo użytkownik nie istnieje.");
                }
            }
            else
            {
                inputNotifier.setText("Nazwa użytkownika i hasło nie mogą być puste.");
            }
        } catch (SQLException sqlE)
        {
            throw new RuntimeException(sqlE);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void switchToNewUserScene(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML files/NewUser.fxml"));
        //root = FXMLLoader.load(getClass().getResource("/FXML files/NewUser.fxml"));

        Parent newRoot = loader.load();
        scene = new Scene(newRoot);
        stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchToUserMenu(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML files/UserMenu.fxml"));
        Parent newRoot = loader.load();
        scene = new Scene(newRoot);
        stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);

//        UserMenuController userMenuController = loader.getController();
//        userMenuController.setUsernameMenuText(SessionInfo.getInstance().getUsername());

        stage.show();
    }

    @FXML
    protected void closeApp(ActionEvent event) throws IOException
    {
        //Alternatywna metoda zamykania
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        stage.close();
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        inputNotifier.setText("");
        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\w*"))
            {
                txtUsername.setText(oldValue); // Revert to the previous value
            }
        });
    }
}
