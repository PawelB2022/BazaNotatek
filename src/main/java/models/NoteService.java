package models;

import controllers.Main;
import entities.Category;
import entities.Note;
import session.SessionInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NoteService
{
    private EntityManagerFactory managerFactory = Main.getFactory();

    public List<Note> findAllByCurrentUser(String currentUser)
    {
        List<Note> notes = new ArrayList<>();
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createQuery("SELECT a.title, a.createdAt, a.modifiedAt FROM Note a JOIN User b ON a.user = b.id WHERE b.name = :currentUser")
                    .setParameter("currentUser", currentUser);
            List<Object[]> rows = query.getResultList();
//            System.out.println("Result list: " + query.getResultList());

            for(Object[] row : rows)
            {
                Note note = new Note();
                note.setTitle(row[0].toString());
                note.setCreatedAt(LocalDateTime.parse(row[1].toString()));
                note.setModifiedAt(LocalDateTime.parse(row[2].toString()));
                notes.add(note);
            }

            transaction.commit();
        } catch (Exception e)
        {
            if(transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
        return notes;
    }

    public List<Note> findAllByCurrentUserWithCategories(String currentUser)
    {
        List<Note> notes = new ArrayList<>();
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createQuery("SELECT a.title, a.createdAt, a.modifiedAt, a.id FROM Note a JOIN User b ON a.user = b.id WHERE b.name = :currentUser")
                    .setParameter("currentUser", currentUser);
            List<Object[]> rows = query.getResultList();
//            System.out.println("Result list: " + rows);

            for(Object[] row : rows)
            {
                Note note = new Note();
                note.setTitle(row[0].toString());
                note.setCreatedAt(LocalDateTime.parse(row[1].toString()));
                note.setModifiedAt(LocalDateTime.parse(row[2].toString()));
                int noteId = ((Integer) row[3]).intValue();
                note.setId(noteId);

                Query subQuery = entityManager.createQuery("SELECT c FROM Category c LEFT JOIN c.notes a WHERE a.id = :id")
                        .setParameter("id", noteId);
                List<Object[]> subRows = subQuery.getResultList();

                Set<Category> filledCategorySet = note.getCategories();
                for(Object categoryRow : subRows)
                {
                    Category categoryCast = (Category) categoryRow;
                    filledCategorySet.add(categoryCast);
                }
                notes.add(note);
            }

            transaction.commit();
        } catch (Exception e)
        {
            if(transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }
        return notes;
    }

    public Note findOne(int id)
    {
        Note note = null;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            note = entityManager.find(Note.class,id);

            transaction.commit();
        } catch (Exception e)
        {
            if(transaction != null) transaction.rollback();
        }
        return note;
    }

    public boolean createNew(Note note)
    {
        boolean result = true;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(note);

            transaction.commit();
        } catch (Exception e)
        {
            result = false;
            if(transaction != null) transaction.rollback();
        }

        //Ustawianie notatki jako obecnie edytowanej //NOTICE: Niepotymalne, gdyz moze byc to niepozadane dzialanie
        if (result == true)
        {
            SessionInfo.getInstance().setNoteEditedBool(true);
            SessionInfo.getInstance().setEditedNoteID(note.getId());
        }
        return result;
    }

    public boolean update(int id, Note updatedNote)
    {
        boolean result = true;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            Note note = entityManager.find(Note.class,id);

            transaction = entityManager.getTransaction();
            transaction.begin();

            note.setTitle(updatedNote.getTitle());
            note.setModifiedAt(updatedNote.getModifiedAt());
            note.setContent(updatedNote.getContent());
            note.setCategories(updatedNote.getCategories());
            entityManager.merge(note);

            transaction.commit();
        } catch (Exception e)
        {
            result = false;
            if(transaction != null) transaction.rollback();
        }
        return result;
    }
}
