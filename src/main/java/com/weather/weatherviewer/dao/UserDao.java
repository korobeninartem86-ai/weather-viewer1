package com.weather.weatherviewer.dao;

import com.weather.weatherviewer.entity.Users;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Users user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public Users findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Users where username = :username", Users.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    public Users findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Users.class, id);
        }
    }

    public void deleteUserById(long userId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Users user = session.get(Users.class, userId);
            if (user == null) {
                transaction.commit();
                return;
            }
            session.remove(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

}



