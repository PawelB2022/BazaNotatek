package entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Categories")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")
    private int id;

    @Column(name = "CategoryName")
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    private Set<Note> notes = new HashSet<Note>();

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public Set<Note> getNotes()
    {
        return notes;
    }

    public void setNotes(Set<Note> notes)
    {
        this.notes = notes;
    }

    @Override
    public String toString()
    {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
