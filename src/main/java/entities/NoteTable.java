package entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class NoteTable
{
    private SimpleIntegerProperty noteID = new SimpleIntegerProperty(0);
    private SimpleStringProperty title = new SimpleStringProperty("");
    private SimpleStringProperty creationDate = new SimpleStringProperty("");
    private SimpleStringProperty modifiedDate = new SimpleStringProperty("");

    private SimpleStringProperty listOfCategories = new SimpleStringProperty("");

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public NoteTable()
    {
    }

    public NoteTable(int noteID, String title, LocalDateTime creationDate, LocalDateTime modifiedDate, Set<Category> listOfCategories)
    {
        setNoteID(noteID);
        setTitle(title);
        setCreationDate(creationDate.format(formatter));
        setModifiedDate(modifiedDate.format(formatter));

        if( !listOfCategories.isEmpty() )
        {
            StringBuilder stringCategoryBuilder = new StringBuilder();
            int index = 0;
            for(Category c : listOfCategories)
            {
                stringCategoryBuilder.append(c.getCategoryName());
                if(!(index == listOfCategories.size() - 1)) stringCategoryBuilder.append(", ");
                index++;
            }
            setListOfCategories(stringCategoryBuilder.toString());
        }
        else setListOfCategories("N/A");
    }

    public int getNoteID()
    {
        return noteID.get();
    }

    public SimpleIntegerProperty noteIDProperty()
    {
        return noteID;
    }

    public void setNoteID(int noteID)
    {
        this.noteID.set(noteID);
    }

    public String getTitle()
    {
        return title.get();
    }

    public SimpleStringProperty titleProperty()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public String getCreationDate()
    {
        return creationDate.get();
    }

    public SimpleStringProperty creationDateProperty()
    {
        return creationDate;
    }

    public void setCreationDate(String creationDate)
    {
        this.creationDate.set(creationDate);
    }

    public String getModifiedDate()
    {
        return modifiedDate.get();
    }

    public SimpleStringProperty modifiedDateProperty()
    {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate)
    {
        this.modifiedDate.set(modifiedDate);
    }

    public String getListOfCategories()
    {
        return listOfCategories.get();
    }

    public SimpleStringProperty listOfCategoriesProperty()
    {
        return listOfCategories;
    }

    public void setListOfCategories(String listOfCategories)
    {
        this.listOfCategories.set(listOfCategories);
    }

    @Override
    public String toString()
    {
        return "NoteTable{" +
                "noteID=" + noteID +
                ", title=" + title +
                ", creationDate=" + creationDate +
                ", modifiedDate=" + modifiedDate +
                ", listOfCategories=" + listOfCategories +
                '}';
    }
}
