package controllers;

import entities.Note;
import models.NoteTable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import models.NoteService;
import models.UserService;
import session.SessionInfo;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable
{
    private static final String [] searchOptions = {"Nazwa", "Data utworzenia", "Data modyfikacji"};
    private Stage stage;
    private Scene scene;
    @FXML
    private Parent root;

    private String username;

    @FXML
    private MenuButton userMenu;
    @FXML
    private TableView<NoteTable> tableView;
    @FXML
    private TableColumn<NoteTable, String> noteTitleColumn;
    @FXML
    private TableColumn<NoteTable, String> noteDateCreatedColumn;
    @FXML
    private TableColumn<NoteTable, String> noteDateModifiedColumn;
    @FXML
    private TableColumn<NoteTable, String> noteCategoryColumn;
    @FXML
    private ComboBox<String> comboSearchMethod;
    @FXML
    private TextField nameTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private MenuItem logOutMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private Button addNewNoteButton;

    private NoteService noteService = new NoteService();
    private UserService userService = new UserService();
    private ObservableList<NoteTable> list = FXCollections.observableArrayList();

    @FXML
    protected void populateTable()
    {
//        System.out.println("Username: " + this.username);
        list.clear();

        System.out.println("Dodawanie listy do tabeli...");
        List<Note> userNotesList = noteService.findAllByCurrentUserWithCategories(username);

        for(Note nT: userNotesList)
        {
            NoteTable newNote = new NoteTable(
                    nT.getId(),
                    nT.getTitle(),
                    nT.getCreatedAt(),
                    nT.getModifiedAt(),
                    nT.getCategories()
            );
//            System.out.println(newNote);
            list.add(newNote);
        }
    }

    @FXML
    protected void setTableCells()
    {
        noteTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        noteDateCreatedColumn.setCellValueFactory(cellData -> cellData.getValue().creationDateProperty());
        noteDateModifiedColumn.setCellValueFactory(cellData -> cellData.getValue().modifiedDateProperty());
        noteCategoryColumn.setCellValueFactory(cellData -> cellData.getValue().listOfCategoriesProperty());
    }

    public void setUsernameMenuText(String username)
    {
        this.username = username;
        userMenu.setText(username);
    }

    protected void onComboSearchMethodValueChanged()
    {
        String currentOption = comboSearchMethod.getValue();
        if(currentOption.equals(searchOptions[0]))
        {
            switchToNameTextField();
        }
        if(currentOption.equals(searchOptions[1]) ||
           currentOption.equals(searchOptions[2]))
        {
            switchToDatePicker();
        }
    }

    protected void switchToNameTextField()
    {
        nameTextField.setVisible(true);
        nameTextField.setDisable(false);
        nameTextField.toFront();

        datePicker.setVisible(false);
        datePicker.setDisable(true);
    }

    protected void switchToDatePicker()
    {
        datePicker.setVisible(true);
        datePicker.setDisable(false);
        datePicker.toFront();

        nameTextField.setVisible(false);
        nameTextField.setDisable(true);
    }

    @FXML
    protected void logOut(ActionEvent event) throws IOException
    {
        switchToLogInScene(event);
        SessionInfo.getInstance().clearSession();
    }

    @FXML
    protected void createNewNote(ActionEvent event) throws IOException
    {
        //NOTICE: Brak systemu sesji
        SessionInfo.getInstance().setNoteEditedBool(false);
        SessionInfo.getInstance().setEditedNoteID(-1);
        switchToNoteEditorScene(event);
    }

    @FXML
    protected void modifyNote(ActionEvent event) throws IOException
    {
        //NOTICE: Czy dziala poprawnie dla wielowierszowego zaznaczenia?
        int selectedNoteID = getSelectedRowNoteId();
        if(selectedNoteID > 0)
        {
            SessionInfo.getInstance().setNoteEditedBool(true);
            SessionInfo.getInstance().setEditedNoteID(selectedNoteID);
            switchToNoteEditorScene(event);
        }
        else
        {
            System.out.println("Modify: Brak zaznaczenia.");
        }
    }

    @FXML
    protected void deleteNote(ActionEvent event) throws IOException
    {
        int selectedNoteID = getSelectedRowNoteId();
        if(selectedNoteID > 0)
        {
            if (deleteNoteAlert(event) == true)
            {
                if(noteService.delete(selectedNoteID) == true)
                {
                    NoteTable selectedNote = tableView.getSelectionModel().getSelectedItem();
                    list.remove(selectedNote);
                }
            }
        }
        else
        {
            System.out.println("Delete: Brak zaznaczenia.");
        }
    }

    @FXML
    protected int getSelectedRowNoteId()
    {
        TableSelectionModel<NoteTable> selectionModel = tableView.getSelectionModel();
        if(selectionModel.isEmpty() == false)
        {
            NoteTable selectedNoteRow = selectionModel.getSelectedItem();
            int id = selectedNoteRow.getNoteID();
            System.out.println("selectedNoteRow: " + selectedNoteRow);
            return id;
        }
        else return -1;
    }

    @FXML
    protected boolean deleteNoteAlert(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuwanie notatki");
        alert.setHeaderText("Czy na pewno usunąć wybraną notatkę?");
//        alert.setContentText("Niezapisane zmiany zostaną utracone.");

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            return true;
        }
        else return false;
    }

    @FXML
    protected void deleteUserAndData(ActionEvent event) throws IOException
    {
        if(deleteUserAlert(event) == true)
        {
            userService.deleteUserAndDataService(SessionInfo.getInstance().getUserID());
            logOut(event);
            System.out.println("Usuniecie usera i danych");
        }
    }

    @FXML
    protected boolean deleteUserAlert(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuwanie profilu");
        alert.setHeaderText("Czy na pewno usunąć profil użytkonika: " + SessionInfo.getInstance().getUsername() + " ?");
        alert.setContentText("Wszystkie powiązane z tym profilem notatki także zostaną usunięte.");

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            return true;
        }
        else return false;
    }

    @FXML
    protected void switchToLogInScene(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML files/LogInStart.fxml"));
        System.out.println("Wylogowywanie...");
        Parent newRoot = loader.load();
        scene = new Scene(newRoot);
        stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);

        LoginController loginController = loader.getController();
        loginController.setInputNotifierText("Wylogowano: " + this.username);

        stage.show();
    }

    @FXML
    protected void switchToNoteEditorScene(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML files/NoteEditor.fxml"));
        Parent newRoot = loader.load();
        scene = new Scene(newRoot);
        stage = (Stage)root.getScene().getWindow();

        NoteEditorController noteEditorController = loader.getController();
        noteEditorController.setStage(stage);
        if(SessionInfo.getInstance().isNoteEditedBool() == true)
        {
            noteEditorController.setupEditor( noteService.findOne(SessionInfo.getInstance().getEditedNoteID()) );
        }

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void switchToChangeUsernameScene(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML files/ChangeUsername.fxml"));
        Parent newRoot = loader.load();
        scene = new Scene(newRoot);
        stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
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
    public void initialize(URL location, ResourceBundle resources)
    {
        setUsernameMenuText(SessionInfo.getInstance().getUsername());
        comboSearchMethod.getItems().addAll(searchOptions);
        comboSearchMethod.setValue("Nazwa");
        switchToNameTextField();

        //Wyrazenie lambda odpowiedzialne za wykrywanie zmiany wartosci dla comboSearchMethod
        comboSearchMethod.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) ->
        {
            onComboSearchMethodValueChanged();
        });

        setTableCells();

        tableView.setItems(list);
        populateTable();
    }
}
