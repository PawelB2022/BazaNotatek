package controllers;

import entities.Category;
import entities.Note;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.CategoryService;
import models.NoteService;
import models.UserService;
import session.SessionInfo;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class NoteEditorController implements Initializable
{
    private Stage stage;
    private Scene scene;
    @FXML
    private Parent root;

    NoteService noteService = new NoteService();
    UserService userService = new UserService();

    @FXML
    private Label inputNotifier;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea textArea;
    @FXML
    private MenuItem saveNoteMenuItem;
    @FXML
    private MenuItem decreaseFontMenuItem;
    @FXML
    private MenuItem increaseFontMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private Menu categoriesMenu;

    CategoryService categoryService = new CategoryService();

    public void setStage(Stage stage)
    {
        this.stage = stage;
        //NOTICE: Ustawia okno wyboru na stale
        stage.setOnCloseRequest(event ->
        {
            try
            {
                closeWindowAlert(event);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void setupEditor(Note editedNote)
    {
        if( (editedNote == null) == false )
        {
            titleTextField.setText(editedNote.getTitle());
            textArea.setText(editedNote.getContent());
            //TODO: Zaznaczyć kategorie
        }
    }

    protected void assignMenuItemValues()
    {
        ObservableList<MenuItem> items = categoriesMenu.getItems();
        items.get(0).setUserData(categoryService.findOne(1));
        items.get(1).setUserData(categoryService.findOne(2));
        items.get(2).setUserData(categoryService.findOne(3));
    }

    @FXML
    protected void closeAlert(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zamykanie edytora");
        alert.setHeaderText("Wyjść z edytora?");
        alert.setContentText("Niezapisane zmiany zostaną utracone.");

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            switchToUserMenu(event);
        }
    }

    @FXML
    protected void newNoteAlert(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Otwieranie nowej notatki");
        alert.setHeaderText("Otworzyć nową notatkę?");
        alert.setContentText("Niezapisane zmiany zostaną utracone.");

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            resetEditorToNew();
        }
    }

    @FXML
    protected void closeWindowAlert(WindowEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zamykanie edytora");
        alert.setHeaderText("Wyjść z aplikacji?");
        alert.setContentText("Zostaniesz wylogowany i niezapisane zmiany zostaną utracone.");

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            SessionInfo.getInstance().clearSession();
            stage.close();
        }
        else
        {
            event.consume();
        }
    }

    @FXML
    protected void resetEditorToNew()
    {
        titleTextField.setText("");
        textArea.setText("");
        for (MenuItem item : categoriesMenu.getItems())
        {
            if(item instanceof CheckMenuItem) ((CheckMenuItem) item).setSelected(false);
        }
        SessionInfo.getInstance().setNoteEditedBool(false);
        SessionInfo.getInstance().setEditedNoteID(-1);
        inputNotifier.setText("Tworzenie nowej notatki.");
    }

    @FXML
    protected void saveNoteAction()
    {
        if(SessionInfo.getInstance().isNoteEditedBool() == false) createNewNote();
        else updateNote();
    }

    @FXML
    protected void createNewNote()
    {
        String titleString = titleTextField.getText();
        //Sprawdzamy czy obecna notatka nie jest edycja juz istniejacej
        if(SessionInfo.getInstance().getEditedNoteID() < 0 == true)
        {
            Note newNote = new Note();
            if (titleString.isBlank())  newNote.setTitle("default");
            else newNote.setTitle(titleString);
            newNote.setUser( (userService.findOne(SessionInfo.getInstance().getUserID())) );
            newNote.setCreatedAt(LocalDateTime.now());
            newNote.setModifiedAt(LocalDateTime.now());
            newNote.setContent(textArea.getText());
            //Dodawanie i usuwanie kategorii
            for (MenuItem item : categoriesMenu.getItems())
            {
                if(item instanceof CheckMenuItem)
                {
                    CheckMenuItem checkItem = ((CheckMenuItem) item);
//                    System.out.println("Czy CheckMenuItem jest zaznaczony?: " + checkItem.isSelected());
                    if(checkItem.isSelected() == true)
                    {
                        newNote.addCategory((Category) checkItem.getUserData());

                    }
                }
            }

            System.out.println("Nowo stworzona notaka:" + newNote);
            if(noteService.createNew(newNote))
            {
                inputNotifier.setText("Zapisano nową notatkę pod nazwą: " + titleTextField.getText());
            }
            else
            {
                inputNotifier.setText("Zapis nowej notatki się nie powiódł.");
            }
        }
        //Kod dla notatki juz istniejacej
        else
        {
            inputNotifier.setText("Błąd przy zapisie: Notatka oznaczona istniejącym numerem ID.");
        }
    }

    @FXML
    protected void updateNote()
    {
        String titleString = titleTextField.getText();
        if(SessionInfo.getInstance().getEditedNoteID() < 0 == false)
        {
            Note editedNote = new Note();
            if (titleString.isBlank())  editedNote.setTitle("default");
            else                        editedNote.setTitle(titleString);
            editedNote.setModifiedAt(LocalDateTime.now());
            editedNote.setContent(textArea.getText());
            //Dodawanie i usuwanie kategorii
            for (MenuItem item : categoriesMenu.getItems())
            {
                if(item instanceof CheckMenuItem)
                {
                    CheckMenuItem checkItem = ((CheckMenuItem) item);
                    if(checkItem.isSelected() == true)
                    {
                        editedNote.addCategory((Category) checkItem.getUserData());
                    }
                }
            }

            System.out.println("Edytowana notaka:" + editedNote);
            if(noteService.update(SessionInfo.getInstance().getEditedNoteID(),editedNote))
            {
                inputNotifier.setText("Zapisano pod nazwą: " + titleTextField.getText());
            }
            else
            {
                inputNotifier.setText("Zapis edytowanej notatki się nie powiódł.");
            }
        }
        else
        {
            inputNotifier.setText("Błąd przy zapisie: Notatka oznaczona pustym lub negatywnym numerem ID.");
        }
    }

    @FXML
    protected void switchToUserMenu(ActionEvent event) throws IOException
    {
        //Resetuje okienko ostrzegajace o zamknieciu programu.
        stage.setOnCloseRequest(null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML files/UserMenu.fxml"));
        Parent newRoot = loader.load();
        scene = new Scene(newRoot);
        stage = (Stage)root.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void smallerFont(ActionEvent actionEvent) {
        double size = textArea.getFont().getSize();
        textArea.setFont(Font.font(size - 2));
    }


    @FXML
    public void biggerFont(ActionEvent actionEvent) {
        double size = textArea.getFont().getSize();
        textArea.setFont(Font.font(size + 2));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if(SessionInfo.getInstance().isNoteEditedBool() == false)
        {
            inputNotifier.setText("Tworzenie nowej notatki.");
        }
        else
        {
            inputNotifier.setText("Edycja istniejacej notatki.");
            //TODO: Pobieranie informacji o istniejacej notatce
        }
        assignMenuItemValues();
    }

}
