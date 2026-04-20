package com.weather.weatherviewer.dao;

import com.weather.weatherviewer.entity.Users;
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

    public void save (Users user){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
        session.close();
    }
        public Users findByUsername(String username){
        Session session = sessionFactory.openSession();
        Users user = session.createQuery("from Users where username = :username",Users.class)
                .setParameter("username",username).uniqueResult();
        session.close();
        return user;

    }
    public Users findById(Long id ){
        Session session = sessionFactory.openSession();
        Users user = session.get(Users.class , id);
        session.close();
        return user;
    }
    public void deleteUserById(long userId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Users user = session.get(Users.class ,userId);
        if (user==null){
            transaction.rollback();
            session.close();
            return;
        }
        session.remove(user);
        transaction.commit();
        session.close();
    }

}

