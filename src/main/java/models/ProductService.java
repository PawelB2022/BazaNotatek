//package models;
//
//import entities.Product;
//import controllers.Main;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Query;
//import java.util.List;
//
//public class ProductService
//{
//    private EntityManagerFactory managerFactory = Main.getFactory();
//
//    public List<Product> findAll()
//    {
//        List<Product> products;
//        EntityManager entityManager;
//        EntityTransaction transaction = null;
//        try
//        {
//            entityManager = managerFactory.createEntityManager();
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//
//            Query query = entityManager.createQuery("SELECT a FROM Product a",Product.class);
//            products = query.getResultList();
//
//            transaction.commit();
//        } catch (Exception e)
//        {
//            if(transaction != null) transaction.rollback();
//            throw new RuntimeException(e);
//        }
//        return products;
//    }
//
//    public List<Product> search(double min, double max)
//    {
//        List<Product> products;
//        EntityManager entityManager;
//        EntityTransaction transaction = null;
//        try
//        {
//            entityManager = managerFactory.createEntityManager();
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//
//            Query query = entityManager.createQuery("SELECT a FROM Product a WHERE a.price >= :min AND a.price <= :max",Product.class)
//                    .setParameter("min",min)
//                    .setParameter("max",max);
//            products = query.getResultList();
//
//            transaction.commit();
//        } catch (Exception e)
//        {
//            if(transaction != null) transaction.rollback();
//            throw new RuntimeException(e);
//        }
//        return products;
//    }
//
//    public Product findOne(int id)
//    {
//        Product product = null;
//        EntityManager entityManager;
//        EntityTransaction transaction = null;
//        try
//        {
//            entityManager = managerFactory.createEntityManager();
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//
//            product = entityManager.find(Product.class,id);
//
//            transaction.commit();
//        } catch (Exception e)
//        {
//            if(transaction != null) transaction.rollback();
//        }
//        return product;
//    }
//
//    public boolean create(Product product)
//    {
//        boolean result = true;
//        EntityManager entityManager;
//        EntityTransaction transaction = null;
//        try
//        {
//            entityManager = managerFactory.createEntityManager();
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//
//            entityManager.persist(product);
//
//            transaction.commit();
//        } catch (Exception e)
//        {
//            result = false;
//            if(transaction != null) transaction.rollback();
//        }
//        return result;
//    }
//
//    public boolean update(int id, Product updatedProduct)
//    {
//        boolean result = true;
//        EntityManager entityManager;
//        EntityTransaction transaction = null;
//        try
//        {
//            entityManager = managerFactory.createEntityManager();
//            Product product = entityManager.find(Product.class,id);
//
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//
//            product.setName(updatedProduct.getName());
//            entityManager.merge(product);
//
//            transaction.commit();
//        } catch (Exception e)
//        {
//            result = false;
//            if(transaction != null) transaction.rollback();
//        }
//        return result;
//    }
//
//    public boolean delete(int id)
//    {
//        boolean result = true;
//        EntityManager entityManager;
//        EntityTransaction transaction = null;
//        try
//        {
//            entityManager = managerFactory.createEntityManager();
//            Product product = entityManager.find(Product.class,id);
//
//            transaction = entityManager.getTransaction();
//            transaction.begin();
//
//            entityManager.remove(product);
//
//            transaction.commit();
//        } catch (Exception e)
//        {
//            result = false;
//            if(transaction != null) transaction.rollback();
//        }
//        return result;
//    }
//}
