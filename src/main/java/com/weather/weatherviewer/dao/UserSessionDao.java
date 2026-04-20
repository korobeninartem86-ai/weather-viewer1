package com.weather.weatherviewer.dao;

import com.weather.weatherviewer.entity.UserSession;
import org.apache.catalina.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserSessionDao {
    private final SessionFactory sessionFactory;

    public UserSessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void save (UserSession userSession) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(userSession);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public UserSession findBySessionId (UUID idSession){
        Session session = sessionFactory.openSession();
        UserSession userSession = session.get(UserSession.class,idSession);
        session.close();
        return userSession;
    }


    public void deleteBySessionId( UUID sessionId){
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction =session.beginTransaction();
            UserSession userSession = session.get(UserSession.class , sessionId);
            if (userSession == null){
                return;
            }
            session.remove(userSession);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }
    public List<UserSession> findAllByUserId(long userId){

        Session session = null;
            session = sessionFactory.openSession();
            List<UserSession>userSessions = session.createQuery("FROM UserSession where userId=:userId",UserSession.class).setParameter("userId",userId).list();
            session.close();
            return userSessions;

    }


}
