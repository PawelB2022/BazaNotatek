package models;

import controllers.Main;
import entities.Note;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.SQLException;

public class UserService
{
    private EntityManagerFactory managerFactory = Main.getFactory();

    public User findOne(int id)
    {
        User user = null;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            user = entityManager.find(User.class,id);

            transaction.commit();
        } catch (Exception e)
        {
            if(transaction != null) transaction.rollback();
        }
        return user;
    }

    public User findOneByUsername(String username)
    {
        User user = null;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            user = new User();

            transaction.begin();

            Query query = entityManager.createQuery("SELECT a.id, a.name FROM User a WHERE lower(a.name) = :name")
                    .setParameter("name",username);
            Object[] returnedQuery = (Object[]) query.getSingleResult();

            user.setId( ((Integer)returnedQuery[0]).intValue() );
            user.setName(returnedQuery[1].toString());

            transaction.commit();
        } catch (Exception e)
        {
            if(transaction != null) transaction.rollback();
        }
        return user;
    }

//    public User findOneByUsername(String username)
//    {
//        User user = null;
//        EntityManager entityManager;
//        EntityTransaction transaction = null;
//        try
//        {
//            entityManager = managerFactory.createEntityManager();
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//
//            Query query = entityManager.createQuery("SELECT a.id, a.name FROM User a WHERE lower(a.name) = :name",User.class)
//                    .setParameter("name",username);
//            Object[] returnedQuery = (Object[]) query.getSingleResult();
//
//            User searchedUser = new User();
//            searchedUser.setId(((Integer) returnedQuery[0]).intValue());
//            searchedUser.setName(returnedQuery[1].toString());
//
//            user = searchedUser;
//
//            transaction.commit();
//        } catch (Exception e)
//        {
//            if(transaction != null) transaction.rollback();
//        }
//        return user;
//    }

    public boolean doesUserExist(String username) throws SQLException
    {
        boolean result = false;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            username = username.toLowerCase();
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createQuery("SELECT a.name FROM User a WHERE lower(a.name) = :name",User.class)
                    .setParameter("name",username);

            Object[] returnedQuery = (Object[]) query.getSingleResult();
            if(returnedQuery[0] != null)    {result = true;}
            else                            {result = false;}

            transaction.commit();
        } catch (Exception e)
        {
            result = false;
            if(transaction != null) transaction.rollback();
        }
        return result;
    }

    public boolean changeUsername(String username, String newUsername) throws SQLException
    {
        boolean result = true;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            username = username.toLowerCase();
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();

            transaction.begin();

            Query query = entityManager.createQuery("SELECT a FROM User a WHERE lower(a.name) = :name",User.class)
                    .setParameter("name",username);

            User user = (User) query.getSingleResult();
            if(user != null)
            {
                user.setName(newUsername);
                entityManager.merge(user);
            }
            else {result = false;}

            transaction.commit();
        } catch (Exception e)
        {
            result = false;
            if(transaction != null) transaction.rollback();
        }
        return result;
    }

    public boolean isLogin(String username, String password) throws SQLException
    {
        boolean result = false;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            username = username.toLowerCase();
            entityManager = managerFactory.createEntityManager();

            transaction = entityManager.getTransaction();
            transaction.begin();

            Query query = entityManager.createQuery("SELECT a FROM User a WHERE lower(a.name) = :name AND a.password = :password",User.class)
                    .setParameter("name",username)
                    .setParameter("password",password);

            User user = (User) query.getSingleResult();
            if(user != null)    {result = true;}
            else                {result = false;}

            transaction.commit();
        } catch (Exception e)
        {
            result = false;
            if(transaction != null) transaction.rollback();
        }
        return result;
    }

    public boolean createUser(User user)
    {
        boolean result = true;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(user);

            transaction.commit();
        } catch (Exception e)
        {
            result = false;
            if(transaction != null) transaction.rollback();
        }
        return result;
    }

    public void deleteUserAndDataService(int userId) {
        EntityManager entityManager;
        entityManager = managerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Pobierz użytkownika do usunięcia
            User user = entityManager.find(User.class, userId);

            if (user != null) {
                // Usuń użytkownika
                entityManager.remove(user);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

}
