package entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Notes")
public class Note
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NoteID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "Date_created")
    private LocalDateTime createdAt;

    @Column(name = "Date_modified")
    private LocalDateTime modifiedAt;

    @Column(name = "Title")
    private String title;

    @Column(name = "Content")
    private String content;

    @ManyToMany
    @JoinTable(name = "Notes_Categories",
            joinColumns = @JoinColumn(name = "NoteID"),
            inverseJoinColumns = @JoinColumn(name = "CategoryID") )
    private Set<Category> categories = new HashSet<Category>();

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt()
    {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt)
    {
        this.modifiedAt = modifiedAt;
    }

    public Set<Category> getCategories()
    {
        return categories;
    }

    public void setCategories(Set<Category> categories)
    {
        this.categories = categories;
    }

    public void addCategory(Category cat)
    {
        this.categories.add(cat);
        cat.getNotes().add(this);
    }

    public void removeCategory(Category cat)
    {
        this.categories.remove(cat);
        cat.getNotes().remove(this);
    }

    @Override
    public String toString()
    {
        return "Note{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
