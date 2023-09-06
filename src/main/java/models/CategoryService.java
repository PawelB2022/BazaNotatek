package models;

import entities.Category;
import controllers.Main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class CategoryService
{
    private EntityManagerFactory managerFactory = Main.getFactory();

    public Category findOne(int id)
    {
        Category category = null;
        EntityManager entityManager;
        EntityTransaction transaction = null;
        try
        {
            entityManager = managerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();

            category = entityManager.find(Category.class,id);

            transaction.commit();
        } catch (Exception e)
        {
            if(transaction != null) transaction.rollback();
        }
        return category;
    }
}
